package wiki.pde.shixun.shopping.shoppingcart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInOrder;
import wiki.pde.shixun.shopping.shoppingcart.model.User;
import wiki.pde.shixun.shopping.shoppingcart.repository.ProductInOrderRepository;
import wiki.pde.shixun.shopping.shoppingcart.service.ProductInOrderService;

import javax.transaction.Transactional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductInOrderServiceImpl implements ProductInOrderService {

    private final
    ProductInOrderRepository productInOrderRepository;

    public ProductInOrderServiceImpl(ProductInOrderRepository productInOrderRepository) {
        this.productInOrderRepository = productInOrderRepository;
    }

    @Override
    @Transactional
    public void update(String itemId, Integer quantity, User user) {
        var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCount(quantity);
            productInOrderRepository.save(productInOrder);
        });

    }

    @Override
    public ProductInOrder findOne(String itemId, User user) {
        var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        AtomicReference<ProductInOrder> res = new AtomicReference<>();
        op.ifPresent(res::set);
        return res.get();
    }
}
