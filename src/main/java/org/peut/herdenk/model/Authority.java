package org.peut.herdenk.model;

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

    @Column(nullable = false)
    private String authority;

    public Authority() {
    }

    public Authority(Long userId, Long graveId, String authority) {
        this.userId = userId;
        this.graveId = graveId;
        this.authority = authority;
    }

}
