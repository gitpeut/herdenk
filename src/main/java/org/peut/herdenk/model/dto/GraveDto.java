package org.peut.herdenk.model.dto;


import org.peut.herdenk.model.Grave;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class GraveDto {


    private Long    graveId;
    private String  occupantFullName;
    private Date    creationDate;
    private List<AuthorityDto> authorities;
    private List<ReactionDto> reactions;

    public static org.peut.herdenk.model.dto.GraveDto from(Grave grave){
            org.peut.herdenk.model.dto.GraveDto graveDto = new org.peut.herdenk.model.dto.GraveDto();
            graveDto.setGraveId( grave.getGraveId() );
            graveDto.setOccupantFullName( grave.getOccupantFullName());
            graveDto.setCreationDate( grave.getCreationDate());

            if (Objects.nonNull( grave.getAuthorities() )){
                graveDto.setAuthorities( grave.getAuthorities().stream().map(AuthorityDto::from).collect(Collectors.toList() ));
            }
            if (Objects.nonNull( grave.getReactions() )){
                graveDto.setReactions( grave.getReactions().stream().map(ReactionDto::from).collect(Collectors.toList() ));
            }
            return graveDto;
        }



    public Long getGraveId() {
        return graveId;
    }

    public void setGraveId(Long graveId) {
        this.graveId = graveId;
    }

    public String getOccupantFullName() {
        return occupantFullName;
    }

    public void setOccupantFullName(String occupantFullName) {
        this.occupantFullName = occupantFullName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<AuthorityDto> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityDto> authorities) {
        this.authorities = authorities;
    }

    public List<ReactionDto> getReactions() {
        return reactions;
    }

    public void setReactions(List<ReactionDto> reactions) {
        this.reactions = reactions;
    }
}
