package wiki.pde.shixun.shopping.shoppingcart.response;

import lombok.Data;
import org.springframework.data.domain.Page;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInfo;

@Data
public class CategoryPage {
    private String category;
    private Page<ProductInfo> page;
    public CategoryPage(String category, Page<ProductInfo> page) {
        this.category = category;
        this.page = page;
    }
}
