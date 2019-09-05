package wiki.pde.shixun.shopping.shoppingcart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.enums.OrderStatusEnum;
import wiki.pde.shixun.shopping.shoppingcart.enums.ResultEnum;
import wiki.pde.shixun.shopping.shoppingcart.exception.MyException;
import wiki.pde.shixun.shopping.shoppingcart.model.OrderMain;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInOrder;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInfo;
import wiki.pde.shixun.shopping.shoppingcart.repository.OrderRepository;
import wiki.pde.shixun.shopping.shoppingcart.repository.ProductInOrderRepository;
import wiki.pde.shixun.shopping.shoppingcart.repository.ProductInfoRepository;
import wiki.pde.shixun.shopping.shoppingcart.repository.UserRepository;
import wiki.pde.shixun.shopping.shoppingcart.service.OrderService;
import wiki.pde.shixun.shopping.shoppingcart.service.ProductService;

import javax.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    private final
    OrderRepository orderRepository;
    private final
    ProductInfoRepository productInfoRepository;
    private final
    ProductService productService;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductInfoRepository productInfoRepository, ProductService productService, ProductInOrderRepository productInOrderRepository) {
        this.orderRepository = orderRepository;
        this.productInfoRepository = productInfoRepository;
        this.productService = productService;
    }

    @Override
    public Page<OrderMain> findAll(Pageable pageable) {
        return orderRepository.findAllByOrderByOrderStatusAscCreateTimeDesc(pageable);
    }

    @Override
    public Page<OrderMain> findByStatus(Integer status, Pageable pageable) {
        return orderRepository.findAllByOrderStatusOrderByCreateTimeDesc(status, pageable);
    }

    @Override
    public Page<OrderMain> findByBuyerEmail(String email, Pageable pageable) {
        return orderRepository.findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(email, pageable);
    }

    @Override
    public Page<OrderMain> findByBuyerPhone(String phone, Pageable pageable) {
        return orderRepository.findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(phone, pageable);
    }

    @Override
    public OrderMain findOne(Long orderId) {
        OrderMain orderMain = orderRepository.findByOrderId(orderId);
        if(orderMain == null) {
            throw new MyException(ResultEnum.ORDER_NOT_FOUND);
        }
        return orderMain;
    }

    @Override
    @Transactional
    public OrderMain finish(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderRepository.save(orderMain);
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public OrderMain cancel(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderRepository.save(orderMain);

        // Restore Stock
        Iterable<ProductInOrder> products = orderMain.getProducts();
        for(ProductInOrder productInOrder : products) {
            ProductInfo productInfo = productInfoRepository.findByProductId(productInOrder.getProductId());
            if(productInfo != null) {
                productService.increaseStock(productInOrder.getProductId(), productInOrder.getCount());
            }
        }
        return orderRepository.findByOrderId(orderId);

    }
}