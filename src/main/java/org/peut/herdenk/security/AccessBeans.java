package org.peut.herdenk.security;

import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.model.User;
import org.peut.herdenk.service.AuthorityService;
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

    @Autowired
    public AccessBeans(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }



    public boolean isSelfOrIsAdmin( Long userId ){

        User   registeredUser;
        try {
            registeredUser = userService.getUser(userId);
        }catch( Exception e ){
            return false;
        }

        String pathUser = registeredUser.getEmail();
        String username = "";

        if ( isAdmin() ) return  true;

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

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;
        if (authentication.isAuthenticated()) {
            if ( authentication.getPrincipal().equals("anonymousUser") ) {
                return false;
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

}
//https://stackoverflow.com/questions/42673084/using-other-bean-and-method-in-spring-security-preauthorize