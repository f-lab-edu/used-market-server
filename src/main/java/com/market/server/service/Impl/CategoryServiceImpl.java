package com.market.server.service.Impl;

import com.market.server.dto.CategoryDTO;
import com.market.server.mapper.CategoryMapper;
import com.market.server.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        if (accountId != null) {
            categoryMapper.register(categoryDTO); // 관리자 계정만 오는지 확인.
        } else {
            log.error("register ERROR! {}", categoryDTO);
            throw new RuntimeException("register ERROR! 상품 카테고리 등록 메서드를 확인해주세요\n" + "Params : " + categoryDTO);
        }

    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        if (categoryDTO != null) {
            categoryMapper.updateCategory(categoryDTO);
        } else {
            log.error("update ERROR! {}", categoryDTO);
            throw new RuntimeException("update ERROR! 물품 카테고리 변경 메서드를 확인해주세요\n" + "Params : " + categoryDTO);
        }
    }

    @Override
    public void delete(int categoryId) {
        if (categoryId != 0) {
            categoryMapper.deleteCategory(categoryId);
        } else {
            log.error("deleteCategory ERROR! {}", categoryId);
            throw new RuntimeException("deleteCategory ERROR! 물품 카테고리 삭제 메서드를 확인해주세요\n" + "Params : " + categoryId);
        }
    }
}
