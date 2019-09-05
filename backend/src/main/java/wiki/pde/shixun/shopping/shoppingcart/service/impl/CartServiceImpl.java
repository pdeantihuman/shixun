package wiki.pde.shixun.shopping.shoppingcart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.model.Cart;
import wiki.pde.shixun.shopping.shoppingcart.model.OrderMain;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInOrder;
import wiki.pde.shixun.shopping.shoppingcart.model.User;
import wiki.pde.shixun.shopping.shoppingcart.repository.CartRepository;
import wiki.pde.shixun.shopping.shoppingcart.repository.OrderRepository;
import wiki.pde.shixun.shopping.shoppingcart.repository.ProductInOrderRepository;
import wiki.pde.shixun.shopping.shoppingcart.repository.UserRepository;
import wiki.pde.shixun.shopping.shoppingcart.service.CartService;
import wiki.pde.shixun.shopping.shoppingcart.service.ProductService;
import wiki.pde.shixun.shopping.shoppingcart.service.UserService;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    private final
    ProductService productService;
    private final
    OrderRepository orderRepository;

    private final
    ProductInOrderRepository productInOrderRepository;
    private final
    CartRepository cartRepository;

    public CartServiceImpl(ProductService productService, OrderRepository orderRepository, UserRepository userRepository, ProductInOrderRepository productInOrderRepository, CartRepository cartRepository, UserService userService) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.productInOrderRepository = productInOrderRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart getCart(User user) {
        return user.getCart();
    }

    @Override
    @Transactional
    public void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user) {
        Cart finalCart = user.getCart();
        productInOrders.forEach(productInOrder -> {
            Set<ProductInOrder> set = finalCart.getProducts();
            Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductId().equals(productInOrder.getProductId())).findFirst();
            ProductInOrder prod;
            if (old.isPresent()) {
                prod = old.get();
                prod.setCount(productInOrder.getCount() + prod.getCount());
            } else {
                prod = productInOrder;
                prod.setCart(finalCart);
                finalCart.getProducts().add(prod);
            }
            productInOrderRepository.save(prod);
        });
        cartRepository.save(finalCart);

    }

    @Override
    @Transactional
    public void delete(String itemId, User user) {
        var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCart(null);
            productInOrderRepository.deleteById(productInOrder.getId());
        });
    }


    @Override
    @Transactional
    public void checkout(User user) {
        // Creat an order
        OrderMain order = new OrderMain(user);
        orderRepository.save(order);

        // clear cart's foreign key & set order's foreign key& decrease stock
        user.getCart().getProducts().forEach(productInOrder -> {
            productInOrder.setCart(null);
            productInOrder.setOrderMain(order);
            productService.decreaseStock(productInOrder.getProductId(), productInOrder.getCount());
            productInOrderRepository.save(productInOrder);
        });

    }
}