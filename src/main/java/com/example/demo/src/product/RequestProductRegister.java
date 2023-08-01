package com.example.demo.src.product;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Builder
public record RequestProductRegister(

        @NotNull
        String productName,
        @NotNull
        String description,
        @NotNull
        String categoryName,
        @NotNull
        String image,
        @NotNull
        Long saleCount,
        @NotNull
        Long likes

){

}