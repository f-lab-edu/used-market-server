package com.market.server.service.Impl;

import com.market.server.dto.CategoryDTO;
import com.market.server.mapper.CategoryMapper;
import com.market.server.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    private static Logger logger = LogManager.getLogger(CategoryServiceImpl.class);

    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {

        if (accountId != null) {
            categoryMapper.register(categoryDTO); // 관리자 계정만 오는지 확인.
        } else {
            logger.error("register ERROR! {}", categoryDTO);
            throw new RuntimeException("register ERROR! 상품 카테고리 등록 메서드를 확인해주세요\n" + "Params : " + categoryDTO);
        }

    }
}
