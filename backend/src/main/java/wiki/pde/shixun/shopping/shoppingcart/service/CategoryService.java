package wiki.pde.shixun.shopping.shoppingcart.service;

import wiki.pde.shixun.shopping.shoppingcart.model.ProductCategory;

import java.util.List;

public interface CategoryService {
    List<ProductCategory> findAll();

    ProductCategory findByCategoryType(Integer categoryType);

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
