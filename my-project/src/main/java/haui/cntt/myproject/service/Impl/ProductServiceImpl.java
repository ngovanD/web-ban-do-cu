package haui.cntt.myproject.service.Impl;

import haui.cntt.myproject.common.enum_.ProductStatusEnum;
import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.common.file.FileUploadUtil;
import haui.cntt.myproject.common.text.VNCharacterUtil;
import haui.cntt.myproject.persistance.entity.*;
import haui.cntt.myproject.persistance.repository.CategoryRepository;
import haui.cntt.myproject.persistance.repository.ProductRepository;
import haui.cntt.myproject.persistance.repository.PropertyRepository;
import haui.cntt.myproject.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;
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
                    throw new BadRequestException("User không tồn tại !!!");
                }
        );
        product.setUser(foundUser);

        product.setStatus(ProductStatusEnum.STOCKING);

        return productRepository.save(product);
    }

    @Transactional
    public void uploadImage(long productId, MultipartFile[] multipartFiles) throws IOException, Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> {
            throw new BadRequestException("Sản phẩm không tồn tại !!!");
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
    public Product getDetailProduct(long productId) throws Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> {
            throw new BadRequestException("Sản phẩm không tồn tại !!!");
        });

        foundProduct.setView(foundProduct.getView() + 1);
        return productRepository.save(foundProduct);
    }

    @Transactional
    public void edit(Product product) throws Throwable {
        Product foundProduct = productRepository.findById(product.getId()).orElseThrow(() -> {
            throw new BadRequestException("Sản phẩm không tồn tại !!!");
        });
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
                                throw new BadRequestException("Thuộc tính sản phẩm không tồn tại !!!");
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
                    throw new BadRequestException("Sản phẩm không tồn tại !!!");
                }
        );
        foundProduct.setStatus(ProductStatusEnum.SOLD_OUT);
        productRepository.save(foundProduct);
    }

    @Transactional
    public void delete(long productId) throws Throwable {
        Product foundProduct = productRepository.findById(productId).orElseThrow(
                () -> {
                    throw new BadRequestException("Sản phẩm không tồn tại !!!");
                }
        );
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

    public Page<Product> searchProduct(String keyword, int page, String slugCategory, int min, int max, String sort, int codeProvince) {


        List<Product> productList = productRepository.filterProductList(slugCategory, min, max, codeProvince);
        String formatKeyword = VNCharacterUtil.removeAccent(keyword).toLowerCase();
        String[] listKey = formatKeyword.split(" ");
        Set<Product> setProduct = new HashSet<>();

        for(String key : listKey)
        {
            setProduct.addAll(productList.stream().filter(p -> p.getKeyword().contains(key)).collect(Collectors.toList()));
        }
        productList = new ArrayList<>(setProduct);

        if(sort.equals("price"))
        {
            Collections.sort(productList, Comparator.comparingLong(Product::getPrice));
        }
        if(sort.equals("created_date"))
        {
            Collections.sort(productList, (lhs, rhs) -> {
                if(lhs.getCreatedDate().isBefore(rhs.getCreatedDate()))
                {
                    return 1;
                }
                return -1;
            });
        }


        Pageable pageable = PageRequest.of(page, 12);
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), productList.size());
        return new PageImpl<>(productList.subList(start, end), pageable, productList.size());
    }
}
