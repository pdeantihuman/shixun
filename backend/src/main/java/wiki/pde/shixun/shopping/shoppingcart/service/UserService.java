package wiki.pde.shixun.shopping.shoppingcart.service;

import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.model.User;

import java.util.Collection;

public interface UserService {
    User findOne(String email);

    Collection<User> findByRole(String role);

    User save(User user);

    User update(User user);
}
