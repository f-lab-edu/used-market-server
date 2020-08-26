package com.market.server.service.Impl;

import com.market.server.dto.ProductDTO;
import com.market.server.dto.UserDTO;
import com.market.server.mapper.ProductMapper;
import com.market.server.mapper.UserProfileMapper;
import com.market.server.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public void register(String id, ProductDTO productDTO) {
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        productDTO.setAccountId(memberInfo.getAccountId());
        productDTO.setCreatetime(new Date());

        if (productDTO.getStatus().ordinal() == 0 || productDTO.getStatus() == null)
            throw new RuntimeException("register ERROR! 상품 categoryId를 확인해주세요\n" + "categoryId : " + productDTO.getStatus().ordinal());
        else
            productDTO.setCategoryId(productDTO.getStatus().ordinal());

        if (productDTO.getPrice() == 0)
            throw new RuntimeException("register ERROR! 상품 price를 확인해주세요\n" + "categoryId : " + productDTO.getPrice());


        if (memberInfo != null) {
            productMapper.register(productDTO);
        } else {
            log.error("register ERROR! {}", productDTO);
            throw new RuntimeException("register ERROR! 상품 등록 메서드를 확인해주세요\n" + "Params : " + productDTO);
        }
    }

    @Override
    public List<ProductDTO> getMyProducts(int accountId) {
        List<ProductDTO> productDTOList = productMapper.selectMyProducts(accountId);
        return productDTOList;
    }

    @Override
    public void updateProducts(ProductDTO productDTO) {
        if (productDTO != null && productDTO.getId() != 0 && productDTO.getAccountId() != 0) {
            productMapper.updateProducts(productDTO);
        } else {
            log.error("updateProducts ERROR! {}", productDTO);
            throw new RuntimeException("updateProducts ERROR! 물품 변경 메서드를 확인해주세요\n" + "Params : " + productDTO);
        }
    }

    @Override
    public void deleteProduct(int accountId, int productId) {
        if (accountId != 0 && productId != 0) {
            productMapper.deleteProduct(accountId, productId);
        } else {
            log.error("deleteProudct ERROR! {}", productId);
            throw new RuntimeException("updateProducts ERROR! 물품 삭제 메서드를 확인해주세요\n" + "Params : " + productId);
        }
    }


}
