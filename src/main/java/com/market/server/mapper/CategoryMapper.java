package com.market.server.mapper;

import com.market.server.dto.CategoryDTO;

public interface CategoryMapper {
    public int register(CategoryDTO productDTO);

    public void updateCategory(CategoryDTO categoryDTO);

    public void deleteCategory(int categoryId);
}
