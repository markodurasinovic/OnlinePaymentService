package com.md459.onlinepaymentservice.ejb;

import com.md459.onlinepaymentservice.clients.ConversionClient;
import com.md459.onlinepaymentservice.dao.SystemUserDAO;
import com.md459.onlinepaymentservice.dto.SystemUserTO;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * A Stateless EJB implementation of the UserService interface.
 * It provides functionality regarding SystemUsers, including registration of
 * users and admins, and retrieval of users.
 */
@Stateless
@DeclareRoles({"USER", "ADMIN"})
public class UserServiceBean implements UserService {
    
    @EJB
    SystemUserDAO userDAO;
    
    @EJB
    ConversionClient conversion;
    
    public UserServiceBean() {}
    
    /**
     * Get DAOs for each SystemUser of SystemUserGroup USER.
     * 
     * @return - A list of SystemUser DAOs.
     */
    @Override
    @RolesAllowed("ADMIN")
    public List<SystemUserTO> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * Get a DAO for SystemUser with username.
     * 
     * @param username - A SystemUser's username.
     * @return - A list of SystemUser DAOs.
     */
    @Override
    @RolesAllowed({"USER", "ADMIN"})
    public SystemUserTO getUser(String username) {
        return userDAO.getByUsername(username);
    }
    
    /**
     * Register a USER if a SystemUser with username doesn't already exist.
     * 
     * @param username
     * @param password
     * @param name
     * @param surname
     * @param currency 
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerUser(String username, String password, String name, String surname, String currency) {
        if(hasUser(username))
            throw new EJBException("User with this username already exists.");
        
        float initialBalance = getInitialBalance(currency);
        SystemUserTO user = new SystemUserTO(
                username, getDigest(password), name, surname, initialBalance, currency);
        
        userDAO.insert(user, "USER");
    }
    
    private float getInitialBalance(String currency) throws EJBException {
        float balance;
        try {
            balance = conversion.getConvertedAmount("GBP", currency, 1000.00f);
        } catch(EJBException e) {
            throw new EJBException("Conversion service failed.");
        }
        return balance;
    }
    
    /**
     * Register an ADMIN if a SystemUser with username doesn't already exist.
     * 
     * @param username
     * @param password 
     */
    @Override
//    Limiting this function to only ADMIN users doesn't allow the 
//    startup EJB to register an admin with usrname: admin1; password: admin1
//    on startup.
//    
//    @RolesAllowed("ADMIN")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerAdmin(String username, String password) {
        if(hasUser(username)) 
            throw new EJBException("Admin with this username already exists.");
        
        SystemUserTO admin = new SystemUserTO(username, getDigest(password));
        
        userDAO.insert(admin, "ADMIN");
    }
    
    @Override
    public boolean hasUser(String username) {
        return userDAO.getByUsername(username) != null;            
    }
    
    private String getDigest(String password) {
        String pwd = password;
        try {   
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            pwd = sb.toString();
        } catch(UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pwd;
    }
}
