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

    @Transactional
    public void createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles = roleRepository.findAll();
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional
    public void createUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(RoleEnum.ROLE_USER).orElse(null);
        user.setRoles(Collections.singleton(role));
        user.setAvatar("avatar_default.png");
        userRepository.save(user);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email, long userId) throws Throwable{
        if(userId > 0)
        {
            User foundUser = userRepository.findById(userId).orElseThrow(
                    () ->{throw new BadRequestException("User không tồn tại !!!");}
            );

            if(email.equals(foundUser.getEmail())){
                return false;
            }
            else{
                return userRepository.existsByEmail(email);
            }
        }
        return userRepository.existsByEmail(email);
    }

    public boolean existsByCellphone(String cellphone, long userId) throws Throwable{
        if(userId > 0)
        {
            User foundUser = userRepository.findById(userId).orElseThrow(
                    () ->{throw new BadRequestException("User không tồn tại !!!");}
            );

            if(cellphone.equals(foundUser.getCellphone())){
                return false;
            }
            else{
                return userRepository.existsByCellphone(cellphone);
            }
        }
        return userRepository.existsByCellphone(cellphone);
    }

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
    public void editUser(User user, boolean resetPassword) throws Throwable{
        User foundUser = userRepository.findById(user.getId()).orElseThrow(
                ()->{throw new BadRequestException("User không tồn tại !!!");}
        );

        if(resetPassword)
        {
            foundUser.setPassword(passwordEncoder.encode(foundUser.getUsername()));
        }

        foundUser.setFullName(user.getFullName());
        foundUser.setCellphone(user.getCellphone());
        foundUser.setEmail(user.getEmail());
        foundUser.setHiddenFlag(user.getHiddenFlag());

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

        return newFileName + typeOfFile;
    }

    @Transactional
    public void changeInfo(User convertToUser) throws Throwable{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByUsername(username).orElseThrow(
                ()->{throw new BadRequestException("Không tìm thấy user !!!");}
        );

        foundUser.setFullName(convertToUser.getFullName());
        foundUser.setEmail(convertToUser.getEmail());

        userRepository.save(foundUser);
    }
}
