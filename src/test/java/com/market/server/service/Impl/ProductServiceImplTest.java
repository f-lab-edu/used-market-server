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
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductMapper productMapper;

    @Mock
    UserProfileMapper userProfileMapper;

    @Mock
    ProductDao productDao;

    // 새로운 물품 객체를 생성하여 반환한다.
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

    // 새로운 물품 객체 리스트를 생성하여 반환한다.
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

    public UserDTO generateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("testUserId");
        userDTO.setPassword(SHA256Util.encryptSHA256("testPassword"));
        userDTO.setName("testUserName");
        userDTO.setPhone("010-1111-2222");
        userDTO.setAddress("testAdress");
        userDTO.setStatus(UserDTO.Status.DEFAULT);
        userDTO.setCreatetime(new Date());
        userDTO.setUpdatetime(new Date());
        userDTO.setAddmin(false);
        userDTO.setAccountId(1);
        return userDTO;
    }

    @Test
    void register() {
        ProductDTO productDTO = generateProduct();
        given(productMapper.register(productDTO)).
                willReturn(productDTO.getId());

        UserDTO userDTO = generateUser();
        given(userProfileMapper.getUserProfile(userDTO.getId()))
                .willReturn(userDTO);

        productService.register(userDTO.getId(), productDTO);
    }

    @Test
    void getMyProducts() {
        List<ProductDTO> productDTOList = generateProductList();
        given(productMapper.selectMyProducts(1)).willReturn(productDTOList);
        given(productMapper.selectMyProducts(999)).willReturn(null);
        productMapper.selectMyProducts(1);

        UserDTO userDTO = generateUser();

        productService.getMyProducts(userDTO.getAccountId());
    }

    @Test
    void updateProducts() {
        ProductDTO productDTO = generateProduct();
        productDTO.setContents("testProductContentsMapper");
        productMapper.updateProducts(productDTO);
        assertTrue(productDTO.getContents().equals("testProductContentsMapper"));

        productDTO.setContents("testProductContentService");
        productService.updateProducts(productDTO);
        assertTrue(productDTO.getContents().equals("testProductContentService"));
    }

    @Test
    void deleteProduct() {
        ProductDTO productDTO = generateProduct();
        UserDTO userDTO = generateUser();
        given(userProfileMapper.getUserProfile(userDTO.getId()))
                .willReturn(userDTO);

        productMapper.deleteProduct(1, 1);
        productService.deleteProduct(userDTO.getAccountId(), productDTO.getId());

    }
}