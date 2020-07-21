package com.market.server.controller;

import com.market.server.aop.LoginCheck;
import com.market.server.dto.CategoryDTO;
import com.market.server.service.Impl.CategoryServiceImpl;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"4. categories"})
@RestController
@RequestMapping("/categories")
@Log4j2
public class CategoryController {

    private CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 중고물품 카테고리 등록 메서드.
     */
    @PostMapping("categories")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void registerCategory(String accountId, @RequestBody CategoryDTO categoryDTO) {
        categoryService.register(accountId, categoryDTO);
    }

    /**
     * 중고물품 카테고리 수정 메서드.
     */
    @PatchMapping("{categoryId}/update")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void updateCategories(String accountId,
                               @PathVariable(name = "categoryId") int categoryId,
                               @RequestBody CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO = new CategoryDTO(categoryId,categoryRequest.getName());
        categoryService.update(categoryDTO);
    }

    /**
     * 본인 중고물품 카테고리 삭제 메서드.
     */
    @DeleteMapping("{categoryId}/delete")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void updateCategories(String accountId,
                               @PathVariable(name = "categoryId") int categoryId) {
        categoryService.delete(categoryId);
    }

    // -------------- request 객체 --------------

    @Setter
    @Getter
    private static class CategoryRequest {
        private int id;
        private String name;
    }

}
