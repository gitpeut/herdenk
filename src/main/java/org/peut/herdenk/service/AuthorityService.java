package org.peut.herdenk.service;

import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.exceptions.DuplicateException;
import org.peut.herdenk.model.Authority;
import org.peut.herdenk.model.AuthorityKey;
import org.peut.herdenk.repository.AuthorityRepository;
import org.peut.herdenk.utility.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final UserService userService;

    @Autowired
    public AuthorityService(
            AuthorityRepository authorityRepository,
            UserService userService)
    {
        this.authorityRepository = authorityRepository;
        this.userService = userService;
    }

    public List<Authority> getAuthorities(){
        return authorityRepository.findAll();
    }

    public Authority getAuthority( AuthorityKey key ){
        return authorityRepository.findById(key).
                orElseThrow( ()->new BadRequestException(
                        String.format("Authority with user id %d and grave id %d does not exist",
                                key.getUserId(), key.getGraveId())));
    }

    public List<Authority> getAuthoritiesByUser(Long userId) {
        return authorityRepository.findAccessibleByUserId(userId);
    }

    public List<Authority> getAuthoritiesByGrave(Long graveId) {
        return authorityRepository.findAllByGraveId(graveId);
    }


    public Authority registerAuthority( Authority authority ){

        Optional<Authority> optionalAuthority = authorityRepository.findById( keyFromAuthority(authority));
        if ( optionalAuthority.isPresent() ){
            System.out.println( "ERROR registering authority, already present " + authority );
            throw new DuplicateException(String.format( "Authority with userid %d grave id %d already exists", authority.getUserId(), authority.getGraveId() ));
        }

        // UserId 0 is used to signal public access, but is not a registered User
        if ( authority.getUserId() != 0L ){
            try{
                userService.getUser( authority.getUserId() );
            }catch( Exception e ) {
                throw new BadRequestException(e.getMessage());
            }
        }
        System.out.println( "register Saving authority " + authority );
        authority = authorityRepository.save( authority  );

        return ( authority );
    }

    public Authority updateAuthority( Authority authority ){

        Authority oldAuthority;

        Optional<Authority> optionalAuthority = authorityRepository.findById( keyFromAuthority(authority) );
        if ( optionalAuthority.isEmpty() ){
            throw new BadRequestException(String.format( "Authority with userid %d grave id %d does not exists", authority.getUserId(), authority.getGraveId() ));
        }
        oldAuthority = optionalAuthority.get();

        if ( oldAuthority.getAuthority().equals(Access.OWNER.name()) &&
                !authority.getAuthority().equals(Access.OWNER.name()) ){

                Optional<List<Authority>> optionalOwners = authorityRepository.findOwnerByGraveId( oldAuthority.getGraveId() );
                if ( optionalOwners.isPresent() ) {
                    List<Authority> owners = optionalOwners.get();
                    if (owners.size() == 1) {
                        throw new BadRequestException( "Grave must have at least owner, you cannot delete the only owner");
                    }
                }
        }

        authority = authorityRepository.save( authority  );

        return ( authority );
    }




    public Authority deleteAuthority( AuthorityKey authorityKey){
        Authority authority;
        Long userId;

        try {
            authority = getAuthority(authorityKey);
            userId = userService.getUserIdByEmail( getCurrentUser() );
        }catch( Exception e){
            throw new BadRequestException( e.getMessage() );
        }
        if ( authorityKey.getUserId().equals(userId) ){
            throw new BadRequestException("Cannot delete one's own OWNER authority");
        }
        authorityRepository.delete( authority );
        return( authority);
    }

    private AuthorityKey keyFromAuthority( Authority authority){
        return new AuthorityKey( authority.getUserId(), authority.getGraveId() );
    }

    public boolean isGravePublic( Long graveId ){
        Optional<Authority> optionalAuthority = authorityRepository.findPubliclyAccessibleByGraveId( graveId);
        return optionalAuthority.isPresent();
    }

    public boolean isGraveAccessibleByUser( Long graveId ){
        Long userId;
        try {
            userId = userService.getUserIdByEmail(getCurrentUser());
        }catch( Exception e ){
            return false;
        }
        Optional<Authority> optionalAuthority = authorityRepository.findGraveAccessibleByUserId( userId, graveId );
        return optionalAuthority.isPresent();
    }

    public String getGraveUserAccess( Long graveId ){
        Long userId = userService.getUserIdByEmail( getCurrentUser() );
        String access = Access.NONE.name();
        AuthorityKey key = new AuthorityKey(userId, graveId);

        if ( isGravePublic( graveId ) ) {
            access = Access.PUBLIC.name();
        }
        try {
            Authority authority = getAuthority(key);
            access = authority.getAuthority();
        }catch( Exception e){
            return access;
        }
        return access;
    }



    public boolean isGraveAccessAtLeast( Long graveId, String access){
        Long userId;
        try {
            userId = userService.getUserIdByEmail(getCurrentUser());
        }catch( Exception e){
            return false;
        }
        Authority authority;

        AuthorityKey key = new AuthorityKey( userId, graveId);
        try {
            authority = getAuthority(key);
        }catch( Exception e ){
            return false;
        }
        return Access.atLeast( authority.getAuthority(), access );
    }

    public void setGravePublic( Long graveId, boolean publiclyAccessible ){

        Optional<Authority> optionalAuthority = authorityRepository.findPubliclyAccessibleByGraveId( graveId);
        Authority authority;

        if (optionalAuthority.isEmpty()){
              authority = new Authority();
        }else {
              authority = optionalAuthority.get();
        }

        authority.setUserId( 0L );
        authority.setGraveId( graveId );
        authority.setAuthority( publiclyAccessible? Access.PUBLIC.name() : Access.NONE.name() );

        try {
            authorityRepository.save(authority);
        }catch(Exception e){
            throw new RuntimeException( String.format("Error saving authority %s ", authority ), e );
        }

    }

    public void setGraveOwner(Long graveId ){
        Long userId = userService.getUserIdByEmail( getCurrentUser() );
        Authority authority = new Authority(userId,graveId,"OWNER");
        authorityRepository.save( authority  );

    }

    public String getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth == null ) return "";
        if ( auth.getName().equals("anonymousUser") )return(""); // Should not happen, with fulAuthentication
                                                                 // properly configured for all functional endpoints
                                                                 // in Security config
        return auth.getName();
    }

}
