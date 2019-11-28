package com.future.booklook.repository;

import com.future.booklook.model.entity.Role;
import com.future.booklook.model.entity.properties.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName roleName);

}
