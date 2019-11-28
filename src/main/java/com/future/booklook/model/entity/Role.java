package com.future.booklook.model.entity;

import com.future.booklook.model.constants.RoleConstant;
import com.future.booklook.model.entity.properties.RoleName;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = RoleConstant.TABLE_NAME)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = RoleConstant.ROLE_ID)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = RoleConstant.NAME)
    private RoleName name;

    public Role() {

    }

    public Role(RoleName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}
