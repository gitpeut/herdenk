package org.peut.herdenk.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorityKey implements Serializable {
    private Long userId;
    private Long graveId;

    public AuthorityKey(){}
    public AuthorityKey( Long userId, Long graveId ){
        this.userId = userId;
        this.graveId = graveId;
    }
}

