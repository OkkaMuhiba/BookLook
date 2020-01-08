package com.future.booklook.service.impl;

import com.future.booklook.model.entity.Role;
import com.future.booklook.model.entity.properties.RoleName;
import com.future.booklook.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl {
    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByRoleName(RoleName roleName){
        return roleRepository.findByName(roleName);
    }
}
