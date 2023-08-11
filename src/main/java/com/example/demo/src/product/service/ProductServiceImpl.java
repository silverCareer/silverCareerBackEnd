package com.example.demo.src.product.service;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.S3Service;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.*;
import com.example.demo.src.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void createProduct(String username, CreateProductReq createProductReq) throws IOException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

        MultipartFile image = createProductReq.getProductImage();
        String imageUrl = "";

        if (image != null && !image.isEmpty()) {
            imageUrl = s3Service.upload(image, "product",
                    username + "_" + createProductReq.getProductName());
        }

        Product product = Product.builder()
                .productName(createProductReq.getProductName())
                .description(createProductReq.getProductDescription())
                .category(createProductReq.getCategory())
                .price(createProductReq.getPrice())
                .image(imageUrl)
                .likes(0L)
                .saleCount(0L)
                .member(member)
                .build();

        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisplayProductRes> displayProductByCategory(String category) {
        List<Product> productList = category.equals("all")
                ? productRepository.findAll()
                : productRepository.findByCategory(category);

        if (productList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ELEMENT);
        }

        return productList.stream()
                .map(DisplayProductRes::of) // 람다 표현식 사용
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailRes getProductDetail(Long productId) {
        Product product = productRepository.findProductByProductIdx(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

        return ProductDetailRes.builder()
                .productIdx(product.getProductIdx())
                .productName(product.getProductName())
                .category(product.getCategory())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .likes(product.getLikes())
                .build();
    }
}
