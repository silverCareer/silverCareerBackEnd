package com.example.demo.src.search.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.search.NotFoundSearchException;
import com.example.demo.src.likes.repository.LikeRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.ResponseDisplayProducts;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.search.dto.ResponseMultiSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{
    private final ProductRepository productRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> search(final Authentication authentication, final String query, final int page, final int size){
        Page<Product> searchProduct = productRepository.findByProductName(query, PageRequest.of(page - 1, size, Sort.by("productIdx").descending()));
        if(searchProduct.isEmpty() || searchProduct == null){
            throw new NotFoundSearchException();
        }
        List<ResponseDisplayProducts> searchListDto = searchProduct.getContent().stream()
                .map(product -> {
                    boolean isLiked = false;
                    if (authentication != null && authentication.isAuthenticated()){
                        String authority = authentication.getAuthorities().stream()
                                .findFirst().map(GrantedAuthority::getAuthority).orElse("");
                        if("ROLE_MENTEE".equals(authority)){
                            String memberEmail = authentication.getName();
                            isLiked = likeRepository.existsByProductIdxAndMemberEmail(product.getProductIdx(), memberEmail);
                        }
                    }
                    return ResponseDisplayProducts.of(product, isLiked);
                })
                .collect(Collectors.toList());

        ResponseMultiSearch<ResponseDisplayProducts> response =
                new ResponseMultiSearch<>(searchListDto, page, searchProduct.getTotalPages(), searchProduct.getTotalElements());

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }
}
