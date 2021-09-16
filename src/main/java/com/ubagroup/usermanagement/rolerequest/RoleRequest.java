package com.ubagroup.usermanagement.rolerequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ubagroup.usermanagement.appuser.AppUser;
import com.ubagroup.usermanagement.role.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class RoleRequest {
    @SequenceGenerator(name="role_request_sequence", sequenceName = "role_request_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_request_sequence")
    @Column(name = "role_request_id")
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne
    private AppUser userId;

    private String email;

    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @OneToOne
    private Role role;

    private RoleRequestStatus roleRequestStatus = RoleRequestStatus.PENDING;

    @JsonIgnore
    @JoinColumn(name = "admin_officer_id", referencedColumnName = "id")
    @OneToOne
    private AppUser adminOfficer;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd h:m:s")
    private LocalDateTime requestActionTime;

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd h:m:s")
    private LocalDateTime timeCreated;

    @UpdateTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd h:m:s")
    private LocalDateTime timeUpdated;


    public RoleRequest(AppUser userId, String email, Role role, RoleRequestStatus roleRequestStatus) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.roleRequestStatus = roleRequestStatus;
    }
}
