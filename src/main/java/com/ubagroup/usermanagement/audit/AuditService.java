package com.ubagroup.usermanagement.audit;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;


    public void logAudit(AuditRequest auditRequest) {
        Audit audit = new Audit();
        BeanUtils.copyProperties(auditRequest,audit );
        auditRepository.save(audit);
    }
}
