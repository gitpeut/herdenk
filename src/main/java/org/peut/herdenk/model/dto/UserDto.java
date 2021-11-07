package org.peut.herdenk.model.dto;

import lombok.Data;
import org.peut.herdenk.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private Long userId;

    @NotBlank
    @Size(min=4, max=80)
    private String  fullName;
    @NotBlank
    @Email
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
