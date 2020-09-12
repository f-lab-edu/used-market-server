package com.market.server.mapper;

import com.market.server.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    public int register(ProductDTO productDTO);

    public List<ProductDTO> selectMyProducts(int accountId);

    public void updateProducts(ProductDTO productDTO);

    public void deleteProduct(int accountId, int productId);

    public int getLastProductId();
}
