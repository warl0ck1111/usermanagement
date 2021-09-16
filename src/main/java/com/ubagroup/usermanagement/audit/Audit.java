package com.ubagroup.usermanagement.audit;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ubagroup.usermanagement.appuser.AppUser;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit")
public class Audit {

    @SequenceGenerator(name="audit_sequence", sequenceName = "audit_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_sequence")
    private Long id;

    @JsonBackReference
    @JoinColumn(referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AppUser actor;


    private String email;
    private String description;
    private String actionResult;
    private String actionItem;
    private String otherItem;
    private String event;


    @Column(nullable = false, length = 500)
    private String action;


    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd h:m:s")
    private LocalDateTime actionDate;


}
