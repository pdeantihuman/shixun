package wiki.pde.shixun.shopping.shoppingcart.service;

import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInOrder;
import wiki.pde.shixun.shopping.shoppingcart.model.User;

public interface ProductInOrderService {
    void update(String itemId, Integer quantity, User user);

    ProductInOrder findOne(String itemId, User user);
}
