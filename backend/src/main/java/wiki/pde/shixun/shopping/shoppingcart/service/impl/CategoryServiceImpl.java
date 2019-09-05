package wiki.pde.shixun.shopping.shoppingcart.service.impl;

import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductCategory;
import wiki.pde.shixun.shopping.shoppingcart.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<ProductCategory> findAll() {
        return null;
    }

    @Override
    public ProductCategory findByCategoryType(Integer categoryType) {
        return null;
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return null;
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return null;
    }
}
