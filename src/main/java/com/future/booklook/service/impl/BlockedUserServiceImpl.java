package com.future.booklook.service.impl;

import com.future.booklook.model.entity.BlockedUser;
import com.future.booklook.model.entity.User;
import com.future.booklook.repository.BlockedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BlockedUserServiceImpl {
    @Autowired
    private BlockedUserRepository blockedUserRepository;

    public BlockedUser saveBlockedUser(BlockedUser blockedUser){
        return blockedUserRepository.save(blockedUser);
    }

    public void removeBlockedUser(BlockedUser blockedUser){
        blockedUserRepository.deleteByBlockedId(blockedUser.getBlockedId());
    }

    public BlockedUser findBlockedUserByUser(User user){
        return blockedUserRepository.findByUser(user);
    }

    public Set<BlockedUser> findAllBlockedMarket(){
        return blockedUserRepository.findAllBlockedUser();
    }

    public Boolean userAlreadyBlockedBefore(User user){
        return blockedUserRepository.existsByUser(user);
    }
}
