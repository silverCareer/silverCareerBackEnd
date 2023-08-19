package com.example.demo.src.search.mapper;

import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.DisplayProductRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-17T17:23:23+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class SearchMapperImpl implements SearchMapper {

    @Override
    public List<DisplayProductRes> displayProductRes(List<Product> product) {
        if ( product == null ) {
            return null;
        }

        List<DisplayProductRes> list = new ArrayList<DisplayProductRes>( product.size() );
        for ( Product product1 : product ) {
            list.add( productToDisplayProductRes( product1 ) );
        }

        return list;
    }

    protected DisplayProductRes productToDisplayProductRes(Product product) {
        if ( product == null ) {
            return null;
        }

        DisplayProductRes.DisplayProductResBuilder displayProductRes = DisplayProductRes.builder();

        displayProductRes.productIdx( product.getProductIdx() );
        displayProductRes.productName( product.getProductName() );

        return displayProductRes.build();
    }
}
