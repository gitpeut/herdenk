package org.peut.herdenk.model;

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

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(nullable = false, length = 2048)
    private String text;

    @Column(nullable = false, length = 512)
    private String mediaPath;

}
