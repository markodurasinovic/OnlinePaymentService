/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.md459.onlinepaymentservice.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author marko
 */
public class SystemUserGroupTO implements Serializable {
    public Long id;
    public String groupname;
    public List<SystemUserTO> users;
    public String username;
    
    public SystemUserGroupTO() {}
}
