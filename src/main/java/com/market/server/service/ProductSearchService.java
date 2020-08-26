package com.market.server.service;

import com.market.server.dto.CategoryDTO;
import com.market.server.dto.ProductDTO;

import java.util.List;

public interface ProductSearchService {
    List<ProductDTO> getProducts(ProductDTO productDTO, CategoryDTO categoryDTO);
}
