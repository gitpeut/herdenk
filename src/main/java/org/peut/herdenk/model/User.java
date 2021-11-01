package org.peut.herdenk.model;

import org.peut.herdenk.model.dto.UserDto;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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

    @Column (nullable = false )
    private boolean enabled = true;

    // herdenk has only 2 roles: ADMIN_ROLE or not (=ROLE_USER)
    // But authentication will fail if this field is left blank
    @Column (nullable = false, length = 32 )
    private String role = "ROLE_USER";

    @Column(nullable = false, length = 80)
    private String password;

    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "userId",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Authority> authorities = new ArrayList<>();

    public static User from( UserDto userDto){
        User user = new User();

        user.setFullName( userDto.getFullName() );
        user.setEmail( userDto.getEmail());
        user.setPassword(userDto.getPassword());

        return user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
