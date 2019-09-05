package wiki.pde.shixun.shopping.shoppingcart.service;

import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.model.Cart;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInOrder;
import wiki.pde.shixun.shopping.shoppingcart.model.User;

import java.util.Collection;

public interface CartService {
    Cart getCart(User user);

    void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user);

    void delete(String itemId, User user);

    void checkout(User user);
}
