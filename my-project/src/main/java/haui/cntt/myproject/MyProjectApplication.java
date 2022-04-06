package haui.cntt.myproject;

import haui.cntt.myproject.common.enum_.RoleEnum;
import haui.cntt.myproject.persistance.entity.Role;
import haui.cntt.myproject.persistance.entity.User;
import haui.cntt.myproject.service.Impl.RoleServiceImpl;
import haui.cntt.myproject.service.Impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyProjectApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleServiceImpl roleService, UserServiceImpl userService) {
		return args -> {
			try {
				roleService.create(Role.builder().name(RoleEnum.ROLE_ADMIN).build());
				roleService.create(Role.builder().name(RoleEnum.ROLE_USER).build());
			} catch (Exception ignored) {
			}
			try {
				userService.createAdmin(User.builder()
						.username("admin")
						.password("123")
						.fullName("Ngô Văn Đang")
						.build());
				userService.createUser(User.builder()
						.username("dang")
						.password("123")
						.build());
			} catch (Exception ignored) {
			}
		};
	}
}
