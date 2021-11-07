package org.peut.herdenk.service;

import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.exceptions.FileNotFoundException;
import org.peut.herdenk.model.Grave;
import org.peut.herdenk.model.Reaction;
import org.peut.herdenk.model.User;
import org.peut.herdenk.repository.ReactionRepository;
import org.peut.herdenk.utility.Access;
import org.peut.herdenk.config.HerdenkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ReactionService {

    private String uploads;
    @Autowired
    private HerdenkConfig herdenkConfig;

    private final ReactionRepository reactionRepository;
    private final AuthorityService authorityService;
    private final GraveService graveService;
    private final UserService userService;
    @Autowired
    public ReactionService(
            ReactionRepository reactionRepository,
            AuthorityService authorityService,
            GraveService graveService,
            UserService userService)
    {
        this.reactionRepository = reactionRepository;
        this.authorityService = authorityService;
        this.graveService = graveService;
        this.userService = userService;
    }

    public Reaction getReaction( Long reactionId ){
        Optional<Reaction> optionalReaction = reactionRepository.findById( reactionId );
        if ( optionalReaction.isEmpty() )return null;
        return optionalReaction.get();
    }


    public List<Reaction> getReactions(){
        return  reactionRepository.findAll();
    }

    public List<Reaction> getReactionsForUser( Long userId){
        return  reactionRepository.findAllByUserId( userId );
    }

    public List<Reaction> getReactionsForReaction( Long reactionId) {
        List<Reaction> reactions = new ArrayList<>();
        Optional<Reaction> optionalReaction = reactionRepository.findById(reactionId);
        optionalReaction.ifPresent(reactions::add);
        return reactions;
    }

    public List<Reaction> getReactionsForGrave(Long graveId ){
        List<Reaction> reactions;
        try {
             reactions = graveService.getGrave(graveId).getReactions();
        }catch(Exception e){
            reactions = new ArrayList<>();
        }
        return reactions;
    }

    public List<Reaction> deleteReaction(Long reactionId ) {
        List<Reaction> reactions = new ArrayList<>();
        Reaction reaction = new Reaction();

        Optional<Reaction> optionalReaction = reactionRepository.findById(reactionId);
        if (optionalReaction.isPresent()){
            reaction = optionalReaction.get();
            reactions.add( reaction );

            reactionRepository.delete( reaction );
        }

        deleteDirectory( Paths.get("/media/" + reaction.getGraveId() + "/" + reaction.getReactionId() ).toFile()  );

        return reactions;
    }


    boolean deleteDirectory( File directoryToBeDeleted ){

        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public List<Reaction>  findPermissionQuestions( Long graveId ) {
        List<Reaction> reactions;
        try {
            reactions = reactionRepository.findReactionsByGraveIdAndType( graveId, "PERMISSION");
        }catch(Exception e ){
            throw new FileNotFoundException( "No permission questions found for grave " + graveId);
        }
        return  reactions;
    }

    public List<Reaction>  askPermission( Long graveId, String permission) {
        String userEmail = authorityService.getCurrentUser();
        User user = userService.getUserByEmail( userEmail );

        if ( !permission.equals(Access.READ.name()) && !permission.equals( Access.WRITE.name()) ){
            throw new BadRequestException("Permission must be either " + Access.READ.name() + " or " + Access.WRITE.name() );
        }

        Grave grave;
        try {
            grave = graveService.getGrave(graveId);
        }catch( Exception e ){
            throw new FileNotFoundException("Grave with id " + graveId + " does not exist");
        }

        Reaction reaction = new Reaction();

        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append( "User ");
        textBuilder.append(  user.getFullName() );
        textBuilder.append( " with email " + user.getEmail() + " " );
        textBuilder.append( "wants " + permission + " access ");
        textBuilder.append( "to the grave of " + grave.getOccupantFullName() + ".");

        reaction.setType("PERMISSION");
        reaction.setGraveId( graveId);
        reaction.setUserId( user.getUserId() );
        reaction.setUserName( user.getFullName() );
        reaction.setText( textBuilder.toString() );

        reaction = reactionRepository.save( reaction );

        List<Reaction> reactions = new ArrayList<>();
        reactions.add( reaction);

        return reactions;
    }

    public List<Reaction> registerReaction(Reaction reaction, MultipartFile multipartFile) {
        Reaction fullReaction;

        String userEmail = authorityService.getCurrentUser();
        User   user = userService.getUserByEmail( userEmail );
        reaction.setUserId(  user.getUserId() );
        reaction.setUserName(  user.getFullName() );

        // save first to get a reaction id, which is needed in the mediaPath
        reaction = reactionRepository.save( reaction );

        if ( multipartFile != null) {
            try {
                fullReaction = saveMediaFile(reaction, multipartFile);
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }
            fullReaction.setType("MEDIA");
        }else{
            fullReaction = reaction;
            fullReaction.setMediaPath(null);
            fullReaction.setType("TEXT");
        }

        System.out.println( "Reaction type = " + fullReaction.getType() );

        fullReaction = reactionRepository.save(fullReaction);

        List<Reaction> reactions = new ArrayList<>();
        reactions.add(fullReaction);
        return reactions;
    }



    public Reaction saveMediaFile(
            Reaction reaction,
            MultipartFile multipartFile) throws IOException {

        System.out.println("Uploads to " + herdenkConfig.getUploads());
        String uploadDir =  herdenkConfig.getUploads() +
                reaction.getGraveId() +
                "/" +
                reaction.getReactionId() ;

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Path filePath;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }

        reaction.setMediaPath( filePath.toString().replace("\\", "/").replace(herdenkConfig.getUploads(), "/media/")  );
        return reaction;
    }

}
