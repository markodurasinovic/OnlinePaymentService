package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Local;

@Local
public interface UserService {
    
    public List<SystemUserTO> getAllUsers();
    public boolean hasUser(String username);
    public SystemUserTO getUser(String username);
    public void registerUser(String username, String password, String name, String surname, String currency) throws EJBException;
    public void registerAdmin(String username, String password) throws EJBException;
}
