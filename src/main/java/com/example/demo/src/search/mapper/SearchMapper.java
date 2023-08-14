package com.example.demo.src.search.mapper;

import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.DisplayProductRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchMapper {
    @Mapping(source = "productId", target = "id")
    List<DisplayProductRes> displayProductRes(List<Product> product);
}
