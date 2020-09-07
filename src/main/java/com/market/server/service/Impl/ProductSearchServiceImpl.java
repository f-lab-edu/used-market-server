package com.market.server.service.Impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.server.dto.CategoryDTO;
import com.market.server.dto.ProductDTO;
import com.market.server.mapper.ProductSearchMapper;
import com.market.server.service.ProductSearchService;
import com.market.server.utils.RedisKeyFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ProductSearchMapper productSearchMapper;
    private static final int DEFAULT_SEARCH_CATEGORY_ID = 1;
    private static final int DEFAULT_PRODUCT_CACHE_COUNT = 2000;
    private static final String DEFAULT_SEARCH_CATEGORY_NAME = CategoryDTO.SortStatus.CATEGORIES.toString();
    public static final String DEFAULT_PRODUCT_CACHE_KEY = "noLogin";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${expire.products}")
    private long productsExpireSecond;

    public ProductSearchServiceImpl(ProductSearchMapper productSearchMapper)
    {
        this.productSearchMapper = productSearchMapper;

    }

    // 상위 2000개 게시글을 redisTemplate에 push
    @PostConstruct
    public void init() {
        System.out.println("ProductSearchServiceImpl @PostConstruct init redisTemplate에 push");

        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id(DEFAULT_SEARCH_CATEGORY_ID)
                .name(DEFAULT_SEARCH_CATEGORY_NAME)
                .sortStatus(CategoryDTO.SortStatus.NEWEST)
                .searchCount(DEFAULT_PRODUCT_CACHE_COUNT)
                .pagingStartOffset(CategoryDTO.PAGING_OFFSET)
                .build();

        List<ProductDTO> productDTOList = productSearchMapper.selectProducts(ProductDTO.Status.NEW.toString(),categoryDTO);

        for (ProductDTO productDTO : productDTOList) {

            final String key = RedisKeyFactory.generateProductKey(DEFAULT_PRODUCT_CACHE_KEY);

            redisTemplate.watch(key); // 해당 키를 감시한다. 변경되면 로직 취소.

            try {
                if (redisTemplate.opsForList().size(key) >= 2000) {
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

    public void addRedisKeys(ProductDTO productDTO, String userId) {
        final String key = RedisKeyFactory.generateProductKey(userId);

        redisTemplate.watch(key); // 해당 키를 감시한다. 변경되면 로직 취소.

        try {
            if (redisTemplate.opsForList().size(key) >= 2000) {
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

    /**
     * 최상단 중고물품들을 조회한다.
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

    @Override
    public List<ProductDTO> getProducts(ProductDTO productDTO, CategoryDTO categoryDTO) {
        productDTO.setCategoryId(categoryDTO.getId());
        List<ProductDTO> productDTOList = productSearchMapper.selectProducts(productDTO.getStatus().toString(),categoryDTO);
        return productDTOList;
    }
}
