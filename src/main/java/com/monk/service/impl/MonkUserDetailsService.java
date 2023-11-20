package com.monk.service.impl;

import com.monk.exception.RecordNotFoundException;
import com.monk.model.entity.MonkUser;
import com.monk.repository.MonkUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MonkUserDetailsService implements UserDetailsService {

    private final MonkUserRepository monkUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return monkUserRepository
                .findByEmail(email)
                .orElseThrow(RecordNotFoundException::new);
    }
}
