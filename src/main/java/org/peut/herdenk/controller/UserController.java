package org.peut.herdenk.controller;

import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.model.User;
import org.peut.herdenk.model.dto.UserDto;
import org.peut.herdenk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
// NOT /api/v1/users, as this would make it impossible for new users to register due to
// tha general antMatcher ruls for .../users in SecurityConfiguration
@RequestMapping( path = "/api/v1")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping( value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity handle() { return new ResponseEntity(HttpStatus.OK); }

    @GetMapping( path="/users/all" )
    public ResponseEntity<List<UserDto>> getUsers() {

        List<User> users = userService.getUsers();
        List<UserDto> userDtos = users.stream().map( (UserDto::from) ).collect( Collectors.toList());

        return new ResponseEntity<>(userDtos,HttpStatus.OK);
    }

    @GetMapping( path="/users/{userId}" )
    public ResponseEntity<UserDto> getUser( @PathVariable("userId") Long userId) {

        User user = userService.getUser( userId );
        UserDto userDto = UserDto.from( user);

        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @GetMapping( path="/users/me" )
    public ResponseEntity<UserDto> getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth == null ) throw new BadRequestException("You are not logged in");
        if ( auth.getName().equals("anonymousUser") )throw new BadRequestException("You are not logged in"); // Should not happen, with fulAuthentication

        User user = userService.getUserByEmail( auth.getName() );
        UserDto userDto = UserDto.from( user);

        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @PostMapping(path="/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody final UserDto userDto){

           User user = User.from(userDto);
           user = userService.registerUser(user);

           return new ResponseEntity<>(UserDto.from(user), HttpStatus.OK);
    }


    @PutMapping( path="/users/{userId}" )
    public ResponseEntity<UserDto> updateUser( @RequestBody final UserDto userDto, @PathVariable("userId") Long userId ){

        User user = userService.updateUser( User.from( userDto ), userId );
        return new ResponseEntity<>( UserDto.from( user ), HttpStatus.OK);
    }

    @DeleteMapping( path="/users/{userId}" )
    public ResponseEntity<UserDto> deleteUser( @PathVariable("userId") Long userId ){
        User user = userService.deleteUser( userId );
        return new ResponseEntity<>( UserDto.from( user ), HttpStatus.OK);
    }


}
