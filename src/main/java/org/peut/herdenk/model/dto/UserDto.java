package org.peut.herdenk.model.dto;

import lombok.Data;
import org.peut.herdenk.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class UserDto {

    private Long userId;
    private String  fullName;
    private String  email;
    private boolean enabled;
    private String  password;
    private String  role;
    private List<AuthorityDto> authorities;

    public static UserDto from(User user){
        UserDto userDto = new UserDto();

        userDto.setUserId( user.getUserId() );
        userDto.setFullName( user.getFullName());
        userDto.setEmail( user.getEmail() );
        userDto.setEnabled( user.isEnabled() );
        userDto.setPassword( "-- Intentionally left blank --");
        userDto.setRole( user.getRole());
        if (Objects.nonNull( user.getAuthorities() )){
            userDto.setAuthorities( user.getAuthorities().stream().map(AuthorityDto::from).collect(Collectors.toList() ));
        }
        return userDto;
    }
}
