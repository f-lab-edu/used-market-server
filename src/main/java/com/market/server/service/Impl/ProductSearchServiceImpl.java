package com.market.server.service.Impl;

import com.market.server.dto.ProductDTO;
import com.market.server.mapper.ProductSearchMapper;
import com.market.server.service.ProductSearchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Log4j2
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductSearchMapper productSearchMapper;

    @Override
    public List<ProductDTO> getProducts(ProductDTO productDTO) {
        List<ProductDTO> productDTOList = productSearchMapper.selectProducts(productDTO);
        return productDTOList;
    }
}
