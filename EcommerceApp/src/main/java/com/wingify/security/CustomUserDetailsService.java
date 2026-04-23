package com.wingify.security;
import com.wingify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    @Override public UserDetails loadUserByUsername(String email) {
        var u = users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new User(u.getEmail(), u.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name())));
    }
}
