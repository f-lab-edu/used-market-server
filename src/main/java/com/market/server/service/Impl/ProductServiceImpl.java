package com.market.server.service.Impl;

import com.market.server.advice.exception.DuplicateIdException;
import com.market.server.dto.ProductDTO;
import com.market.server.dto.UserDTO;
import com.market.server.mapper.ProductMapper;
import com.market.server.mapper.UserProfileMapper;
import com.market.server.service.ProductService;
import com.market.server.utils.SHA256Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        productDTO.setWriterId(memberInfo.getAccountId());
        productDTO.setCreatetime(new Date());

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
}
