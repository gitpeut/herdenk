package org.peut.herdenk.service;

import org.peut.herdenk.config.HerdenkConfig;
import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.exceptions.DuplicateException;
import org.peut.herdenk.exceptions.FileNotFoundException;
import org.peut.herdenk.model.Grave;
import org.peut.herdenk.model.Reaction;
import org.peut.herdenk.model.User;
import org.peut.herdenk.repository.ReactionRepository;
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
import java.util.*;


@Service
public class ReactionService {



    private final HerdenkConfig herdenkConfig;

    private final ReactionRepository reactionRepository;
    private final AuthorityService authorityService;
    private final GraveService graveService;
    private final UserService userService;
    @Autowired
    public ReactionService(
            ReactionRepository reactionRepository,
            AuthorityService authorityService,
            GraveService graveService,
            UserService userService,
            HerdenkConfig herdenkConfig)
    {
        this.reactionRepository = reactionRepository;
        this.authorityService = authorityService;
        this.graveService = graveService;
        this.userService = userService;
        this.herdenkConfig = herdenkConfig;
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
        List<Reaction> reactions;
        try {
            reactions = userService.getUser( userId ).getReactions();
        }catch(Exception e){
            reactions = new ArrayList<>(); //return empty list
        }
        return reactions;
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

        deleteDirectory( Paths.get(herdenkConfig.getUploads() + reaction.getGraveId()  + "/" + reaction.getReactionId() ).toFile()  );

        return reactions;
    }


    boolean deleteDirectory( File directoryToBeDeleted ){
        System.out.println( "Deleting " + directoryToBeDeleted.toString() + " exists? " + directoryToBeDeleted.exists() );

        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public List<Reaction>  findPermissionQuestions( Long graveId) {
        List<Reaction> reactions;

        reactions = findReactionType(graveId, "WRITE" );
        reactions.addAll( findReactionType(graveId, "READ" ));

        return reactions;
    }


    public List<Reaction> findReactionType(Long graveId, String type) {
        List<Reaction> reactions;

        reactions = reactionRepository.findReactionsByGraveIdAndType(graveId, type);

        return reactions;
    }

    public List<Reaction>  addReactionType( Long graveId, String type) {

        String [] permittedTypes = {"READ","WRITE","FLOWER","TEAR"};
        List<String>  checkTypeList = Arrays.asList( permittedTypes );

        if ( !checkTypeList.contains( type  ) ){
            throw new BadRequestException( "Unknown reaction type, should be one of "+ Arrays.toString( permittedTypes) );
        }

        User user;
        try {
            String userEmail = authorityService.getCurrentUser();
            user = userService.getUserByEmail(userEmail);
        }catch( Exception e ){
            throw new BadRequestException("User not found" );
        }

        Grave grave;
        try {
            grave = graveService.getGrave(graveId);
        }catch( Exception e ){
            throw new FileNotFoundException("Grave with id " + graveId + " does not exist");
        }

        Reaction reaction = new Reaction();

        if ( type.equals("READ") || type.equals("WRITE") ) {

            List<Reaction> existingPermissions = findPermissionQuestions( graveId );
            for( Reaction r : existingPermissions ){
                if ( Objects.equals( r.getUserId(), user.getUserId() ) &&
                        ( r.getType().equals("READ") || r.getType().equals("WRITE") )  ){
                    throw new DuplicateException("An applicaton to access grave " + grave.getOccupantFullName() + " by " + user.getFullName() + " is already active");
                }
            }

            String text = "";
            text += "User " + user.getFullName();
            text += " with email " + user.getEmail() + " ";
            text += "wants " + type + " access ";
            text += "to the grave of " + grave.getOccupantFullName() + ".";

            reaction.setType( type );
            reaction.setText( text );

//            StringBuilder textBuilder = new StringBuilder();
//            textBuilder.append("User ");
//            textBuilder.append(user.getFullName());
//            textBuilder.append(" with email " + user.getEmail() + " ");
//            textBuilder.append("wants " + type + " access ");
//            textBuilder.append("to the grave of " + grave.getOccupantFullName() + ".");
//            reaction.setType( type );
//            reaction.setText(textBuilder.toString());
        }

        if ( type.equals("FLOWER") || type.equals("TEAR") ) {
            reaction.setType( type );
            reaction.setText( user.getFullName() +  " shared a " + type );
        }

        reaction.setGraveId( graveId);
        reaction.setUserId( user.getUserId() );
        //reaction.setUserName( user.getFullName() );

        reaction = reactionRepository.save( reaction );

        List<Reaction> reactions = new ArrayList<>();
        reactions.add( reaction);

        return reactions;
    }

    public List<Reaction> updateReaction(Reaction reaction, MultipartFile multipartFile) {
        Reaction oldReaction;
        try {
            oldReaction = reactionRepository.getById(reaction.getReactionId());
        }catch( Exception e ){
            throw new FileNotFoundException( " Reaction not found, cannot be updated" );
        }


        if ( multipartFile != null  ){
            if ( oldReaction.getType().equals("MEDIA") ) {
                deleteDirectory(Paths.get(herdenkConfig.getUploads() + oldReaction.getGraveId() + "/" + oldReaction.getReactionId()).toFile());
            }
            try {
                oldReaction = saveMediaFile( oldReaction, multipartFile);
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }
            oldReaction.setType("MEDIA");
        }else{
            if ( oldReaction.getType().equals("MEDIA") && reaction.getMediaPath() == null ){
                deleteDirectory(Paths.get(herdenkConfig.getUploads() + oldReaction.getGraveId() + "/" + oldReaction.getReactionId()).toFile());

                oldReaction.setMediaPath( null);
                oldReaction.setType("TEXT");
            }
        }

        oldReaction.setText(reaction.getText());
        oldReaction = reactionRepository.save( oldReaction );

        List<Reaction> reactions = new ArrayList<>();
        reactions.add( oldReaction);
        return reactions;
    }

    public List<Reaction> registerReaction(Reaction reaction, MultipartFile multipartFile) {
        Reaction fullReaction;

        String userEmail = authorityService.getCurrentUser();
        User   user = userService.getUserByEmail( userEmail );
        reaction.setUserId(  user.getUserId() );
        //reaction.setUserName(  user.getFullName() );

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

        String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Path   filePath;

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
