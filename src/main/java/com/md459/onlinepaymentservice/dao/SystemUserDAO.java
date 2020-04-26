/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.entity.SystemUser;
import com.md459.onlinepaymentservice.entity.SystemUserGroup;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author marko
 */
@Local
public interface SystemUserDAO {
    public long insert(SystemUser user, SystemUserGroup group);
    public SystemUser getById(long id);
    public SystemUser getByUsername(String username);
    public boolean update(SystemUser user);
    public List<SystemUser> getAllUsers();
}
