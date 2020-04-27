/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.dto.SystemUserTO;
import com.md459.onlinepaymentservice.entity.SystemUser;
import com.md459.onlinepaymentservice.entity.SystemUserGroup;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author marko
 */
@Stateless
public class JPASystemUserDAO implements SystemUserDAO {
    
    @PersistenceContext
    EntityManager em;

    @Override
    public void insert(SystemUserTO user, String groupName) {
        SystemUser userEntity;
        if(groupName.equals("ADMIN")) {
            userEntity = new SystemUser(user.username, user.userpassword);
        } else {
            userEntity = new SystemUser(
                user.username, user.userpassword, user.name, user.surname, user.balance, user.currency);
        }
        
        SystemUserGroup group = new SystemUserGroup(user.username, groupName);
        
        em.persist(userEntity);
        em.persist(group);
    }
    
    @Override
    public void update(SystemUserTO user) {
        SystemUser userEntity = em.find(SystemUser.class, user.id);
        userEntity.setUserData(user);
    }

    @Override
    public SystemUserTO getById(long id) {
        SystemUser user = em.find(SystemUser.class, id);
        
        return user.getUserData();
    }

    @Override
    public SystemUserTO getByUsername(String username) {
        TypedQuery<SystemUser> query = em.createQuery(
                "SELECT u FROM SystemUser u WHERE u.username = :username", SystemUser.class);
        
        List<SystemUser> users = query
                .setParameter("username", username)
                .getResultList();
        
        return getUserData(users);
    }
    
    private SystemUserTO getUserData(List<SystemUser> users) {
        if(users.isEmpty()) {
            return null;
        } else {
            return users.get(0).getUserData();
        }
    }
    
    @Override
    public List<SystemUserTO> getAllUsers() {
        TypedQuery<SystemUser> query = em.createQuery(
            "SELECT u FROM SystemUser u", SystemUser.class);
        
        List<SystemUser> users = query.getResultList();
        
        List<SystemUserTO> userTOs = new ArrayList<>();
        users.forEach((u) -> {
            userTOs.add(u.getUserData());
        });
        
        return userTOs;
    }
}
