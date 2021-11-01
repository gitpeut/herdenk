package org.peut.herdenk.service;

import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.exceptions.DuplicateException;
import org.peut.herdenk.model.User;
import org.peut.herdenk.repository.UserRepository;
import org.peut.herdenk.utility.Entropy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers(){
         return userRepository.findAll();
    }

    public User getUser( Long userId ){
        return userRepository.findById( userId ).orElseThrow( ()->new BadRequestException( String.format("User with id %d does not exist", userId)));
    }

    public User getUserByEmail( String email ){
        return userRepository.findUserByEmail( email ).orElseThrow( ()->new BadRequestException( String.format("User with email %s does not exist", email)));
    }

    public Long getUserIdByEmail( String email ){
        User user;
        try {
            user = getUserByEmail(email);
        }catch ( Exception e){
            throw new BadRequestException( e.getMessage());
        }
        return user.getUserId();
    }


    @Transactional
    public User updateUser( User newUser, Long userId ){

        User originalUser = getUser( userId );
        Optional<User> optionalUser;

        if ( newUser.getEmail() != null && !originalUser.getEmail().equals( newUser.getEmail() ) ) {
            optionalUser = userRepository.findUserByEmail(newUser.getEmail());
            if (optionalUser.isEmpty()) {
                originalUser.setEmail( newUser.getEmail() );
            }else{
                throw new DuplicateException(String.format("User with email %s already exists", newUser.getEmail()));
            }
        }

        if ( newUser.getFullName() != null && !originalUser.getFullName().equals( newUser.getFullName() ) ) {

            optionalUser = userRepository.findUserByFullName(newUser.getFullName());
            if ( optionalUser.isEmpty() ) {
                originalUser.setFullName( newUser.getFullName() );
            }else{
                throw new DuplicateException(String.format("User with full name %s already exists", newUser.getFullName()));
            }
        }


        if ( newUser.getPassword() != null && !originalUser.getPassword().equals( newUser.getPassword() ) ) {

            double passwordStrength = Entropy.getEntropy(newUser.getPassword());
            if (passwordStrength < 3.0) {
                throw new BadRequestException("Password is too weak");
            }

            String encryptedPassword = passwordEncoder.encode(newUser.getPassword());
            originalUser.setPassword( encryptedPassword );
        }

        if ( newUser.isEnabled() != originalUser.isEnabled() ) {
            originalUser.setEnabled(newUser.isEnabled());
        }

        originalUser = userRepository.save( originalUser );
        return ( originalUser );

    }


    public User deleteUser( Long userId ){

        User user = getUser( userId );
        userRepository.delete( user );

        return ( user );
    }



    public User registerUser( User user ){

        Optional<User> optionalUser    = userRepository.findUserByEmail( user.getEmail() );
        if ( optionalUser.isPresent() ){
            throw new DuplicateException(String.format( "User with email %s already exists", user.getEmail() ));
        }

        optionalUser    = userRepository.findUserByFullName( user.getFullName() );
        if ( optionalUser.isPresent() ){
            throw new DuplicateException(String.format( "User with full name %s already exists", user.getFullName() ));
        }

        double passwordStrength = Entropy.getEntropy( user.getPassword() );
        if ( passwordStrength < 3.0 ){
            throw new BadRequestException( "Password is too weak" );
        }
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword( encryptedPassword );

        user.setRole("ROLE_USER");
        user.setEnabled( true );
        // prevent authorities to be set at registration time, authorities should only be
        // added by owners or ADMIN.
        user.setAuthorities(null);
        user = userRepository.save( user );

        return( user );
    }

}
