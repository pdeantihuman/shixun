package wiki.pde.shixun.shopping.shoppingcart.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductCategory;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInfo;
import wiki.pde.shixun.shopping.shoppingcart.response.CategoryPage;
import wiki.pde.shixun.shopping.shoppingcart.service.CategoryService;
import wiki.pde.shixun.shopping.shoppingcart.service.ProductService;
import java.util.List;

/**
 * @author john
 */
@RestController
@CrossOrigin
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }


    /**
     * Show products in category
     *
     * @param categoryType 分类
     * @param page 当前页
     * @param size 每页的长度
     * @return 当前页的产品列表
     */
    @GetMapping("/category/{type}")
    public CategoryPage showOne(@PathVariable("type") Integer categoryType,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "3") Integer size) {
        List<ProductCategory> cat = categoryService.findAllByCategoryType(categoryType);
        PageRequest request = PageRequest.of(page - 1, size);
        Page<ProductInfo> productInCategory = productService.findAllInCategory(categoryType, request);
        var tmp = new CategoryPage("", productInCategory);
        if (cat.isEmpty()) {
            return tmp;
        }
        tmp.setCategory(cat.get(0).getCategoryName());
        return tmp;
    }
}
