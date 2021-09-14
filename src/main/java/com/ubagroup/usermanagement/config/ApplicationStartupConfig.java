package com.ubagroup.usermanagement.config;

import com.ubagroup.usermanagement.appuser.AppUserRepository;
import com.ubagroup.usermanagement.appuser.AppUserRole;
import com.ubagroup.usermanagement.role.Role;
import com.ubagroup.usermanagement.role.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ApplicationStartupConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RoleService roleService;


//    @Value("${databaseAlreadySetup}")
    private boolean databaseAlreadyConfigured;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("onApplicationEvent");
        List<Role> roleList = getRoles();
        for (Role r : roleList){
            System.out.println(r.getName());
        }
        if (roleList.size() != 0) databaseAlreadyConfigured = true;

        if (!databaseAlreadyConfigured) {
            log.info("database not configured");
            List<Role> roles = new ArrayList<>();
            for (AppUserRole role : AppUserRole.values()) {
                roles.add(new Role(role.name()));
            }
            roleService.getRoleRepository().saveAll(roles);
        }else
            log.info("database is configured");


    }


    private Role addRole(String roleName) {
        return roleService.saveRole(roleName);
    }

    public List<Role> getRoles() {
        return roleService.findAll();

    }


}
