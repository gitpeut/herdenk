package org.peut.herdenk.model.dto;
import lombok.Data;
import org.peut.herdenk.model.Authority;
import org.peut.herdenk.model.Authority;
import org.peut.herdenk.model.Grave;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class AuthorityDto {

    private Long userId;
    private Long graveId;
    private String authority;

    public static AuthorityDto from(Authority authority){
        AuthorityDto authorityDto = new AuthorityDto();

        authorityDto.setUserId( authority.getUserId());
        authorityDto.setGraveId( authority.getGraveId() );
        authorityDto.setAuthority(authority.getAuthority() );

        return authorityDto;
    }
}
