package org.peut.herdenk.service;

import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.model.dto.AuthenticationRequest;
import org.peut.herdenk.model.dto.AuthenticationResponse;
import org.peut.herdenk.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtl;

    @Autowired
    public UserAuthenticateService(
        AuthenticationManager authenticationManager,
        UserDetailsService userDetailsService,
        JwtUtil jwtUtl)
    {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtl = jwtUtl;

    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {

        String email    = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate( authentication );

        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Incorrect username or password");
        } catch( AuthenticationException e){
            throw new RuntimeException("Something went wrong in authentication", e);
        } catch( Exception e){
            throw new RuntimeException("Something went wrong", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String jwt = jwtUtl.generateToken(userDetails);

        return new AuthenticationResponse(jwt);
    }

}
