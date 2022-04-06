package haui.cntt.myproject.service.Impl;

import com.github.slugify.Slugify;
import haui.cntt.myproject.common.enum_.RoleEnum;
import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.persistance.entity.Role;
import haui.cntt.myproject.persistance.entity.User;
import haui.cntt.myproject.persistance.repository.RoleRepository;
import haui.cntt.myproject.persistance.repository.UserRepository;
import haui.cntt.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles = roleRepository.findAll();
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(RoleEnum.ROLE_USER).orElse(null);
        user.setRoles(Collections.singleton(role));
        user.setAvatar("avatar_default.png");
        userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByCellphone(String cellphone) {
        return userRepository.existsByCellphone(cellphone);
    }
    @Override
    public User getCurrentUser() throws Throwable{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(
                () ->{throw new BadRequestException("User không tồn tại !!!");}
        );
    }

    public Page<User> getAllUser(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(long userId) throws Throwable{
        return userRepository.findById(userId).orElseThrow(
                ()->{throw new BadRequestException("User không tồn tại !!!");}
        );
    }

    @Transactional
    public void editUser(User user) throws Throwable{
        User foundUser = userRepository.findById(user.getId()).orElseThrow(
                ()->{throw new BadRequestException("User không tồn tại !!!");}
        );

        foundUser.setFullName(user.getFullName());
        foundUser.setCellphone(user.getCellphone());
        foundUser.setEmail(user.getEmail());

        userRepository.save(foundUser);
    }

    @Transactional
    public String updateAvatar(long userId, String originalFilename) throws Throwable{
        User foundUser = userRepository.findById(userId).orElseThrow(
                ()->{throw new BadRequestException("User không tồn tại !!!");}
        );

        String typeOfFile = originalFilename.substring(originalFilename.lastIndexOf("."));

        String newFileName = new Slugify().slugify(foundUser.getUsername() + LocalDateTime.now());
        foundUser.setAvatar(newFileName + typeOfFile);

        userRepository.save(foundUser);

        return newFileName+ typeOfFile;
    }
}
