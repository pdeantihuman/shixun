package wiki.pde.shixun.shopping.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductCategory;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    // Some category
    List<ProductCategory> findByCategoryTypeInOrderByCategoryTypeAsc(List<Integer> categoryTypes);
    // All category
    List<ProductCategory> findAllByOrderByCategoryType();
    // One category
    ProductCategory findByCategoryType(Integer categoryType);

    /**
     * 根绝商品分类类型获取所有的商品分类
     * @param categoryType 商品分类类型
     * @return 商品分类
     */
    List<ProductCategory> findAllByCategoryType(Integer categoryType);
}
