/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author marko
 */
@Local
public interface SystemUserDAO {
    public void insert(SystemUserTO user, String groupName);
    public void update(SystemUserTO user);
    public SystemUserTO getById(long id);
    public SystemUserTO getByUsername(String username);
    public List<SystemUserTO> getAllUsers();
}
