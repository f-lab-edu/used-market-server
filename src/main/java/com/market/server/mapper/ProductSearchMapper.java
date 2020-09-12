package com.market.server.mapper;

import com.market.server.dto.CategoryDTO;
import com.market.server.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProductSearchMapper {
    public List<ProductDTO> selectProducts(String productStatus, CategoryDTO categoryDTO);

    public int getProductsIndex(String productStatus, CategoryDTO categoryDTO, int productId);
}
