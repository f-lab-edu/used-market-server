package com.market.server.service.Impl;
import com.market.server.dao.ProductDao;
import com.market.server.dto.CategoryDTO;
import com.market.server.dto.ProductDTO;
import com.market.server.mapper.ProductSearchMapper;
import com.market.server.service.ProductSearchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Log4j2
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ProductSearchMapper productSearchMapper;

    private final ProductDao productDao;

    public ProductSearchServiceImpl(ProductSearchMapper productSearchMapper, ProductDao productDao)
    {
        this.productSearchMapper = productSearchMapper;
        this.productDao = productDao;
    }

    public List<ProductDTO> findAllProductsByCacheId(String useId) {
        return productDao.findAllProductsByCacheId(useId);
    }

    @Override
    public List<ProductDTO> getProducts(ProductDTO productDTO, CategoryDTO categoryDTO) {
        productDTO.setCategoryId(categoryDTO.getId());
        List<ProductDTO> productDTOList = productSearchMapper.selectProducts(productDTO.getStatus().toString(),categoryDTO);
        return productDTOList;
    }
}
