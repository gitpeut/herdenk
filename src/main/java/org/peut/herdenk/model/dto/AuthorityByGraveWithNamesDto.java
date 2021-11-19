package org.peut.herdenk.model.dto;

import lombok.Data;

@Data
public class AuthorityByGraveWithNamesDto {
    private Long    graveId;
    private String  occupantFullName;
    private String  userFullName;
    private String  access;
}
