package com.market.server.mapper;

import com.market.server.dto.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    public int register(ProductDTO productDTO);
}
