package com.example.demo.src.product;


import com.example.demo.global.exception.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/productAll")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/product/{prodIdx}")
    public BaseResponse<ResponseProductRegister> getProductById(@PathVariable Long prodIdx) {
        ResponseProductRegister responseProduct = productService.getProductById(prodIdx);

        return new BaseResponse<>(responseProduct);
    }
    @PostMapping("/productAdd")
    public ResponseProductRegister addProduct(@RequestBody RequestProductRegister requestProduct) {
        ResponseProductRegister responseProduct = productService.addProduct(requestProduct);

        return responseProduct;
    }

    // 추가적인 API 엔드포인트들을 작성할 수 있습니다.
    // ...
}
