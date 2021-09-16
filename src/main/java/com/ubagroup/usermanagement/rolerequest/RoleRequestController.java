package com.ubagroup.usermanagement.rolerequest;

import com.ubagroup.usermanagement.appuser.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/role")
public class RoleRequestController {

    @Autowired
    private RoleRequestService roleRequestService;

    @PostMapping(path = "request/{roleId}")
    public ResponseEntity<?> requestRole(@PathVariable(name = "roleId") Long roleId, Authentication authentication){
        AppUser appUser = (AppUser) authentication.getPrincipal();
        RoleRequest roleRequest = roleRequestService.requestRole(appUser, roleId);
        return ResponseEntity.ok(roleRequest);

    }

    @PutMapping(path = "request/approve/{requestId}")
    public ResponseEntity<?> approveRole(@PathVariable(name = "requestId") Long requestId, Authentication authentication){
        AppUser appUser = (AppUser) authentication.getPrincipal();
        RoleRequest roleRequest = roleRequestService.approveRoleRequest(appUser, requestId);
        return ResponseEntity.ok(roleRequest);

    }

    @PutMapping(path = "request/reject/{requestId}")
    public ResponseEntity<?> rejectRole(@PathVariable(name = "requestId") Long requestId, Authentication authentication){
        AppUser appUser = (AppUser) authentication.getPrincipal();
        RoleRequest roleRequest = roleRequestService.rejectRoleRequest(appUser, requestId);
        return ResponseEntity.ok(roleRequest);

    }

    @GetMapping(path = "request/all")
    public ResponseEntity<?> findAllRoleRequests(){
        return ResponseEntity.ok(roleRequestService.getAllRoleRequest());
    }
}
