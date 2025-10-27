package in.com.main.service;

import java.util.List;

import in.com.main.entities.Cart;

public interface CartService {
	
	public Cart saveCart(Integer productId, Integer userId);
	
	public List<Cart> getAllCart(int userId);
	
	public Integer getCountByUserId(Integer userId);

	public double getTotalOrderAmt(int userId);

	public void changeQuantity(Integer cartId, int delta);

}
