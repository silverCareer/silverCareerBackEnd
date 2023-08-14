package com.example.demo.src.product.repository;

import com.example.demo.src.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findByCategory(String category);
    Optional<Product> findProductByProductIdx(Long productIdx);

    @Query("SELECT p FROM Product p where p.productName LIKE CONCAT('%', :query, '%')")
    Page<Product> findByProductName(@Param("query")String query, Pageable pageable);
}
