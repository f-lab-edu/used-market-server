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
        registerProductsInRedisTenplate(ProductDTO.DEFAULT_PRODUCT_SEARCH_CACHE_KEY);
    }

    // 스프링 컨테이너에서 객체인 빈을 제거하기 전에 DEFAULT_PRODUCT_SEARCH_CACHE_KEY로 redisTemplate에 push된 게시물들 삭제
    @PreDestroy
    public void destory() {
        deleteProductsInRedisTenplate(ProductDTO.DEFAULT_PRODUCT_SEARCH_CACHE_KEY);
    }

    public void registerProductsInRedisTenplate(String userId){
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id(ProductDTO.DEFAULT_SEARCH_CATEGORY_ID)
                .name(ProductDTO.DEFAULT_SEARCH_CATEGORY_NAME)
                .sortStatus(CategoryDTO.SortStatus.OLDEST)
                .searchCount(ProductDTO.DEFAULT_PRODUCT_CACHE_COUNT)
                .pagingStartOffset(CategoryDTO.PAGING_OFFSET)
                .build();

        List<ProductDTO> productDTOList = productSearchMapper.selectProducts(ProductDTO.Status.NEW.toString(), categoryDTO);

        if (userId == null)
            userId = ProductDTO.DEFAULT_PRODUCT_SEARCH_CACHE_KEY;

        for (ProductDTO productDTO : productDTOList) {
            addProduct(productDTO, userId);
        }
    }

    public void deleteProductsInRedisTenplate(String userId){
        redisTemplate.delete(RedisKeyFactory.generateProductKey(userId));
    }

    //삭제 하려는 index 가 redis key 중에서 몇번째 인지 return
    public int selectProductsIndex(int index){
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id(ProductDTO.DEFAULT_SEARCH_CATEGORY_ID)
                .name(ProductDTO.DEFAULT_SEARCH_CATEGORY_NAME)
                .sortStatus(CategoryDTO.SortStatus.OLDEST)
                .searchCount(ProductDTO.DEFAULT_PRODUCT_CACHE_COUNT)
                .pagingStartOffset(CategoryDTO.PAGING_OFFSET)
                .build();

        return productSearchMapper.getProductsIndex(ProductDTO.Status.NEW.toString(), categoryDTO, index);
    }

    /**
     * 최상단 중고 물품들을 캐싱된 레디스 에서 조회 한다.
     *
     * @param productId 물품 아이디
     * @return
     * @author topojs8
     */
    public List<ProductDTO> findAllProductsByCacheId(String productId) {
        List<ProductDTO> items = redisTemplate.opsForList()
                .range(RedisKeyFactory.generateProductKey(productId), 0, -1)
                .stream()
                .map(item -> objectMapper.convertValue(item, ProductDTO.class))
                .collect(Collectors.toList());
        return items;
    }

    /**
     * redis list에 해당 물품을 추가한다.
     * RedisKeyFactory로 물품 아이디, 내부 키를 이용해 키를 생산한 후 물품을 저장시킨다.
     *
     * @param productDTO 레디스 리스트에 추가할 중고 물품
     * @param productId      물품 아이디
     * @return
     * @author topojs8
     */
    public void addProduct(ProductDTO productDTO, String productId) {
        final String key = RedisKeyFactory.generateProductKey(productId);

        redisTemplate.watch(key); // 해당 키를 감시한다. 변경되면 로직 취소.

        try {
            if (redisTemplate.opsForList().size(key) > 2000) {
                throw new IndexOutOfBoundsException("최상단 중고 물품 2O00개 이상 담을 수 없습니다.");
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
     * 물품 레디스 리스트 에서 해당 인덱스에 해당하는 물품을 삭제한다.
     *
     * @param productId 물품 아이디
     * @param index 삭제할 메뉴 인덱스
     * @return 삭제에 성공할 시 true
     * @author topojs8
     */
    public boolean deleteByProductIdAndIndex(String productId, long index) {
        /*
         * opsForList().remove(key, count, value)
         * key : list를 조회할 수 있는 key
         * count > 0이면 head부터 순차적으로 조회하며 count의 절대값에 해당하는 개수만큼 제거
         * count < 0이면 tail부터 순차적으로 조회하며 count의 절대값에 해당하는 개수만큼 제거
         * count = 0이면 모두 조회한 후 value에 해당하는 값 모두 제거
         * value : 주어진 값과 같은 value를 가지는 대상이 삭제 대상이 된다
         * return값으로는 삭제한 인자의 개수를 리턴한다.
         *
         * 해당 리스트에서 인덱스에 해당하는 값을 조회한 후, remove의 value값 인자로 넘겨준다.
         * 그 후 count에 1 값을 주면 head부터 순차적으로 조회하며 index에 해당하는 값을 제거할것이다.
         * return값이 1이면 1개를 삭제한 것이니 성공, 1이 아니라면 잘 삭제된것이 아니니 실패이다.
         */
        Long remove = redisTemplate.opsForList().remove(RedisKeyFactory.generateProductKey(productId), 1,
                redisTemplate.opsForList().index(RedisKeyFactory.generateProductKey(productId), index));
        return remove == 1;
    }

}
