package com.tdt.jwt.service;

import com.tdt.jwt.dao.UserAuthDAO;
import com.tdt.jwt.model.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author
 **/
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService, UserAuthService {

    @Autowired
    UserAuthDAO userAuthDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAuth user = userAuthDAO.findOne(username);
        return user != null ?
                new User(username, user.getPassword(), Arrays.asList(new GrantedAuthority[]{new SimpleGrantedAuthority("USER")}))
                : null;
    }

    @Override
    public UserAuth findById(String id) {
        return userAuthDAO.findOne(id);
    }
}
