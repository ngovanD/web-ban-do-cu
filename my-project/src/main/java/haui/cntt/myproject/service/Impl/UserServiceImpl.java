package haui.cntt.myproject.service.Impl;

import com.github.slugify.Slugify;

import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.common.myEnum.RoleEnum;
import haui.cntt.myproject.persistance.entity.Product;
import haui.cntt.myproject.persistance.entity.Role;
import haui.cntt.myproject.persistance.entity.User;
import haui.cntt.myproject.persistance.repository.ProductRepository;
import haui.cntt.myproject.persistance.repository.RoleRepository;
import haui.cntt.myproject.persistance.repository.UserRepository;
import haui.cntt.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ProductRepository productRepository;

    @Transactional
    public void createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles = roleRepository.findAll();
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(RoleEnum.ROLE_USER).orElse(null);
        user.setRoles(Collections.singleton(role));
        user.setAvatar("avatar_default.png");
        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email, long userId) throws Throwable {
        if (userId > 0) {
            User foundUser = userRepository.findById(userId).orElseThrow(
                    () -> {
                        throw new BadRequestException("User kh??ng t???n t???i !!!");
                    }
            );
            if (email.equals(foundUser.getEmail())) {
                return false;
            } else {
                return userRepository.existsByEmail(email);
            }
        }
        return userRepository.existsByEmail(email);
    }

    public boolean existsByCellphone(String cellphone, long userId) throws Throwable {
        if (userId > 0) {
            User foundUser = userRepository.findById(userId).orElseThrow(
                    () -> {
                        throw new BadRequestException("User kh??ng t???n t???i !!!");
                    }
            );
            if (cellphone.equals(foundUser.getCellphone())) {
                return false;
            } else {
                return userRepository.existsByCellphone(cellphone);
            }
        }
        return userRepository.existsByCellphone(cellphone);
    }

    public User getCurrentUser() throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(
                () -> {
                    throw new BadRequestException("User kh??ng t???n t???i !!!");
                }
        );
    }

    public Page<User> getAllUser(int page, int size, String keyword) {
        String keywordFormat = keyword.trim().toLowerCase();
        Pageable pageable = PageRequest.of(page, size);
        if (keywordFormat.equals("")) {
            return userRepository.findAllUser(pageable);
        } else {
            return userRepository.findAllUserAndSearch(pageable, keywordFormat);
        }
    }

    @Transactional
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(long userId) throws Throwable {
        return userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new BadRequestException("User kh??ng t???n t???i !!!");
                }
        );
    }

    @Transactional
    public void editUser(User user, boolean resetPassword) throws Throwable {
        User foundUser = userRepository.findById(user.getId()).orElseThrow(
                () -> {
                    throw new BadRequestException("User kh??ng t???n t???i !!!");
                }
        );
        if (resetPassword) {
            foundUser.setPassword(passwordEncoder.encode(foundUser.getUsername()));
        }
        foundUser.setFullName(user.getFullName());
        foundUser.setCellphone(user.getCellphone());
        foundUser.setEmail(user.getEmail());
        foundUser.setHiddenFlag(user.getHiddenFlag());
        userRepository.save(foundUser);
    }

    @Transactional
    public String updateAvatar(long userId, String originalFilename) throws Throwable {
        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new BadRequestException("User kh??ng t???n t???i !!!");
                }
        );
        String typeOfFile = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = new Slugify().slugify(foundUser.getUsername() + LocalDateTime.now());
        foundUser.setAvatar(newFileName + typeOfFile);
        userRepository.save(foundUser);
        return newFileName + typeOfFile;
    }

    @Transactional
    public void changeInfo(User convertToUser) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByUsername(username).orElseThrow(
                () -> {
                    throw new BadRequestException("Kh??ng t??m th???y user !!!");
                }
        );
        foundUser.setFullName(convertToUser.getFullName());
        foundUser.setEmail(convertToUser.getEmail());
        userRepository.save(foundUser);
    }

    @Transactional
    public void changPassword(String password) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByUsername(username).orElseThrow(
                () -> {
                    throw new BadRequestException("User kh??ng t???n t???i !!!");
                }
        );
        String newPassword = passwordEncoder.encode(password);
        foundUser.setPassword(newPassword);
        userRepository.save(foundUser);
    }

    @Transactional
    public String changPasswordForUserForgetPassword(String email) throws Throwable {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String newPassword = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println(newPassword);
        User foundUser = userRepository.findByEmail(email).orElseThrow(
                () -> {
                    throw new BadRequestException("Kh??ng c?? user n??o ??ang s??? d???ng email n??y !!!");
                }
        );
        foundUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(foundUser);

        return newPassword;
    }

    public Page<Product> getMyListProduct(int page, String status) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByUsername(username).orElseThrow(
                () -> {
                    throw new BadRequestException("User kh??ng t???n t???i !!!");
                }
        );
        List<Product> productList = new ArrayList<>();
        if (status.equals("all")) {
            productList = foundUser.getProducts().stream().collect(Collectors.toList());
        } else {
            productList = foundUser.getProducts().stream().filter(p -> p.getStatus().toString().equals(status)).collect(Collectors.toList());
        }

        Collections.sort(productList, Comparator.comparingLong(Product::getId).reversed());
        Pageable pageable = PageRequest.of(page, 10);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), productList.size());
        return new PageImpl<>(productList.subList(start, end), pageable, productList.size());
    }

    @Transactional
    public void addWishlist(long productId) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new BadRequestException("User kh??ng t???n t???i !!!");
        });
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> {
            throw new BadRequestException("S???n ph???m kh??ng t???n t???i !!!");
        });
        if (foundUser.getWishlistProducts().contains(foundProduct)) {
            throw new BadRequestException("B???n ???? th??m s???n ph???m n??y v??o danh s??ch y??u th??ch r???i !!!");
        } else {
            foundUser.getWishlistProducts().add(foundProduct);
            userRepository.save(foundUser);
        }
    }

    public Page<Product> getWishlist(int page) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new BadRequestException("User kh??ng t???n t???i !!!");
        });
        List<Product> productList = foundUser.getWishlistProducts()
                .stream()
                .collect(Collectors.toList());
        Collections.sort(productList, Comparator.comparingLong(Product::getId).reversed());
        Pageable pageable = PageRequest.of(page, 10);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), productList.size());
        return new PageImpl<>(productList.subList(start, end), pageable, productList.size());
    }

    @Transactional
    public void removeWishlist(long productId) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> {
            throw new BadRequestException("User kh??ng t???n t???i !!!");
        });
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> {
            throw new BadRequestException("S???n ph???m kh??ng t???n t???i !!!");
        });
        if (foundUser.getWishlistProducts().contains(foundProduct)) {
            foundUser.getWishlistProducts().remove(foundProduct);
            userRepository.save(foundUser);
        } else {
            throw new BadRequestException("Kh??ng c?? s???n ph???m n??y trong danh s??ch y??u th??ch !!!");
        }
    }

    public int getNewUser(String from, String to) {
        return userRepository.getNewUser(from, to);
    }

    @Override
    public User getUserByUsername(String username) throws Throwable{
        return userRepository.findByUsername(username).orElseThrow(
                ()->{throw new BadRequestException("Kh??ng t??m th???y user !!!");}
        );
    }
}
