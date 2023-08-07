package com.example.demo.src.product.service;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.CreateProduct;
import com.example.demo.src.product.dto.DisplayProductReq;
import com.example.demo.src.product.dto.DisplayProductRes;
import com.example.demo.src.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createProduct(String username, CreateProduct createProduct) {
        String productName = createProduct.productName();
        String productDescription = createProduct.productDescription();
        String category = createProduct.category();
        Long price = createProduct.price();
        String productImages = createProduct.productImages();

        Member member = memberRepository.findMemberByUsername(username);

        Product product = Product.builder()
                .productName(productName)
                .description(productDescription)
                .category(category)
                .price(price)
                .image(productImages)
                .likes(0L)
                .saleCount(0L)
                .member(member)
                .build();

        productRepository.save(product);
    }

    @Transactional
    public DisplayProductRes displayProductByCategory(DisplayProductReq displayProductReq) {
        String category = displayProductReq.category();
        List<Product> products;

        if(category.equals("all")){
            products = productRepository.findAll();
        }
        else{
            products = productRepository.findByCategory(category);
        }

        List<DisplayProductRes.ProductDto> productDtoList = products.stream().map(this::mapToProductDto).collect(Collectors.toList());
        return DisplayProductRes.builder().products(productDtoList).build();
    }

    private DisplayProductRes.ProductDto mapToProductDto(Product product) {
        return DisplayProductRes.ProductDto.builder()
                .productIdx(product.getProductIdx())
                .productName(product.getProductName())
                .productDescription(product.getDescription())
                .productPrice(product.getPrice())
                .productImage(product.getImage())
                .productLikes(product.getLikes())
                .build();
    }
}
