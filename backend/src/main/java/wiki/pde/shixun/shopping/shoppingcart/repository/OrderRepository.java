package wiki.pde.shixun.shopping.shoppingcart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wiki.pde.shixun.shopping.shoppingcart.model.OrderMain;

@Repository
public interface OrderRepository extends JpaRepository<OrderMain, Integer> {
    OrderMain findByOrderId(Long orderId);

    Page<OrderMain> findAllByOrderStatusOrderByCreateTimeDesc(Integer orderStatus, Pageable pageable);

    /**
     * 获取当前用户的订单列表
     * @param buyerEmail 购买者的email
     * @param pageable 分页参数
     * @return 当前页的订单列表
     */
    Page<OrderMain> findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(String buyerEmail, Pageable pageable);

    /**
     *
     * @param pageable 分页参数
     * @return 接口
     */
    Page<OrderMain> findAllByOrderByOrderStatusAscCreateTimeDesc(Pageable pageable);

    Page<OrderMain> findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(String buyerPhone, Pageable pageable);
}
