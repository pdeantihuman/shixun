package wiki.pde.shixun.shopping.shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductCategory;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInfo;
import wiki.pde.shixun.shopping.shoppingcart.response.CategoryPage;
import wiki.pde.shixun.shopping.shoppingcart.service.CategoryService;
import wiki.pde.shixun.shopping.shoppingcart.service.ProductService;

@RestController
@CrossOrigin
public class CategoryController {
    private final
    CategoryService categoryService;
    private final
    ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }


    /**
     * Show products in category
     *
     * @param categoryType
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/category/{type}")
    public CategoryPage showOne(@PathVariable("type") Integer categoryType,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "3") Integer size) {

        ProductCategory cat = categoryService.findByCategoryType(categoryType);
        PageRequest request = PageRequest.of(page - 1, size);
        Page<ProductInfo> productInCategory = productService.findAllInCategory(categoryType, request);
        var tmp = new CategoryPage("", productInCategory);
        tmp.setCategory(cat.getCategoryName());
        return tmp;
    }
}
