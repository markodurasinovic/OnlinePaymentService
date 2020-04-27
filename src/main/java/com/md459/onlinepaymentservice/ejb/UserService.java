/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Local;

/**
 *
 * @author marko
 */
@Local
public interface UserService {
    
    public List<SystemUserTO> getAllUsers();
    public boolean hasUser(String username);
    public SystemUserTO getUser(String username);
    public void registerUser(String username, String password, String name, String surname, String currency) throws EJBException;
    public void registerAdmin(String username, String password) throws EJBException;
}
