package com.market.server.service.Impl;

import com.market.server.dto.CategoryDTO;
import com.market.server.dto.ProductDTO;
import com.market.server.mapper.ProductSearchMapper;
import com.market.server.service.ProductSearchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Log4j2
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductSearchMapper productSearchMapper;

    @Cacheable(value="getProducts")
    @Override
    public List<ProductDTO> getProducts(ProductDTO productDTO, CategoryDTO categoryDTO) {
        productDTO.setCategoryId(categoryDTO.getId());
        List<ProductDTO> productDTOList = productSearchMapper.selectProducts(productDTO,categoryDTO);
        return productDTOList;
    }
}
