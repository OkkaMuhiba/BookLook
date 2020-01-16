package com.future.booklook.model.entity;

import com.future.booklook.model.constants.BlockedUserConstant;
import com.future.booklook.model.constants.UserConstant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = BlockedUserConstant.TABLE_NAME)
public class BlockedUser {
    @Id
    @Column(name = BlockedUserConstant.BLOCKED_ID)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String blockedId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = BlockedUserConstant.USER_FK, referencedColumnName = UserConstant.USER_ID)
    private User user;

    @Column(name = BlockedUserConstant.START_AT)
    @CreationTimestamp
    private Timestamp startAt;

    @Column(name = BlockedUserConstant.END_AT)
    private Timestamp endAt;

    public BlockedUser(User user, Timestamp endAt) {
        this.user = user;
        this.endAt = endAt;
    }

    public BlockedUser() {
    }

    public String getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(String blockedId) {
        this.blockedId = blockedId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getStartAt() {
        return startAt;
    }

    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
    }

    public Timestamp getEndAt() {
        return endAt;
    }

    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
    }
}
