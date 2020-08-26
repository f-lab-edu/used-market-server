package com.market.server.controller;

import com.market.server.aop.LoginCheck;
import com.market.server.dto.ProductDTO;
import com.market.server.dto.UserDTO;
import com.market.server.service.Impl.ProductServiceImpl;
import com.market.server.service.Impl.UserServiceImpl;
import com.market.server.utils.SessionUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Api(tags = {"3. products"})
@RestController
@RequestMapping("/products")
@Log4j2
public class ProductController {

    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private ProductResponse productResponse;

    public ProductController(ProductServiceImpl productService, UserServiceImpl userService) {
        this.productService = productService;
        this.userService = userService;
    }

    /**
     * 중고물품 등록 메서드.
     */
    @PostMapping("insertProduct")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public void registerProduct(@RequestBody ProductDTO productDTO, String accountId) {
        productService.register(accountId, productDTO);
    }

    /**
     * 본인 중고물품 검색 메서드.
     */
    @GetMapping("MyProducts")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ProductResponse myProductInfo(String accountId) {
        UserDTO memberInfo = userService.getUserInfo(accountId);
        List<ProductDTO> productDTOList = productService.getMyProducts(memberInfo.getAccountId());
        return new ProductResponse(productDTOList);
    }

    /**
     * 본인 중고물품 수정 메서드.
     */
    @PatchMapping("{productId}")
    public void updateProducts(@PathVariable(name = "productId") int productId,
                               @RequestBody ProductRequest PR,
                               HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);

        UserDTO memberInfo = userService.getUserInfo(id);
        ProductDTO productDTO = new ProductDTO(productId,
                PR.getPrice(),
                memberInfo.getAccountId(),
                PR.getTitle(),
                PR.getContents(),
                PR.getStatus(),
                PR.isTrade(),
                new Date(),
                new Date(),
                PR.getDeliveryprice(),
                PR.getDibcount());

        productService.updateProducts(productDTO);
    }

    /**
     * 본인 중고물품 삭제 메서드.
     */
    @DeleteMapping("{productId}")
    public void updateProducts(@PathVariable(name = "productId") int productId,
                               @RequestBody ProductDeleteRequest productDeleteRequest,
                               HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        productService.deleteProduct(memberInfo.getAccountId(), productId);
    }

    // -------------- response 객체 --------------

    @Getter
    @AllArgsConstructor
    private static class ProductResponse {
        private List<ProductDTO> productDTO;
    }

    // -------------- request 객체 --------------

    @Setter
    @Getter
    private static class ProductRequest {
        private long price;
        private String title;
        private String contents;
        private ProductDTO.Status status;
        private boolean trade;
        private Date updatetime;
        private long deliveryprice;
        private int dibcount;
    }

    @Setter
    @Getter
    private static class ProductDeleteRequest {
        private int id;
        private int accountId;
    }
}
