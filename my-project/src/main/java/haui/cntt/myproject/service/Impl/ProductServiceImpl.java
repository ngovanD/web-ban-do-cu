package haui.cntt.myproject.service.Impl;

import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.common.exception.BadRequestReturnPageException;
import haui.cntt.myproject.common.file.FileUploadUtil;
import haui.cntt.myproject.common.myEnum.OrderStatusEnum;
import haui.cntt.myproject.common.myEnum.ProductStatusEnum;
import haui.cntt.myproject.common.text.VNCharacterUtil;
import haui.cntt.myproject.persistance.entity.*;
import haui.cntt.myproject.persistance.repository.*;
import haui.cntt.myproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    static final String UPLOAD_DIR_IMAGE_PRODUCT = "src/main/resources/static/product/";

    @Transactional
    public Product create(Product product) throws Throwable {
        Category foundCategory = categoryRepository.findById(product.getCategory().getId()).orElse(null);
        product.setCategory(foundCategory);

        List<ProductProperty> productProperties = new ArrayList<>();
        for (ProductProperty productProperty : product.getProductProperties()) {
            Property foundProperty = propertyRepository.findById(productProperty.getProperty().getId()).orElse(null);
            productProperty.setProperty(foundProperty);
            productProperty.setProduct(product);
            productProperties.add(productProperty);
        }
        product.setProductProperties(productProperties);

        product.getAddress().setProduct(product);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User foundUser = userRepository.findByUsername(username).orElseThrow(
                () -> {
                    throw new BadRequestException("User kh??ng t???n t???i !!!");
                }
        );
        product.setUser(foundUser);

        product.setStatus(ProductStatusEnum.STOCKING);

        return productRepository.save(product);
    }

    @Transactional
    public void uploadImage(long productId, MultipartFile[] multipartFiles) throws IOException, Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> {
            throw new BadRequestException("S???n ph???m kh??ng t???n t???i !!!");
        });
        String uploadDir = UPLOAD_DIR_IMAGE_PRODUCT + productId;

        List<ImageProduct> imageProductList = new ArrayList<>();
        int index = 0;
        for (MultipartFile image : multipartFiles) {
            String fileName = image.getName();
            String typeOfImage = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
            ImageProduct imageProduct = ImageProduct.builder()
                    .fileName(fileName + index + typeOfImage)
                    .isMainImage(index == 0 ? true : false)
                    .build();
            imageProduct.setProduct(foundProduct);
            imageProductList.add(imageProduct);
            FileUploadUtil.saveFile(uploadDir, imageProduct.getFileName(), multipartFiles[index]);
            index++;
        }

        foundProduct.setImageProducts(imageProductList);
        productRepository.save(foundProduct);
    }

    @Transactional
    public Product getDetailProductBySlug(String slug) throws Throwable {
        Product foundProduct = productRepository.findBySlug(slug).orElseThrow(() -> {
            throw new BadRequestException("S???n ph???m kh??ng t???n t???i !!!");
        });

        foundProduct.setView(foundProduct.getView() + 1);
        return productRepository.save(foundProduct);
    }

    public Product getDetailProduct(long productId) throws Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> {
            throw new BadRequestException("S???n ph???m kh??ng t???n t???i !!!");
        });

        return foundProduct;
    }

    @Transactional
    public Product getMyDetailProduct(long productId) throws Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> {
            throw new BadRequestException("S???n ph???m kh??ng t???n t???i !!!");
        });
        if (!foundProduct.getUser().getUsername()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new BadRequestReturnPageException("B???n kh??ng th??? truy c???p trang n??y !!!");
        }
        return foundProduct;
    }

    @Transactional
    public void edit(Product product) throws Throwable {
        Product foundProduct = productRepository.findById(product.getId()).orElseThrow(() -> {
            throw new BadRequestException("S???n ph???m kh??ng t???n t???i !!!");
        });
        if (!foundProduct.getStatus().equals(ProductStatusEnum.STOCKING)) {
            throw new BadRequestException("Ch??? c?? th??? c???p nh???t khi s???n ph???m ??? tr???ng th??i ??ang b??n !!!");
        }
        foundProduct.setName(product.getName());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setCurrentStatus(product.getCurrentStatus());
        foundProduct.setDescription(product.getDescription());

        List<ProductProperty> productProperties = foundProduct.getProductProperties().stream().collect(Collectors.toList());
        for (ProductProperty productProperty : productProperties) {
            String value = product.getProductProperties()
                    .stream()
                    .filter(p -> p.getProperty().getId().equals(productProperty.getProperty().getId()))
                    .findFirst().orElseThrow(
                            () -> {
                                throw new BadRequestException("Thu???c t??nh s???n ph???m kh??ng t???n t???i !!!");
                            })
                    .getValue();
            productProperty.setValue(value);
        }

        Address foundAddress = foundProduct.getAddress();
        foundAddress.setProvince(product.getAddress().getProvince());
        foundAddress.setDistrict(product.getAddress().getDistrict());
        foundAddress.setCommune(product.getAddress().getCommune());
        foundAddress.setDetail(product.getAddress().getDetail());

        productRepository.save(foundProduct);
    }

    @Transactional
    public void updateStatus(long productId) throws Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> {
                    throw new BadRequestException("S???n ph???m kh??ng t???n t???i !!!");
                }
        );
        foundProduct.setStatus(ProductStatusEnum.SOLD_OUT);
        productRepository.save(foundProduct);
    }

    @Transactional
    public void delete(long productId) throws Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> {
                    throw new BadRequestException("S???n ph???m kh??ng t???n t???i !!!");
                }
        );

        if (!foundProduct.getStatus().equals(ProductStatusEnum.STOCKING)) {
            throw new BadRequestException("Kh??ng th??? x??a s???n ph???m ??ang ??? tr???ng th??i ch??? x??c nh???n ho???c ???? b??n !!!");
        }
        String uploadDir = UPLOAD_DIR_IMAGE_PRODUCT + productId;
        FileUploadUtil.deleteDir(uploadDir);
        productRepository.delete(foundProduct);
    }

    public List<Product> getRandomProduct(int limit) {
        return productRepository.getRandomProduct(limit);
    }

    public List<Product> getNewProduct(int limit) {
        return productRepository.getNewProduct(limit);
    }

    public List<Product> getHotProduct(int limit) {
        return productRepository.getHotProduct(limit);
    }

    public Page<Product> searchProduct(String keyword, int page, String slugCategory
            , int min, int max, String sort, int codeProvince, String status) {
        List<Product> productList = productRepository.filterProductList(slugCategory, min, max, codeProvince, status);
        String formatKeyword = VNCharacterUtil.removeAccent(keyword).toLowerCase();
        String[] listKey = formatKeyword.split(" ");
        Set<Product> setProduct = new HashSet<>();

        for (String key : listKey) {
            setProduct.addAll(productList.stream().filter(p -> p.getKeyword().contains(" "+key+" ")).collect(Collectors.toList()));
        }
        productList = new ArrayList<>(setProduct);

        if (sort.equals("price")) {
            Collections.sort(productList, Comparator.comparingLong(Product::getPrice));
        }
        if (sort.equals("created_date")) {
            Collections.sort(productList, (lhs, rhs) -> {
                if (lhs.getCreatedDate().isBefore(rhs.getCreatedDate())) {
                    return 1;
                }
                return -1;
            });
        }


        Pageable pageable = PageRequest.of(page, 12);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), productList.size());
        return new PageImpl<>(productList.subList(start, end), pageable, productList.size());
    }

    @Transactional
    public Order deliveryConfirmation(long productId) throws Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> {
                    throw new BadRequestException("Kh??ng t??m th???y s???n ph???m !!!");
                }
        );
        foundProduct.setStatus(ProductStatusEnum.SOLD_OUT);
        productRepository.save(foundProduct);

        Order foundOrder = orderRepository.findByProductId(productId).orElseThrow(
                () -> {
                    throw new BadRequestException("Kh??ng t??m th???y ????n h??ng ?????t s???n ph???m n??y !!!");
                }
        );
        foundOrder.setStatus(OrderStatusEnum.DELIVERY);
        return orderRepository.save(foundOrder);
    }

    @Transactional
    public void cancelDelivery(long productId) throws Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> {
                    throw new BadRequestException("Kh??ng t??m th???y s???n ph???m !!!");
                }
        );

        foundProduct.setStatus(ProductStatusEnum.STOCKING);
        productRepository.save(foundProduct);

        Order foundOrder = orderRepository.findByProductId(productId).orElseThrow(
                () -> {
                    throw new BadRequestException("Kh??ng t??m th???y ????n h??ng ?????t s???n ph???m n??y !!!");
                }
        );
        if (foundOrder.getStatus().equals(OrderStatusEnum.PAID)) {
            foundOrder.setStatus(OrderStatusEnum.WAITING_REFUND);
        } else {
            foundOrder.setStatus(OrderStatusEnum.CANCELED);
        }
        orderRepository.save(foundOrder);
    }

    public int getNewProduct(String from, String to) {
        return productRepository.getNewProduct(from, to);
    }

    public List<Product> getRandomAllProduct(int limit) {
        return productRepository.getRandomProduct(limit);
    }

    public List<Product> getRandomQuanAo(int limit) {
        return productRepository.getRandomQuanAo(limit);
    }

    public List<Product> getRandomDoDienTu(int limit) {
        return productRepository.getRandomDoDienTu(limit);
    }

    public List<Product> getRandomSachTruyen(int limit) {
        return productRepository.getRandomSachTruyen(limit);
    }

    public List<Product> getRecommendList(long productId, int limit) throws Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> {
                    throw new BadRequestException("Kh??ng t??m th???y s???n ph???m !!!");
                }
        );
        return productRepository.getRecommendList(foundProduct.getCategory().getId(), productId, limit);
    }
}
