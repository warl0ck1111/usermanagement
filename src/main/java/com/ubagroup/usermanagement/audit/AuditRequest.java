package com.ubagroup.usermanagement.audit;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ubagroup.usermanagement.appuser.AppUser;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class AuditRequest {

    private Long id;
    private AppUser actor;
    private String action;
    private String rawDump;
}
