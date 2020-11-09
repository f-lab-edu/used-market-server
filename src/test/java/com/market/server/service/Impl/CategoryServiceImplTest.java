package com.market.server.service.Impl;

import com.market.server.dao.ProductDao;
import com.market.server.dto.CategoryDTO;
import com.market.server.dto.ProductDTO;
import com.market.server.mapper.CategoryMapper;
import com.market.server.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
class CategoryServiceImplTest {

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Mock
    CategoryMapper categoryMapper;

    // 새로운 카테고리 객체를 생성하여 반환한다.
    public CategoryDTO generateCategory() {
        MockitoAnnotations.initMocks(this); // mock all the field having @Mock annotation
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id(1)
                .name("testName")
                .sortStatus(CategoryDTO.SortStatus.NEWEST)
                .searchCount(1000)
                .pagingStartOffset(0)
                .build();
        return categoryDTO;
    }

    @Test
    void register() {
        CategoryDTO categoryDTO = generateCategory();
        given(categoryMapper.register(categoryDTO)).willReturn(categoryDTO.getId());
        categoryMapper.register(categoryDTO);
    }

    @Test
    void update() {
        CategoryDTO categoryDTO = generateCategory();
        categoryDTO.setName("testName2");
        categoryMapper.updateCategory(categoryDTO);
        assertEquals(categoryDTO.getName(),("testName2"));
    }

    @Test
    void delete() {
        CategoryDTO categoryDTO = generateCategory();
        categoryDTO.setId(1);
        categoryMapper.deleteCategory(1);
    }
}