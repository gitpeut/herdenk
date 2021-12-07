package org.peut.herdenk.security;

import org.peut.herdenk.model.Reaction;
import org.peut.herdenk.model.User;
import org.peut.herdenk.service.AuthorityService;
import org.peut.herdenk.service.ReactionService;
import org.peut.herdenk.service.UserService;
import org.peut.herdenk.utility.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component(value="AccessBeans")
public class AccessBeans {

    private final UserService userService;
    private final AuthorityService authorityService;
    private final ReactionService reactionService;
    private final RoleBeans roleBeans;

    @Autowired
    public AccessBeans(UserService userService,
                       AuthorityService authorityService,
                       ReactionService reactionService,
                       RoleBeans roleBeans) {

        this.userService = userService;
        this.authorityService = authorityService;
        this.reactionService = reactionService;
        this.roleBeans = roleBeans;

    }



    public boolean isSelfOrIsAdmin( Long userId ){

        User   registeredUser;
        if ( isAdmin() ) return  true;

        try {
            registeredUser = userService.getUser(userId);
        }catch( Exception e ){
            return false;
        }

        String pathUser = registeredUser.getEmail();
        String username = "";


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() ) {

            username = authentication.getName();
            System.out.println("User: " + username );

            if ( username.equalsIgnoreCase(pathUser)){
                return true;
            }
        }
        System.out.println("Forbidden by isSelf");
        System.out.println("isSelf called by user " + username + " for user " + pathUser);
        return false;
    }

    public boolean hasGraveAccessOrIsAdmin( Long graveId ){
        if ( isAdmin() ) return true;
        if ( authorityService.isGravePublic( graveId ) ) return true;
        return authorityService.isGraveAccessibleByUser(graveId);
    }

    public boolean hasAtLeastWriteAccess( Long graveId ){
        if ( isAdmin() ) return true;
        return authorityService.isGraveAccessAtLeast( graveId, Access.WRITE.name() );
    }

    public boolean hasAtLeastOwnerAccess( Long graveId ){
        if ( isAdmin() ) return true;
        return authorityService.isGraveAccessAtLeast( graveId, Access.OWNER.name() );
    }

    public boolean isGraveOwnerOrAuthor( Long reactionId ){

        System.out.println( "reactionid " + reactionId );
        Reaction reaction;
        try {
            reaction = reactionService.getReaction(reactionId);
        }catch( Exception e ){
            return false;
        }

        if ( reaction == null )return false;

        if ( authorityService.isGraveAccessAtLeast( reaction.getGraveId(), Access.OWNER.name() ) ) return true;

        return isSelfOrIsAdmin( reaction.getUserId() ) ;
    }

    public boolean isAdmin() {
        return roleBeans.isAdmin();
    }

}
//https://stackoverflow.com/questions/42673084/using-other-bean-and-method-in-spring-security-preauthorize