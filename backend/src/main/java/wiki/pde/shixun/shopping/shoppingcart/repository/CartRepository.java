package wiki.pde.shixun.shopping.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wiki.pde.shixun.shopping.shoppingcart.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}
