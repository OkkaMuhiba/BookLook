package com.future.booklook.repository;

import com.future.booklook.model.entity.Role;
import com.future.booklook.model.entity.Transaction;
import com.future.booklook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUserId(String userId);

    User findByUserId(String userId);

    User findByTransactions(Set<Transaction> transactions);

    Boolean existsByUserIdAndReadKey(String userId, String readKey);

    User findByUserIdAndReadKey(String userId, String readKey);

    Set<User> findByRoles(Set<Role> roles);

    @Transactional
    @Modifying
    @Query(value = "delete from user_roles where user_id = :userId and role_id = :roleId", nativeQuery = true)
    void deleteUserRolesByUserIdAndRoleId(@Param("userId") String userId, @Param("roleId") Long roleId);
}
