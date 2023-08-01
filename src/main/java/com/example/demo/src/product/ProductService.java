package com.example.demo.src.product;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public ResponseProductRegister getProductById(Long prodIdx) {
        Product product = productRepository.findById(prodIdx).orElse(null);

        return ResponseProductRegister.of(product);
    }

    public ResponseProductRegister addProduct(RequestProductRegister requestProduct) {
        // RequestProductRegister 객체로부터 상품 정보를 읽어와 Product 객체로 변환
        Product product = Product.builder()
                .productName(requestProduct.productName())
                .description(requestProduct.description())
                .categoryName(requestProduct.categoryName())
                .image(requestProduct.image())
                .saleCount(requestProduct.saleCount())
                .likes(requestProduct.likes())
                .build();

        // Product 객체를 저장하고 저장된 결과를 가져옴
        Product savedProduct = productRepository.save(product);

        // 저장된 상품 정보를 ResponseProductRegister 객체로 변환하여 반환
        return ResponseProductRegister.of(savedProduct);
    }

    // 추가적인 비즈니스 로직들을 작성할 수 있습니다.
    // ...
}