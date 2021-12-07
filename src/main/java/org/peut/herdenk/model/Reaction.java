package org.peut.herdenk.model;

import org.hibernate.annotations.CreationTimestamp;
import org.peut.herdenk.model.dto.ReactionRequestDto;
import org.peut.herdenk.model.dto.ReactionResponseDto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reactions")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    @Column
    private Long graveId;

    @Column
    private Long userId;


    @Column(length = 16)
    private String type;

    @Column
    @CreationTimestamp
    private Date creationDate;

    @Column(length = 2048)
    private String text;

    @Column(length = 512)
    private String mediaPath;

    // From frontend to app
    public static Reaction from( ReactionRequestDto reactionRequestDto){
        Reaction reaction = new Reaction();

        reaction.setGraveId(reactionRequestDto.getGraveId());
        reaction.setType(reactionRequestDto.getType());
        reaction.setText(reactionRequestDto.getText());
        reaction.setMediaPath(reactionRequestDto.getMediaPath());

        return reaction;
    }


    public Long getReactionId() {
        return reactionId;
    }

    public void setReactionId(Long reactionId) {
        this.reactionId = reactionId;
    }

    public Long getGraveId() {
        return graveId;
    }

    public void setGraveId(Long graveId) {
        this.graveId = graveId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType( String type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }


}
