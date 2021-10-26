package org.peut.herdenk.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 80)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column
    private boolean enabled = true;

    @Column(nullable = false, length = 80)
    private String password;

    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "userId",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();



}
