package haui.cntt.myproject.config;

import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.persistance.entity.User;
import haui.cntt.myproject.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailServiceConfig implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username).orElse(null);
        if (Objects.isNull(userEntity)) {
            throw new BadRequestException(username + " not found in database.");
        } else {
//            Collection<SimpleGrantedAuthority> authorities = userEntity.getRoles().stream().map(roleEntity ->
//                    new SimpleGrantedAuthority(roleEntity.getName().toString())).collect(Collectors.toList());
//            return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
            return new CustomUserDetails(userEntity);
        }
    }
}
