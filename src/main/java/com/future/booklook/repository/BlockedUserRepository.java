package com.future.booklook.repository;

import com.future.booklook.model.entity.BlockedUser;
import com.future.booklook.model.entity.Market;
import com.future.booklook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, String> {
    BlockedUser findByUser(User user);

    Boolean existsByUser(User user);

    @Transactional
    void deleteByBlockedId(String BlockedId);

    @Query("select b from BlockedUser b")
    Set<BlockedUser> findAllBlockedUser();
}
