package com.raj.security.service;

import com.raj.dao.UserDAO;
import com.raj.model.bean.User;
import com.raj.security.userdetails.DAOUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DAOUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;

    @Autowired
    public DAOUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {

        Optional<User> userO = userDAO.getUserByUserId(username);

        if (userO.isEmpty()) {
            throw new UsernameNotFoundException("username not found.");
        }

        User user = userO.get();

        return new DAOUserDetails(
            user.getUserId(),
            user.getPassword(),
            AuthorityUtils.createAuthorityList("ROLE_" + user.getRole()));
    }
}
