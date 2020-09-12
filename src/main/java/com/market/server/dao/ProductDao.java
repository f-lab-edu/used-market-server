package com.market.server.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.server.dto.CategoryDTO;
import com.market.server.dto.ProductDTO;
import com.market.server.mapper.ProductSearchMapper;
import com.market.server.utils.RedisKeyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class ProductDao {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductSearchMapper productSearchMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${expire.products}")
    private long productsExpireSecond;

    // 상위 2000개 게시글을 redisTemplate에 push
    @PostConstruct
    public void init() {
        System.out.println("ProductSearchServiceImpl @PostConstruct init redisTemplate에 push");

        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id(ProductDTO.DEFAULT_SEARCH_CATEGORY_ID)
                .name(ProductDTO.DEFAULT_SEARCH_CATEGORY_NAME)
                .sortStatus(CategoryDTO.SortStatus.NEWEST)
                .searchCount(ProductDTO.DEFAULT_PRODUCT_CACHE_COUNT)
                .pagingStartOffset(CategoryDTO.PAGING_OFFSET)
                .build();

        List<ProductDTO> productDTOList = productSearchMapper.selectProducts(ProductDTO.Status.NEW.toString(),categoryDTO);

        for (ProductDTO productDTO : productDTOList) {

            final String key = RedisKeyFactory.generateProductKey(productDTO.DEFAULT_PRODUCT_SEARCH_CACHE_KEY);

            redisTemplate.watch(key); // 해당 키를 감시한다. 변경되면 로직 취소.

            try {
                if (redisTemplate.opsForList().size(key) > 2000) {
                    throw new IndexOutOfBoundsException("최상단 중고물품 2O00개 이상 담을 수 없습니다.");
                }

                redisTemplate.multi();
                redisTemplate.opsForList().rightPush(key, productDTO);
                redisTemplate.expire(key, productsExpireSecond, TimeUnit.SECONDS);

                redisTemplate.exec();
            } catch (Exception e) {
                redisTemplate.discard(); // 트랜잭션 종료시 unwatch()가 호출된다
                throw e;
            }
        }
    }

    // 스프링 컨테이너에서 객체인 빈을 제거하기 전에 redisTemplate에 push된 게시물들 삭제
    @PreDestroy
    public void destory(){
        redisTemplate.delete(RedisKeyFactory.generateProductKey(ProductDTO.DEFAULT_PRODUCT_SEARCH_CACHE_KEY));
    }

    /**
     * 최상단 중고물품들을 캐싱된 레디스에서 조회한다.
     * @author topojs8
     * @param useId 고객 아이디
     * @return
     */
    public List<ProductDTO> findAllProductsByCacheId(String useId) {
        List<ProductDTO> items = redisTemplate.opsForList()
                .range(RedisKeyFactory.generateProductKey(useId), 0, -1)
                .stream()
                .map(item -> objectMapper.convertValue(item, ProductDTO.class))
                .collect(Collectors.toList());
        return items;
    }

}
