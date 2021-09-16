package com.ubagroup.usermanagement.rolerequest;

import com.ubagroup.usermanagement.appuser.AppUser;
import com.ubagroup.usermanagement.exception.ApiRequestException;
import com.ubagroup.usermanagement.role.Role;
import com.ubagroup.usermanagement.role.RoleNotFoundException;
import com.ubagroup.usermanagement.role.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
public class RoleRequestService {

    @Autowired
    private RoleRequestRepository roleRequestRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public RoleRequest requestRole(AppUser appUser, Long roleId){
        Role role = roleRepository.findById(roleId).orElseThrow(() -> {

            log.error("invalid role");
            return new RoleNotFoundException("invalid role");
        });
        RoleRequest roleRequest = new RoleRequest(appUser, appUser.getEmail(), role,RoleRequestStatus.PENDING);
        return roleRequestRepository.save(roleRequest);

    }

    @Transactional
    public RoleRequest approveRoleRequest(AppUser approvingOfficer, Long requestId){

        if (!approvingOfficer.getAppUserRole().getName().equalsIgnoreCase("USER ADMIN")){
            log.error("user unauthorized");
            throw new ApiRequestException("user unauthorized");
        }
        RoleRequest roleRequest = roleRequestRepository.findById(requestId).orElseThrow(() -> {
            log.error("invalid request");
            return new ApiRequestException("invalid request");
        });
        roleRequest.setAdminOfficer(approvingOfficer);
        roleRequest.setRequestActionTime(LocalDateTime.now());
        roleRequest.getUserId().setAppUserRole(roleRequest.getRole());
        roleRequest.setRoleRequestStatus(RoleRequestStatus.APPROVED);
        return roleRequestRepository.save(roleRequest);
    }

    @Transactional
    public RoleRequest rejectRoleRequest(AppUser approvingOfficer, Long requestId){
        RoleRequest roleRequest = roleRequestRepository.findById(requestId).orElseThrow(() -> {
            log.error("invalid request");
            return new ApiRequestException("invalid request");
        });
        roleRequest.setAdminOfficer(approvingOfficer);
        roleRequest.setRequestActionTime(LocalDateTime.now());
        roleRequest.setRoleRequestStatus(RoleRequestStatus.REJECTED);
        return roleRequestRepository.save(roleRequest);

    }

    public List<RoleRequest> getAllRoleRequest(){
        return roleRequestRepository.findAll();
    }
}
