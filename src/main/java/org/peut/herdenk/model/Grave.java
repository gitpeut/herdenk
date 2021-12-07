package org.peut.herdenk.model;

import org.hibernate.annotations.CreationTimestamp;
import org.peut.herdenk.model.dto.GraveDto;
import org.peut.herdenk.model.dto.GraveRegisterDto;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "graves")
public class Grave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long graveId;

    @Column(nullable = false, length = 80)
    private String occupantFullName;

    @Column
    @CreationTimestamp
    private Date creationDate;


    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "graveId",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Authority> authorities = new ArrayList<>();

    @OneToMany(
            targetEntity = Reaction.class,
            mappedBy = "graveId",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderBy( "reactionId" )
    private List<Reaction> reactions = new ArrayList<>();

    public static Grave from( GraveDto graveDto){
        Grave grave = new Grave();

        grave.setOccupantFullName( graveDto.getOccupantFullName());
        return grave;
    }

    public static Grave from( GraveRegisterDto graveRegisterDto){
        Grave grave = new Grave();

        grave.setOccupantFullName( graveRegisterDto.getOccupantFullName());
        return grave;
    }


    public Long getGraveId() {
        return graveId;
    }

    public void setGraveId(Long graveId) {
        this.graveId = graveId;
    }

    public String getOccupantFullName() {
        return occupantFullName;
    }

    public void setOccupantFullName(String occupantFullName) {
        this.occupantFullName = occupantFullName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }
}