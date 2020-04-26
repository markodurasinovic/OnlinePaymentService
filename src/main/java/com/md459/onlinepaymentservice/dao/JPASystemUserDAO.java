/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.entity.SystemUser;
import com.md459.onlinepaymentservice.entity.SystemUserGroup;
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
    public long insert(SystemUser user, SystemUserGroup group) {
        group.addUser(user);
        em.persist(group);
        
        return user.getId();
    }

    @Override
    public SystemUser getById(long id) {
        TypedQuery<SystemUser> query = em.createQuery(
                "SELECT u FROM SystemUser u WHERE u.id = :id", SystemUser.class);
        
        List<SystemUser> result = query
                .setParameter("id", id)
                .getResultList();
        
        if(result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public SystemUser getByUsername(String username) {
        TypedQuery<SystemUser> query = em.createQuery(
                "SELECT u FROM SystemUser u WHERE u.username = :username", SystemUser.class);
        
        List<SystemUser> result = query
                .setParameter("username", username)
                .getResultList();
        
        if(result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public boolean update(SystemUser user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<SystemUser> getAllUsers() {
        TypedQuery<SystemUser> query = em.createQuery(
            "SELECT u FROM SystemUser u", SystemUser.class);
        
        return query.getResultList();
    }
}
