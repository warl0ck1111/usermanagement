package com.ubagroup.usermanagement.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Okala III
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @SequenceGenerator(name = "app_user_role_sequence", sequenceName = "app_user_role_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_role_sequence")
    private Long id;

    @Column(unique = true)
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
