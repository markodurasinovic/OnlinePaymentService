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
 * JPA implementation of SystemUserDAO.
 */
@Stateless
public class JPASystemUserDAO implements SystemUserDAO {
    
    @PersistenceContext
    EntityManager em;

    /**
     * Insert a new SystemUser entity into the DB based on the DTO values.
     * A SystemUserGroup entity is created for the user based on groupName.
     * This user group entity is only used as a mapping for jdbcRealm security.
     * Only the group is persisted due to the CascadeType.ALL relationship
     * between SystemUserGroup and SystemUser.
     * 
     * @param user - A SystemUser DTO.
     * @param groupName - A String denoting the user's security group.
     */
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
        group.addUser(userEntity);
        
        em.persist(group);
    }
    
    /**
     * Update an existing SystemUser entity based on the DTO values.
     * 
     * @param user - A SystemUser DTO.
     */
    @Override
    public void update(SystemUserTO user) {
        SystemUser userEntity = em.find(SystemUser.class, user.id);
        userEntity.setUserData(user);
    }

    /**
     * Get a DTO for the SystemUser entity corresponding to id.
     * 
     * @param id - A SystemUser id.
     * @return - A SystemUser DTO.
     */
    @Override
    public SystemUserTO getById(long id) {
        SystemUser user = em.find(SystemUser.class, id);
        
        return user.getUserData();
    }

    /**
     * Get a DTO for the SystemUser entity corresponding to username.
     * 
     * @param username - A SystemUser username.
     * @return - A SystemUser DTO.
     */
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
    
    /**
     * Get DTOs for all SystemUser entities in the DB.
     * 
     * @return - A list of SystemUser DTOs.
     */
    @Override
    public List<SystemUserTO> getAllUsers() {
        TypedQuery<SystemUser> query = em.createQuery(
            "SELECT u FROM SystemUser u WHERE u.usergroup.groupname = :groupname", SystemUser.class);
        
        List<SystemUser> users = query
                .setParameter("groupname", "USER")
                .getResultList();
        
        List<SystemUserTO> userTOs = new ArrayList<>();
        users.forEach((u) -> {
            userTOs.add(u.getUserData());
        });
        
        return userTOs;
    }
}
