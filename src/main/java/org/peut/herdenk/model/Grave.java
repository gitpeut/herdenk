package org.peut.herdenk.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "graves")
public class Grave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long graveId;

    @Column(nullable = false, length = 80)
    private String occupantFullName;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column
    private boolean publicRead = true;


    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "graveId",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(
            targetEntity = Reaction.class,
            mappedBy = "graveId",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Reaction> reactions = new HashSet<>();
}