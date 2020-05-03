package com.md459.onlinepaymentservice.dao;

import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SystemUserDAO {
    public void insert(SystemUserTO user, String groupName);
    public void update(SystemUserTO user);
    public SystemUserTO getById(long id);
    public SystemUserTO getByUsername(String username);
    public List<SystemUserTO> getAllUsers();
}
