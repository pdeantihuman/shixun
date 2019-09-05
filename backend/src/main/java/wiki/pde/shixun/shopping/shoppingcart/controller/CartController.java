package wiki.pde.shixun.shopping.shoppingcart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wiki.pde.shixun.shopping.shoppingcart.form.ItemForm;
import wiki.pde.shixun.shopping.shoppingcart.model.Cart;
import wiki.pde.shixun.shopping.shoppingcart.model.ProductInOrder;
import wiki.pde.shixun.shopping.shoppingcart.model.User;
import wiki.pde.shixun.shopping.shoppingcart.repository.ProductInOrderRepository;
import wiki.pde.shixun.shopping.shoppingcart.service.CartService;
import wiki.pde.shixun.shopping.shoppingcart.service.ProductInOrderService;
import wiki.pde.shixun.shopping.shoppingcart.service.ProductService;
import wiki.pde.shixun.shopping.shoppingcart.service.UserService;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {
    private final
    CartService cartService;
    private final
    UserService userService;
    private final
    ProductService productService;
    private final
    ProductInOrderService productInOrderService;

    public CartController(CartService cartService, UserService userService, ProductService productService,
                          ProductInOrderService productInOrderService,
                          ProductInOrderRepository productInOrderRepository) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
        this.productInOrderService = productInOrderService;
    }

    @PostMapping("")
    public ResponseEntity<Cart> mergeCart(@RequestBody Collection<ProductInOrder> productInOrders, Principal principal) {
        User user = userService.findOne(principal.getName());
        try {
            cartService.mergeLocalCart(productInOrders, user);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("Merge Cart Failed");
        }
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @GetMapping("")
    public Cart getCart(Principal principal) {
        User user = userService.findOne(principal.getName());
        return cartService.getCart(user);
    }


    @PostMapping("/add")
    public boolean addToCart(@RequestBody ItemForm form, Principal principal) {
        var productInfo = productService.findOne(form.getProductId());
        try {
            mergeCart(Collections.singleton(new ProductInOrder(productInfo, form.getQuantity())), principal);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @PutMapping("/{itemId}")
    public ProductInOrder modifyItem(@PathVariable("itemId") String itemId, @RequestBody Integer quantity, Principal principal) {
        User user = userService.findOne(principal.getName());
        productInOrderService.update(itemId, quantity, user);
        return productInOrderService.findOne(itemId, user);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") String itemId, Principal principal) {
        User user = userService.findOne(principal.getName());
        cartService.delete(itemId, user);
        // flush memory into DB
    }


    @PostMapping("/checkout")
    public ResponseEntity checkout(Principal principal) {
        User user;// Email as username
        user = userService.findOne(principal.getName());
        cartService.checkout(user);
        return ResponseEntity.ok(null);
    }


}
