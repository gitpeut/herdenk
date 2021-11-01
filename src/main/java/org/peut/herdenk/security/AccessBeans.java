package nl.novi.security;

import nl.novi.model.User;

import nl.novi.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Component(value="uBean")
public class uBean {


    public boolean isU(){
        String username = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() ) {
            UserDetails user = (UserDetails) authentication.getPrincipal();

            username = authentication.getName();
            System.out.println("User: " + user.toString() );

            if ( !username.startsWith("u")) {
                System.out.println("Granted access by uBean");
                return true;
            }

        }
        System.out.println("Forbidden by uBean");
        System.out.println("isU called for user " + username );
        return false;
    }

    public boolean isSelfOrHasRole( String pathUser, String roleName){
        String username = "";

        if ( !roleName.startsWith("ROLE_"))roleName = "ROLE_" + roleName;
        final String requiredRole = roleName;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() ) {


            username = authentication.getName();
            System.out.println("User: " + username );

            if ( username.equalsIgnoreCase(pathUser)){
                System.out.println("It's me you're looking for, access granted.");
                return true;
            }

            UserDetails user = (UserDetails) authentication.getPrincipal();

            //System.out.println( username + " has roles:");
            //user.getAuthorities().stream().forEach( a -> System.out.println(a.getAuthority().toString()));

            if ( user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals( requiredRole )) ){
                System.out.println( username + " has " + requiredRole);
                return( true );
            }else{
                System.out.println( username + " seems not to have " + requiredRole + ", it has roles:");
            }

        }
        System.out.println("Forbidden by isSelf");
        System.out.println("isSelf called by user " + username + " for user " + pathUser);
        return false;
    }


}
//https://stackoverflow.com/questions/42673084/using-other-bean-and-method-in-spring-security-preauthorize