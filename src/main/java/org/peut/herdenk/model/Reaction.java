package org.peut.herdenk.model;

import org.hibernate.annotations.CreationTimestamp;
import org.peut.herdenk.model.dto.GraveDto;
import org.peut.herdenk.model.dto.ReactionDto;

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

    @Column int type;

    @Column
    @CreationTimestamp
    private Date creationDate;

    @Column(nullable = false, length = 2048)
    private String text;

    @Column(nullable = false, length = 512)
    private String mediaPath;

    public static Reaction from( ReactionDto reactionDto){
        Reaction reaction = new Reaction();

        reaction.setReactionId(reactionDto.getReactionId());
        reaction.setGraveId(reactionDto.getGraveId());
        reaction.setUserId(reactionDto.getUserId());
        reaction.setType(reactionDto.getType());
        reaction.setCreationDate( reactionDto.getCreationDate());
        reaction.setText(reactionDto.getText());
        reaction.setMediaPath(reactionDto.getMediaPath());

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
