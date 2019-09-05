package wiki.pde.shixun.shopping.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wiki.pde.shixun.shopping.shoppingcart.model.User;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Collection<User> findAllByRole(String role);
}
