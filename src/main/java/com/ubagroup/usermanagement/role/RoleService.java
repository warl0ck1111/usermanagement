package com.ubagroup.usermanagement.role;

import com.ubagroup.usermanagement.exception.ApiRequestException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Okala III
 */
@Slf4j
@Getter
@Service
public class RoleService {
    @Autowired
    private  RoleRepository roleRepository;

    @Transactional
    public Role saveRole(String roleName){
        boolean isRoleExist = roleRepository.existsByName(roleName);
        if(isRoleExist){
            log.error("Role already exists");
            throw new ApiRequestException("Role already exists");
        }
        Role role = new Role(roleName);
        return roleRepository.save(role);

    }

    public List<Role> findAll(){
        List<Role> roleList = new ArrayList<>();
        List<Role> roles = roleRepository.findAll();
        for(Role role : roles){
            String roleName = role.getName().replace("_", " ");
            roleList.add(new Role(role.getId(),roleName));
        }
        return roleList;
    }
}
