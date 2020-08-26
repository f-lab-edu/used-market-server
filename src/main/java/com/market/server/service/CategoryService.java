package com.market.server.service;

import com.market.server.dto.CategoryDTO;

public interface CategoryService {

    // 중고 물품 카테고리 등록
    void register(String accountId, CategoryDTO categoryDTO);
}
