package wiki.pde.shixun.shopping.shoppingcart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wiki.pde.shixun.shopping.shoppingcart.enums.ResultEnum;
import wiki.pde.shixun.shopping.shoppingcart.exception.MyException;
import wiki.pde.shixun.shopping.shoppingcart.model.Cart;
import wiki.pde.shixun.shopping.shoppingcart.model.User;
import wiki.pde.shixun.shopping.shoppingcart.repository.CartRepository;
import wiki.pde.shixun.shopping.shoppingcart.repository.UserRepository;
import wiki.pde.shixun.shopping.shoppingcart.service.UserService;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@DependsOn("passwordEncoder")
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final
    UserRepository userRepository;
    private final
    CartRepository cartRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, CartRepository cartRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public User findOne(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Collection<User> findByRole(String role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    @Transactional
    public User save(User user) {
        //register
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User savedUser = userRepository.save(user);

            // initial Cart
            Cart savedCart = cartRepository.save(new Cart(savedUser));
            savedUser.setCart(savedCart);
            return userRepository.save(savedUser);

        } catch (Exception e) {
            throw new MyException(ResultEnum.VALID_ERROR);
        }

    }

    @Override
    @Transactional
    public User update(User user) {
        User oldUser = userRepository.findByEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setName(user.getName());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());
        return userRepository.save(oldUser);
    }
}
