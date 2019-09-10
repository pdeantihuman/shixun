package wiki.pde.shixun.shopping.shoppingcart.service.impl;

import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.enums.ResultEnum;
import wiki.pde.shixun.shopping.shoppingcart.exception.MyException;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductCategory;
import wiki.pde.shixun.shopping.shoppingcart.repository.ProductCategoryRepository;
import wiki.pde.shixun.shopping.shoppingcart.service.CategoryService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public CategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }


    @Override
    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAllByOrderByCategoryType();
    }

    @Override
    public void findByCategoryType(Integer categoryType) {
        ProductCategory res = productCategoryRepository.findByCategoryType(categoryType);
        if(res == null) {
            throw new MyException(ResultEnum.CATEGORY_NOT_FOUND);
        }
    }

    @Override
    public List<ProductCategory> findAllByCategoryType(Integer categoryType) {
        List<ProductCategory> allByCategoryType = productCategoryRepository.findAllByCategoryType(categoryType);
        if (allByCategoryType.isEmpty()){
            throw new MyException(ResultEnum.CATEGORY_NOT_FOUND);
        }
        return allByCategoryType;
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return productCategoryRepository.findByCategoryTypeInOrderByCategoryTypeAsc(categoryTypeList);
    }

    @Override
    @Transactional
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

}
