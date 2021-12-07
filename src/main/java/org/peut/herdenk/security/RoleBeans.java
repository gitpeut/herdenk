package org.peut.herdenk.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class RoleBeans {

    public RoleBeans(){}

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;
        if (authentication.isAuthenticated()) {
            if ( authentication.getPrincipal().equals("anonymousUser") || authentication.getPrincipal().equals("") ) {
                System.out.println("isAdmin found user to Anonymous user " + authentication.getPrincipal() );
                return false;
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

}
