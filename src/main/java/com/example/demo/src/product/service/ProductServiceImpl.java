package com.example.demo.src.product.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.S3Service;
import com.example.demo.global.exception.error.member.NotFoundMemberException;
import com.example.demo.global.exception.error.product.InvalidProductInfoException;
import com.example.demo.global.exception.error.product.NotFoundProductException;
import com.example.demo.global.exception.error.product.NotFoundProductListException;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.payment.domain.Payment;
import com.example.demo.src.payment.repository.PaymentRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.*;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final PaymentRepository paymentRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> createProduct(String username, MultipartFile image, RequestCreateProduct requestCreateProduct) throws IOException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(NotFoundMemberException::new);

        validateProductInfo(image, requestCreateProduct);

        String imageUrl = s3Service.upload(image, "product",
                    username + "_" + requestCreateProduct.getProductName());

        Product product = Product.builder()
                .productName(requestCreateProduct.getProductName())
                .description(requestCreateProduct.getProductDescription())
                .category(requestCreateProduct.getCategory())
                .address(requestCreateProduct.getAddress())
                .price(requestCreateProduct.getPrice())
                .image(imageUrl)
                .likes(0L)
                .saleCount(0L)
                .member(member)
                .build();

        productRepository.save(product);

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("상품 등록 성공").build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CommonResponse> displayProductByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "productIdx"));
        Page<Product> productPage;
        if(category.equals("all")){
            productPage = productRepository.findAll(pageable);
        } else {
            productPage = productRepository.findByCategory(category, pageable);
        }

        if(productPage.isEmpty() || productPage == null) {
            throw new NotFoundProductListException();
        }

        List<ResponseDisplayProducts> productListDto = productPage.stream().map(ResponseDisplayProducts::of).collect(Collectors.toList());
        ResponseMultiProduct<ResponseDisplayProducts> response =
                new ResponseMultiProduct<>(productListDto, productPage.getNumber() + 1, productPage.getTotalPages());

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CommonResponse> getProductDetail(Authentication authentication, Long productId) {
        Product product = productRepository.findProductByProductIdx(productId)
                .orElseThrow(NotFoundProductException::new);
        Member member = product.getMember();
        int status = 1;
        if(authentication != null && authentication.isAuthenticated()){
            String username = authentication.getName();
            String authority = "";
            for(GrantedAuthority auth : authentication.getAuthorities()){
                authority = auth.getAuthority();
                break;
            }
            if(authority.equals("ROLE_MENTOR")){
                status = 2;
            }
            else{
                status = paymentRepository.findPaymentByMember_UsernameAndProductIdx(username, productId) == null
                        ? 3
                        : 4;
            }
        }
        List<ReviewDto> reviews = product.getReviews().stream().map(ReviewDto::of).collect(Collectors.toList());

        ResponseProductDetail response = ResponseProductDetail.builder()
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
                .status(status)
                .build();
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }


    // Helper Function
    private void validateProductInfo(MultipartFile image, RequestCreateProduct requestCreateProduct) {
        if (StringUtils.isBlank(requestCreateProduct.getProductName()) ||
                StringUtils.isBlank(requestCreateProduct.getProductDescription()) ||
                StringUtils.isBlank(requestCreateProduct.getCategory()) ||
                StringUtils.isBlank(requestCreateProduct.getAddress()) ||
                requestCreateProduct.getPrice() == null ||
                image == null || image.isEmpty()) {
            throw new InvalidProductInfoException();
        }
    }
}
