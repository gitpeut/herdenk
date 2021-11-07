package org.peut.herdenk.model.dto;

import lombok.Data;
import org.peut.herdenk.model.Reaction;

import java.util.Date;

@Data
public class ReactionRequestDto {
    private Long graveId;
    private String type;
    private String text;
    private String mediaPath;
}

