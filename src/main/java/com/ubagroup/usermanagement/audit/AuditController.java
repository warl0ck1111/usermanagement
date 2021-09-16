package com.ubagroup.usermanagement.audit;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PostMapping(path = "log")
    public ResponseEntity<?> logAudit(@RequestBody AuditRequest auditRequest){
        auditService.logAudit(auditRequest);
        return ResponseEntity.ok("Success");
    }
}
