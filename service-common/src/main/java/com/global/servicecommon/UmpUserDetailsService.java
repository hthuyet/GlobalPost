package com.global.servicecommon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.global.servicecommon.user.services.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ThuyetLV
 */
@Service("userDetailsService")
public class UmpUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        com.global.servicecommon.user.model.User user = userService.findByUserName(username);
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for(String roleName : user.roleNames){
            setAuths.add(new SimpleGrantedAuthority(roleName));
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(setAuths);
        return new User(user.userName, user.password, authorities);
    }
}
