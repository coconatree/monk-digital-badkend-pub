package com.monk.model.entity;

import com.monk.model.pojo.EMonkRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MonkUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long    id;

    @Column(unique = true, nullable = false)
    private String  email;

    private boolean isEmailValidated = false;
    private Date    emailValidatedAt;

    @Column(nullable = false)
    private String  password;

    private boolean isGoogleAccountConnected = false;
    private boolean isFacebookAccountConnected = false;
    private boolean isLinkedinAccountConnected = false;

    @Column(nullable = false)
    private Date createdAt;
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EMonkRole monkRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections
                .singleton(
                        new SimpleGrantedAuthority(
                                this.getMonkRole().name()
                        )
                );
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
