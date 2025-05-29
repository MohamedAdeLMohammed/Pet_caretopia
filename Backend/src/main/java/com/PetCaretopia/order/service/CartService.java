    package com.PetCaretopia.order.service;



    import com.PetCaretopia.order.DTO.CartDTO;
    import com.PetCaretopia.order.DTO.CartItemDTO;
    import com.PetCaretopia.order.entity.Cart;
    import com.PetCaretopia.order.entity.CartItem;
    import com.PetCaretopia.order.entity.Product;
    import com.PetCaretopia.order.mapper.CartItemMapper;
    import com.PetCaretopia.order.mapper.CartMapper;
    import com.PetCaretopia.order.repository.CartItemRepository;
    import com.PetCaretopia.order.repository.CartRepository;
    import com.PetCaretopia.order.repository.ProductRepository;
    import com.PetCaretopia.user.entity.User;
    import com.PetCaretopia.user.repository.UserRepository;
    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.math.BigDecimal;
    import java.util.List;
    @Service
    public class CartService {

        @Autowired
        private CartRepository cartRepository;

        @Autowired
        private CartItemRepository cartItemRepository;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private CartMapper cartMapper;

        @Autowired
        private CartItemMapper cartItemMapper;

        public CartDTO getCart(Long userId) {
            User user = userRepository.findById(userId).orElseThrow();
            Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(user);
                return cartRepository.save(newCart);
            });
            return cartMapper.toDTO(cart);
        }

        public CartItemDTO addToCart(Long userId, Long productId, int quantity) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(user);
                return cartRepository.save(newCart);
            });

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPrice(product.getPrice());

            return cartItemMapper.toDTO(cartItemRepository.save(item));
        }


        public List<CartItemDTO> getCartItems(Long userId) {
            Cart cart = cartRepository.findByUser(userRepository.findById(userId).orElseThrow()).orElseThrow();
            return cartItemRepository.findByCart(cart).stream().map(cartItemMapper::toDTO).toList();
        }

        @Transactional
        public void clearCart(Long userId) {
            Cart cart = cartRepository.findByUser(userRepository.findById(userId).orElseThrow()).orElseThrow();
            cartItemRepository.deleteByCart(cart);
        }

        public CartItemDTO updateCartItem(Long userId, Long productId, int quantity) {
            Cart cart = cartRepository.findByUser(userRepository.findById(userId).orElseThrow()).orElseThrow();
            List<CartItem> items = cartItemRepository.findByCart(cart);
            for (CartItem item : items) {
                if (item.getProduct().getId().equals(productId)) {
                    item.setQuantity(quantity);
                    return cartItemMapper.toDTO(cartItemRepository.save(item));
                }
            }
            throw new RuntimeException("Cart item not found");
        }

        public void removeCartItem(Long userId, Long productId) {
            Cart cart = cartRepository.findByUser(userRepository.findById(userId).orElseThrow()).orElseThrow();
            cartItemRepository.findByCart(cart).stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .ifPresentOrElse(cartItemRepository::delete, () -> {
                        throw new RuntimeException("Cart item not found");
                    });
        }

        public int getCartItemCount(Long userId) {
            Cart cart = cartRepository.findByUser(userRepository.findById(userId).orElseThrow()).orElseThrow();
            return cartItemRepository.findByCart(cart).stream().mapToInt(CartItem::getQuantity).sum();
        }

        public BigDecimal getCartTotal(Long userId) {
            Cart cart = cartRepository.findByUser(userRepository.findById(userId).orElseThrow()).orElseThrow();
            return cartItemRepository.findByCart(cart).stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public CartItemDTO getCartItemById(Long itemId) {
            return cartItemMapper.toDTO(cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found")));
        }

        public List<CartItemDTO> getAllCartItems() {
            return cartItemRepository.findAll().stream().map(cartItemMapper::toDTO).toList();
        }

        public void deleteCartItemById(Long itemId) {
            cartItemRepository.deleteById(itemId);
        }
    }
