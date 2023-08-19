package com.example.demo.src.product.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.S3Service;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.*;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    public void createProduct(String username, MultipartFile image, CreateProductReq createProductReq) throws IOException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("잘못된 인증 요청입니다."));
        String imageUrl = "";
        if (image != null && !image.isEmpty()) {
            imageUrl = s3Service.upload(image, "product",
                    username + "_" + createProductReq.getProductName());
        }

        Product product = Product.builder()
                .productName(createProductReq.getProductName())
                .description(createProductReq.getProductDescription())
                .category(createProductReq.getCategory())
                .address(createProductReq.getAddress())
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
    public ResponseMultiProduct<DisplayProductRes> displayProductByCategory(String category, Pageable pageable) {
        Page<Product> productPage;
        if(category.equals("all")){
            productPage = productRepository.findAll(pageable);
        } else {
            productPage = productRepository.findByCategory(category, pageable);
        }

        List<Product> products = productPage.getContent();

        if (products.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ELEMENT);
        }

        List<DisplayProductRes> response = productPage.stream().map(DisplayProductRes::of).collect(Collectors.toList());

        return new ResponseMultiProduct<>(response, productPage.getNumber() + 1, productPage.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailRes getProductDetail(Long productId) {
        Product product = productRepository.findProductByProductIdx(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Member member = product.getMember();
        List<ReviewDto> reviews = product.getReviews().stream().map(ReviewDto::of).collect(Collectors.toList());

        return ProductDetailRes.builder()
                .productIdx(product.getProductIdx())
                .productName(product.getProductName())
                .category(product.getCategory())
                .address(product.getAddress())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .likes(product.getLikes())
                .memberName(member.getName())
                .memberCareer(member.getCareer())
                .reviews(reviews)
                .build();
    }
}
