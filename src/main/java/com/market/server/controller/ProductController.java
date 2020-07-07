package com.market.server.controller;

import com.market.server.dto.ProductDTO;
import com.market.server.dto.UserDTO;
import com.market.server.service.Impl.ProductServiceImpl;
import com.market.server.service.Impl.UserServiceImpl;
import com.market.server.utils.SessionUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Api(tags = {"3. products"})
@RestController
@RequestMapping("/products")
@Log4j2
public class ProductController {

    @Autowired
    private final ProductServiceImpl productService;
    @Autowired
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
    public void registerProduct(@RequestBody ProductDTO productDTO,
                                HttpSession session) {
        String Id = SessionUtil.getLoginMemberId(session);
        productService.register(Id, productDTO);
    }

    /**
     * 본인 중고물품 검색 메서드.
     */
    @GetMapping("selectMyProducts")
    public ProductResponse myProductInfo(HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        List<ProductDTO> productDTOList = productService.selectMyProducts(memberInfo.getAccountId());
        return new ProductResponse(productDTOList);
    }



    // -------------- response 객체 --------------

    @Getter
    @AllArgsConstructor
    private static class ProductResponse {
        private List<ProductDTO> productDTO;
    }
}
