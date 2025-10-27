package in.com.main.service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import in.com.main.entities.Cart;
import in.com.main.entities.OrderReq;
import in.com.main.entities.orderAdd;
import in.com.main.entities.ProductOrder;
import in.com.main.repositary.CartRepo;
import in.com.main.repositary.productOrderRepositary;
import in.com.main.util.OrderStatus;

@Service
public class productOrderServiceImpl implements productOrderService {

	@Autowired
	private productOrderRepositary productOrderRepo;

	@Autowired
	private CartRepo cartRepo;


	@Override
	public void saveOrder(Integer userId, OrderReq orderReq) {
	    List<Cart> byUserId = cartRepo.findByUserId(userId);

	    // Fixed additional charges
	    double deliveryFee = 250;
	    double tax = 101;
	    double handlingFee = 10;

	    for (Cart cart : byUserId) {
	        ProductOrder order = new ProductOrder();
	        order.setOrderId(UUID.randomUUID().toString());
	        order.setOrderDate(new Date(System.currentTimeMillis()));
	        order.setProducts(cart.getProduct());
	        order.setPrice(cart.getProduct().getDiscountPrice());
	        order.setQuantity(cart.getQuantity());
	        order.setUser(cart.getUser());
	        order.setStatus(OrderStatus.IN_PROGRESS.getName());
	        order.setPymtType(orderReq.getPymtType());

	        // Total for this order item including additional charges
	        double itemTotal = cart.getQuantity() * cart.getProduct().getDiscountPrice();
	        double totalAmount = itemTotal + deliveryFee + tax + handlingFee;
	        order.setTotalAmount(totalAmount);

	        orderAdd address = new orderAdd();
	        address.setFirstName(orderReq.getFirstName());
	        address.setLastName(orderReq.getLastName());
	        address.setEmail(orderReq.getEmail());
	        address.setMobileNo(orderReq.getMobileNo());
	        address.setAddress(orderReq.getAddress());
	        address.setCity(orderReq.getCity());
	        address.setState(orderReq.getState());
	        address.setPincode(orderReq.getPincode());

	        order.setOrderAdd(address);

	        productOrderRepo.save(order);
	    }
	}

	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		return productOrderRepo.findByUserId(userId);
	}

	@Override
	public boolean cancelOrder(Integer orderId, Integer userId) {
		ProductOrder order = productOrderRepo.findById(orderId).orElse(null);

		if (order != null && order.getUser() != null && order.getUser().getId() == userId) {
			order.setStatus("Cancelled");
			productOrderRepo.save(order);
			return true;
		}
		return false;
	}

	@Override
	public List<ProductOrder> getAllOrders() {
		
		return productOrderRepo.findAll();
	}
	
	@Override
	public void updateOrderStatus(String orderId, OrderStatus status) {
	    ProductOrder order = productOrderRepo.findByOrderId(orderId);
	    if (order != null) {
	    	
	    	 String currentStatus = order.getStatus();
	         if ("CANCELLED".equalsIgnoreCase(currentStatus) || "DELIVERED".equalsIgnoreCase(currentStatus)) {
	             System.out.println("⚠️ Order " + orderId + " is already " + currentStatus + " and cannot be updated.");
	             return;
	         }
	    	
	        order.setStatus(status.getName());
	        productOrderRepo.save(order);
	    }
	}

}
