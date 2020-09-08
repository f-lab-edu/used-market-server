package com.market.server.controller;
import com.market.server.dto.CategoryDTO;
import com.market.server.dto.ProductDTO;
import com.market.server.service.Impl.ProductSearchServiceImpl;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"5. search"})
@RestController
@RequestMapping("/search")
@Log4j2
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchServiceImpl productSearchService;

    /**
     * 중고물품 검색 메서드.
     * 초기 bean 등록시 2000개의 최신 중고물품 캐싱해온 데이터를 검색
     * @params date 물품 생성 날짜,
     *         price 물품 가격,
     *         accountId 계정 번호,
     *         title 물품 제목,
     *         status 상품 상태,
     *         categoryId 물품 카테고리 번호
     * ORDER BY 카테고리별
     *          판매순
     *          최신순
     *          낮은가격순
     *          높은 가격순
     *          평점순
     * @return ProductSearchResponse
     *
     * @author topojs8
     */
    @GetMapping
    public ProductSearchResponse search(ProductDTO productDTO,CategoryDTO categoryDTO) {
        String accountId = ProductDTO.DEFAULT_PRODUCT_SEARCH_CACHE_KEY;
        List<ProductDTO> productDTOList = productSearchService.findAllProductsByCacheId(accountId);

        if(productDTOList.size() == 0)
            productDTOList = productSearchService.getProducts(productDTO,categoryDTO);

        return new ProductSearchResponse(productDTOList);
    }

    // -------------- response 객체 --------------

    @Getter
    @AllArgsConstructor
    private static class ProductSearchResponse {
        private List<ProductDTO> productDTO;
    }
}
