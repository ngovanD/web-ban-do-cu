package haui.cntt.myproject;

import haui.cntt.myproject.common.enum_.RoleEnum;
import haui.cntt.myproject.persistance.entity.Category;
import haui.cntt.myproject.persistance.entity.Property;
import haui.cntt.myproject.persistance.entity.Role;
import haui.cntt.myproject.persistance.entity.User;
import haui.cntt.myproject.service.Impl.CategoryServiceImpl;
import haui.cntt.myproject.service.Impl.PropertyServiceImpl;
import haui.cntt.myproject.service.Impl.RoleServiceImpl;
import haui.cntt.myproject.service.Impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class MyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyProjectApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleServiceImpl roleService, UserServiceImpl userService, CategoryServiceImpl categoryService, PropertyServiceImpl propertyService) {
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

			try{
				propertyService.create(Property.builder().name("Hãng").note("Nhãn hiệu sản phẩm").build());
				propertyService.create(Property.builder().name("Màu sắc").note("Màu sắc sản phẩm").build());
				propertyService.create(Property.builder().name("ROM").note("Đơn vị GB").build());
				propertyService.create(Property.builder().name("RAM").note("Đơn vị GB").build());
				propertyService.create(Property.builder().name("Bảo hành").note("Trạng thái bảo hàng của sản phẩm (còn bảo hành, hết bảo hành)").build());
				propertyService.create(Property.builder().name("CPU").note("Bộ vi xử lý của sản phẩm").build());
				propertyService.create(Property.builder().name("Kích thước màn hình").note("Đơn vị inch").build());
				propertyService.create(Property.builder().name("Năm đăng ký xe").note("Năm đăng ký xe").build());
				propertyService.create(Property.builder().name("Loại xe máy").note("Tay ga, số, côn,...").build());
				propertyService.create(Property.builder().name("Loại xe đạp").note("Xe thể thao, xe phổ thông, xe trẻ em").build());
				propertyService.create(Property.builder().name("Dung tích xe").note("Đơn vị cc").build());
				propertyService.create(Property.builder().name("Số KM đã đi").note("Đơn vị Km").build());
				propertyService.create(Property.builder().name("Biển số").note("Biển số xe").build());
				propertyService.create(Property.builder().name("Thể tích").note("Đơn vị lit (L)").build());
				propertyService.create(Property.builder().name("Chất liệu").note("Nhựa, gỗ, kính,...").build());
				propertyService.create(Property.builder().name("Công suất").note("Đơn vị W").build());
				propertyService.create(Property.builder().name("Khối lượng giặt").note("Đơn vị Kg").build());
				propertyService.create(Property.builder().name("Loại cửa máy giặt").note("Cửa trước, cửa trên").build());
				propertyService.create(Property.builder().name("Chất liệu vải").note("Len, cotton, nhung,...").build());
				propertyService.create(Property.builder().name("Size").note("").build());
				propertyService.create(Property.builder().name("Loại dụng cụ nhà bếp").note("Xoong, nồi, bát, đĩa, bếp ga,...").build());
				propertyService.create(Property.builder().name("Kích thước giường").note("2mx2.2m, 1.8x2m,...").build());
			}catch (Exception ignored)
			{

			}

			try {
				categoryService.createTest(Category.builder()
						.name("Đồ điện tử")
						.build());
				categoryService.createTest(Category.builder()
						.name("Xe cộ")
						.build());
				categoryService.createTest(Category.builder()
						.name("Quần áo, phụ kiện")
						.build());
				categoryService.createTest(Category.builder()
						.name("Sách truyện")
						.build());
				categoryService.createTest(Category.builder()
						.name("Tủ lạnh, điều hòa, máy giặt")
						.build());
				categoryService.createTest(Category.builder()
						.name("Đồ gia dụng, nột thất")
						.build());

				categoryService.createTest2(
						Category.builder().name("Điện thoại").build(),
						"Đồ điện tử",
						Arrays.asList("Hãng", "ROM", "RAM", "Kích thước màn hình", "Bảo hành")
				);
				categoryService.createTest2(
						Category.builder().name("Laptop").build(),
						"Đồ điện tử",
						Arrays.asList("Hãng", "CPU", "ROM", "RAM", "Kích thước màn hình", "Bảo hành")
				);
				categoryService.createTest2(
						Category.builder().name("Máy tính bàn").build(),
						"Đồ điện tử",
						Arrays.asList("CPU", "ROM", "RAM", "Kích thước màn hình", "Bảo hành")
				);
				categoryService.createTest2(
						Category.builder().name("Phụ kiện").build(),
						"Đồ điện tử",
						Arrays.asList("Bảo hành")
				);
				categoryService.createTest2(
						Category.builder().name("Linh kiện").build(),
						"Đồ điện tử",
						Arrays.asList()
				);

				categoryService.createTest2(
						Category.builder().name("Xe máy").build(),
						"Xe cộ",
						Arrays.asList("Hãng", "Năm đăng ký xe", "Loại xe máy", "Dung tích xe", "Số KM đã đi", "Biển số")
				);
				categoryService.createTest2(
						Category.builder().name("Xe đạp").build(),
						"Xe cộ",
						Arrays.asList()
				);

				categoryService.createTest2(
						Category.builder().name("Quần áo").build(),
						"Quần áo, phụ kiện",
						Arrays.asList("Chất liệu vải", "Size")
				);
				categoryService.createTest2(
						Category.builder().name("Giày dép").build(),
						"Quần áo, phụ kiện",
						Arrays.asList("Size")
				);
				categoryService.createTest2(
						Category.builder().name("Phụ kiện thời trang").build(),
						"Quần áo, phụ kiện",
						Arrays.asList()
				);

				categoryService.createTest2(
						Category.builder().name("Sách").build(),
						"Sách truyện",
						Arrays.asList()
				);
				categoryService.createTest2(
						Category.builder().name("Truyện").build(),
						"Sách truyện",
						Arrays.asList()
				);

				categoryService.createTest2(
						Category.builder().name("Tủ lạnh").build(),
						"Tủ lạnh, điều hòa, máy giặt",
						Arrays.asList("Hãng", "Thể tích", "Bảo hành")
				);
				categoryService.createTest2(
						Category.builder().name("Điều hòa").build(),
						"Tủ lạnh, điều hòa, máy giặt",
						Arrays.asList("Hãng", "Bảo hành")
				);
				categoryService.createTest2(
						Category.builder().name("Máy giặt").build(),
						"Tủ lạnh, điều hòa, máy giặt",
						Arrays.asList("Hãng", "Công suất", "Khối lượng giặt", "Loại cửa máy giặt", "Bảo hành")
				);

				categoryService.createTest2(
						Category.builder().name("Tủ, kệ, bàn").build(),
						"Đồ gia dụng, nột thất",
						Arrays.asList("Chất liệu")
				);
				categoryService.createTest2(
						Category.builder().name("Giường, chăn, nệm").build(),
						"Đồ gia dụng, nột thất",
						Arrays.asList("Chất liệu")
				);
				categoryService.createTest2(
						Category.builder().name("Dụng cụ nhà bếp").build(),
						"Đồ gia dụng, nột thất",
						Arrays.asList("Loại dụng cụ nhà bếp")
				);

			} catch (Exception ignored) {
			}
		};
	}
}
