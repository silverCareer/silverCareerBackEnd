package com.example.demo.src.product.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.S3Service;
import com.example.demo.global.exception.error.member.NotFoundMemberException;
import com.example.demo.global.exception.error.product.InvalidProductInfoException;
import com.example.demo.global.exception.error.product.NotFoundProductException;
import com.example.demo.global.exception.error.product.NotFoundProductListException;
import com.example.demo.src.likes.repository.LikeRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
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
    private final LikeRepository likeRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> createProduct
            (final String username, final MultipartFile image, final RequestCreateProduct requestCreateProduct) throws IOException {
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
    public ResponseEntity<CommonResponse> displayProductByCategory(Authentication authentication, final String category, final int page, final int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "productIdx"));
        Page<Product> productPage = category.equals("all")
                ? productRepository.findAll(pageable)
                : productRepository.findByCategory(category, pageable);

        if (productPage.isEmpty()) {
            throw new NotFoundProductListException();
        }

        List<ResponseDisplayProducts> productListDto = productPage.stream()
                .map(product -> {
                    boolean isLiked = false;
                    if (authentication != null && authentication.isAuthenticated()) {
                        String authority = authentication.getAuthorities().stream()
                                .findFirst().map(GrantedAuthority::getAuthority).orElse("");
                        if ("ROLE_MENTEE".equals(authority)) {
                            String memberEmail = authentication.getName();
                            Member member = memberRepository.findMemberByUsername(memberEmail);
                            isLiked = likeRepository.existsByProductAndMember(product, member);
                        }
                    }
                    return ResponseDisplayProducts.of(product, isLiked);
                })
                .collect(Collectors.toList());

        ResponseMultiProduct<ResponseDisplayProducts> response =
                new ResponseMultiProduct<>(productListDto, productPage.getNumber() + 1, productPage.getTotalPages());

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CommonResponse> getProductDetail(Authentication authentication, final Long productId) {
        Product product = productRepository.findProductByProductIdx(productId)
                .orElseThrow(NotFoundProductException::new);

        int status = 1;
        boolean isLiked = false;
        String memberEmail;

        if(authentication != null && authentication.isAuthenticated()){
            memberEmail = authentication.getName();
            String authority = authentication.getAuthorities().stream()
                    .findFirst().map(GrantedAuthority::getAuthority).orElse("");

            if(authority.equals("ROLE_MENTOR")){
                status = 2;
            }
            else{
                status = paymentRepository.findPaymentByMember_UsernameAndProductIdx(memberEmail, productId)
                        == null ? 3 : 4;
                Member member = memberRepository.findMemberByUsername(memberEmail);
                isLiked = likeRepository.existsByProductAndMember(product, member);
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
                .memberName(product.getMember().getName())
                .memberCareer(product.getMember().getCareer())
                .isLiked(isLiked)
                .status(status)
                .reviews(reviews)
                .build();

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CommonResponse> getRecommendProduct(){
        List<Product> recommendProduct = productRepository.findTop10ByOrderBySalesCountDesc()
                .orElseThrow(NotFoundProductListException::new);

        List<ResponseTop10Products> response = recommendProduct.stream()
                .map(ResponseTop10Products::of).collect(Collectors.toList());

        return ResponseEntity.ok().body(CommonResponse.builder().success(true).response(response).build());
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
