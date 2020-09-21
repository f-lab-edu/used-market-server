package com.market.server.service.Impl;

import com.market.server.dao.ProductDao;
import com.market.server.dto.ProductDTO;
import com.market.server.dto.UserDTO;
import com.market.server.mapper.ProductMapper;
import com.market.server.mapper.UserProfileMapper;
import com.market.server.utils.SHA256Util;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductSearchServiceImpl productSearchService;

    @Mock
    ProductMapper productMapper;

    @Mock
    ProductDao productDao;

    // 새로운 멤버 객체를 생성하여 반환한다.
    public ProductDTO generateProduct() {
        MockitoAnnotations.initMocks(this); // mock all the field having @Mock annotation
        ProductDTO productDTO = ProductDTO.builder()
                .id(1)
                .price(1000)
                .accountId(1)
                .title("testProductTitle")
                .contents("testProductContents")
                .status(ProductDTO.Status.NEW)
                .istrade(true)
                .updatetime(new Date())
                .deliveryprice(3000)
                .dibcount(1)
                .categoryId(1)
                .build();
        return productDTO;
    }

    // 새로운 멤버 객체 리스트를 생성하여 반환한다.
    public List<ProductDTO> generateProductList() {
        MockitoAnnotations.initMocks(this); // mock all the field having @Mock annotation
        List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();
        for (int i = 0; i < 5; i++) {
            ProductDTO productDTO = ProductDTO.builder()
                    .id(i)
                    .price(1000)
                    .accountId(1)
                    .title("testProductTitle")
                    .contents("testProductContents")
                    .status(ProductDTO.Status.NEW)
                    .istrade(true)
                    .updatetime(new Date())
                    .deliveryprice(3000)
                    .dibcount(1)
                    .categoryId(1)
                    .build();
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Test
    void register() {
        ProductDTO productDTO = generateProduct();
        given(productMapper.register(productDTO)).willReturn(productDTO.getId());
        productMapper.register(productDTO);
    }

    @Test
    void getMyProducts() {
        List<ProductDTO> productDTOList = generateProductList();
        given(productMapper.selectMyProducts(1)).willReturn(productDTOList);
        given(productMapper.selectMyProducts(999)).willReturn(null);
        productMapper.selectMyProducts(1);
    }

    @Test
    void updateProducts() {
        ProductDTO productDTO = generateProduct();
        productDTO.setContents("testProductContents");
        productMapper.updateProducts(productDTO);
        assertTrue(productDTO.getContents().equals("testProductContents"));
    }

    @Test
    void deleteProduct() {
        ProductDTO productDTO = generateProduct();
        productDTO.setId(1);
        productMapper.deleteProduct(1, 1);
    }
}