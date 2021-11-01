package org.peut.herdenk.model.dto;

import lombok.Data;
import org.peut.herdenk.model.Grave;
import org.peut.herdenk.model.Reaction;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class ReactionDto {

    private Long reactionId;
    private Long graveId;
    private Long userId;
    private int  type;
    private Date creationDate;
    private String text;
    private String mediaPath;

    public static org.peut.herdenk.model.dto.ReactionDto from(Reaction reaction){
        org.peut.herdenk.model.dto.ReactionDto reactionDto = new org.peut.herdenk.model.dto.ReactionDto();

        reactionDto.setReactionId( reaction.getReactionId());
        reactionDto.setGraveId( reaction.getGraveId() );
        reactionDto.setUserId( reaction.getUserId());
        reactionDto.setType(reactionDto.getType());
        reactionDto.setCreationDate( reaction.getCreationDate());
        reactionDto.setText( reaction.getText() );
        reactionDto.setMediaPath( reaction.getMediaPath());

        return reactionDto;
    }




}
