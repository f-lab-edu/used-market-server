package com.market.server.service;

import com.market.server.dto.ProductDTO;

public interface ProductService {

    // 중고 물품 등록
    void register(String id, ProductDTO productDTO);
}
