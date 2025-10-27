package in.com.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import in.com.main.entities.Cart;
import in.com.main.entities.LoginAndRegistration;
import in.com.main.entities.Products;
import in.com.main.repositary.CartRepo;
import in.com.main.repositary.LoginAndRegistartionRepositary;
import in.com.main.repositary.ProductRepositary;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private ProductRepositary productRepo;
	
	@Autowired
	private LoginAndRegistartionRepositary userRepo;
	
	@Override
	public Cart saveCart(Integer productId, Integer userId) {
	 LoginAndRegistration userDtls = userRepo.findById(userId).get();
	Products productDtls = productRepo.findById(productId).get();
	
	Cart cartStatus = cartRepo.findByProductIdAndUserId(productId, userId);
	
	Cart cart = null;
	
	if(ObjectUtils.isEmpty(cartStatus)) {
		cart = new Cart();
		cart.setProduct(productDtls);
		cart.setUser(userDtls);
		cart.setQuantity(1);
		cart.setTotalPrice(1 * productDtls.getDiscountPrice());
	}else {
		cart = cartStatus;
		cart.setQuantity(cart.getQuantity()+1);
		cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
	}
	Cart saveCart = cartRepo.save(cart);
		return saveCart;
	}

	@Override
	public List<Cart> getAllCart(int userId) {
	    List<Cart> carts = cartRepo.findByUserId(userId);

	    double totalOrderAmt = 0.0;

	    for (Cart c : carts) {
	        double totalPrice = c.getProduct().getDiscountPrice() * c.getQuantity();
	        c.setTotalPrice(totalPrice);
	        totalOrderAmt += totalPrice;
	    }

	    return carts; // we'll handle totalOrderAmt separately
	}

	@Override
	public double getTotalOrderAmt(int userId) {
	    List<Cart> carts = cartRepo.findByUserId(userId);
	    double totalOrderAmt = 0.0;
	    for (Cart c : carts) {
	        totalOrderAmt += c.getProduct().getDiscountPrice() * c.getQuantity();
	    }
	    return totalOrderAmt;
	}


	@Override
	public Integer getCountByUserId(Integer userId) {
		  Integer countByUserId = cartRepo.countByUserId(userId);
		return countByUserId ;
	}

	@Override
	public void changeQuantity(Integer cartId, int delta) {
	    Cart cart = cartRepo.findById(cartId).orElse(null);
	    if (cart != null) {
	        int newQty = cart.getQuantity() + delta;
	        if (newQty <= 0) {
	            cartRepo.delete(cart); // remove item if quantity <= 0
	        } else {
	            cart.setQuantity(newQty);
	            cart.setTotalPrice(newQty * cart.getProduct().getDiscountPrice());
	            cartRepo.save(cart);
	        }
	    }
	}

	

	

}
