package org.peut.herdenk.model.dto;


import lombok.Data;
import org.peut.herdenk.model.Grave;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class GraveDto {


    private Long    graveId;
    private String  occupantFullName;
    private Date    creationDate;
    private List<AuthorityDto> authorities;
    private List<ReactionResponseDto> reactions;

    public static GraveDto from(Grave grave){
            org.peut.herdenk.model.dto.GraveDto graveDto = new org.peut.herdenk.model.dto.GraveDto();
            graveDto.setGraveId( grave.getGraveId() );
            graveDto.setOccupantFullName( grave.getOccupantFullName());
            graveDto.setCreationDate( grave.getCreationDate());

            if (Objects.nonNull( grave.getAuthorities() )){
                graveDto.setAuthorities( grave.getAuthorities().stream().map(AuthorityDto::from).collect(Collectors.toList() ));
            }
            if (Objects.nonNull( grave.getReactions() )){
                graveDto.setReactions( grave.getReactions().stream().map( ReactionResponseDto::from ).collect(Collectors.toList() ));
            }
            return graveDto;
     }



}
