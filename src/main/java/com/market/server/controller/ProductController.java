package com.market.server.controller;

import com.market.server.dto.ProductDTO;
import com.market.server.service.Impl.ProductServiceImpl;
import com.market.server.utils.SessionUtil;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Api(tags = {"3. products"})
@RestController
@RequestMapping("/products")
@Log4j2
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

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
}
