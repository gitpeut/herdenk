package org.peut.herdenk.model.dto;

import lombok.Data;
import org.peut.herdenk.model.Reaction;
import org.peut.herdenk.service.GraveService;
import org.peut.herdenk.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@Data
public class ReactionResponseDto {


    private Long reactionId;
    private Long graveId;
    private Long userId;
    private String userName;
    private String type;
    private Date creationDate;
    private String text;
    private String mediaPath;

    // from app to frontend

    public static ReactionResponseDto from(Reaction reaction ){
        ReactionResponseDto reactionResponseDto = new ReactionResponseDto();

        reactionResponseDto.setReactionId( reaction.getReactionId());
        reactionResponseDto.setGraveId( reaction.getGraveId());
        reactionResponseDto.setUserId( reaction.getUserId());
        reactionResponseDto.setUserName( reaction.getUserName() );
        reactionResponseDto.setType( reaction.getType());
        reactionResponseDto.setCreationDate( reaction.getCreationDate());
        reactionResponseDto.setText( reaction.getText() );
        reactionResponseDto.setMediaPath( reaction.getMediaPath());

        return reactionResponseDto;
    }




}
