package org.peut.herdenk.controller;

import org.peut.herdenk.model.User;
import org.peut.herdenk.model.dto.UserDto;
import org.peut.herdenk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping( path = "/api/v1/users")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {

        List<User> users = userService.getUsers();
        List<UserDto> userDtos = users.stream().map( (UserDto::from) ).collect( Collectors.toList());

        return new ResponseEntity<>(userDtos,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody final UserDto userDto){

           User user = User.from(userDto);
           user = userService.registerUser(user);

           return new ResponseEntity<>(UserDto.from(user), HttpStatus.OK);
    }

    @PutMapping( path="{userId}" )
    public ResponseEntity<UserDto> updateUser( @RequestBody final UserDto userDto, @PathVariable("userId") Long userId ){

        User user = userService.updateUser( User.from( userDto ), userId );
        return new ResponseEntity<>( UserDto.from( user ), HttpStatus.OK);
    }

    @DeleteMapping( path="{userId}" )
    public ResponseEntity<UserDto> deleteUser( @PathVariable("userId") Long userId ){
        User user = userService.deleteUser( userId );
        return new ResponseEntity<>( UserDto.from( user ), HttpStatus.OK);
    }


}
