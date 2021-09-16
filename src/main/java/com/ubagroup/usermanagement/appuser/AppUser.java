package com.ubagroup.usermanagement.appuser;

import com.ubagroup.usermanagement.role.Role;
import com.ubagroup.usermanagement.role.RoleRepository;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Okala III
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class AppUser implements UserDetails {
    @SequenceGenerator(name="user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String phoneNo;

    private String merchantId;
    @Column(unique = true)
    private String finacleId;
    @Column(unique = true)
    private String accountNo;

    @JoinColumn(name = "appuser_role_id", referencedColumnName = "id")
    @OneToOne
    private Role appUserRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd h:m:s")
    private LocalDateTime timeCreated;

    @UpdateTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd h:m:s")
    private LocalDateTime timeUpdated;

    /**
     * External User (merchant) signUp
     * @param firstName
     * @param lastName
     * @param merchantId
     * @param email
     * @param password
     */
    public AppUser(String firstName, String lastName, String merchantId, String email, String password, Role appUserRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.merchantId = merchantId;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    /**
     * Internal User SignUp
     * @param finacleId
     * @param email

     */
    public AppUser(String finacleId, String email) {
        this.finacleId = finacleId;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(getAppUserRole().getName());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
