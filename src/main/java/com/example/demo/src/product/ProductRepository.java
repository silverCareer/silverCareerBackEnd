package com.example.demo.src.product;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 모든 Product 조회 메서드
    List<Product> findAll();

    // 특정 prodIdx에 해당하는 Product 조회 메서드
    Optional<Product> findById(Long prodIdx);

    // 추가적인 Product 조회 메서드들을 선언할 수 있습니다.
    // ...
}






