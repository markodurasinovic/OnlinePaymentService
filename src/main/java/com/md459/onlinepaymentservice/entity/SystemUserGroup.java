package com.md459.onlinepaymentservice.entity;

import com.md459.onlinepaymentservice.dto.SystemUserGroupTO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class SystemUserGroup implements Serializable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @NotNull
    private String groupname;
    
    @OneToMany(mappedBy = "usergroup", cascade = CascadeType.ALL)
    private List<SystemUser> users;
    private String username;
    
    public SystemUserGroup() {}
    
    public SystemUserGroup(String username, String groupname) {
        this.username = username;
        this.groupname = groupname;
    }
    
    public SystemUserGroupTO getGroupData() {
        return createSystemUserGroupTO();
    }
    
    private SystemUserGroupTO createSystemUserGroupTO() {
        SystemUserGroupTO userGroup = new SystemUserGroupTO();
        userGroup.id = id;
        userGroup.groupname = groupname;
        userGroup.users = getUserTOs();
        userGroup.username = username;
        
        return userGroup;
    }
    
    private List<SystemUserTO> getUserTOs() {
        List<SystemUserTO> userTOs = new ArrayList<>();
        users.forEach((u) -> {
            userTOs.add(u.getUserData());
        });
        
        return userTOs;
    }
    
    public void setGroupData(SystemUserGroupTO groupTO) {
        groupname = groupTO.groupname;
        users = updateUsers(groupTO.users);
        username = groupTO.username;
    }
    
    private List<SystemUser> updateUsers(List<SystemUserTO> userTOs) {
        List<SystemUser> updatedUsers = new ArrayList<>();
        userTOs.forEach((u) -> {
            SystemUser updatedUser = new SystemUser();
            updatedUser.setUserData(u);
            updatedUsers.add(updatedUser);
        });
        
        return updatedUsers;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.groupname);
        hash = 41 * hash + Objects.hashCode(this.users);
        hash = 41 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SystemUserGroup other = (SystemUserGroup) obj;
        if (!Objects.equals(this.groupname, other.groupname)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.users, other.users)) {
            return false;
        }
        return true;
    }

   public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public List<SystemUser> getUsers() {
        return users;
    }

    public void setUsers(List<SystemUser> users) {
        this.users = users;
    }
    
    public void addUser(SystemUser user) {
        if(users == null) users = new ArrayList<>();
        
        user.setUsergroup(this);
        users.add(user);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
