package org.peut.herdenk.model;


import org.peut.herdenk.exceptions.BadRequestException;
import org.peut.herdenk.model.dto.AuthorityDto;
import org.peut.herdenk.utility.Access;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@IdClass(AuthorityKey.class)
@Table(name = "authorities")
public class Authority implements Serializable {

    @Id
    @Column(nullable = false)
    private Long userId;

    @Id
    @Column(nullable = false)
    private Long graveId;

    @Column(nullable = false, length = 8)
    private String authority;

    public Authority() {
    }

    public Authority(Long userId, Long graveId, String authority) {
        this.userId = userId;
        this.graveId = graveId;
        this.authority = authority;
    }

    public static Authority from(AuthorityDto authorityDto) {
        Authority authority = new Authority();

        authority.setUserId(authorityDto.getUserId());
        authority.setGraveId(authorityDto.getGraveId());

        if (Access.isNameValid(authorityDto.getAuthority())) {
            authority.setAuthority(authorityDto.getAuthority());
        } else {
            throw new BadRequestException(String.format("Access authority %s is invalid", authorityDto.getAuthority()));
        }
        return authority;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGraveId() {
        return graveId;
    }

    public void setGraveId(Long graveId) {
        this.graveId = graveId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
