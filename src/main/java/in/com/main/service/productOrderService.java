package in.com.main.service;



import java.util.List;

import in.com.main.entities.OrderReq;
import in.com.main.entities.ProductOrder;
import in.com.main.util.OrderStatus;


public interface productOrderService {

	public void saveOrder(Integer userId, OrderReq orderReq);

	List<ProductOrder> getOrdersByUser(Integer userId);
	
	public boolean cancelOrder(Integer orderId, Integer userId);

	public List<ProductOrder> getAllOrders();

	public void updateOrderStatus(String orderId, OrderStatus status);

}
