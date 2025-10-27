package in.com.main.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.com.main.entities.ProductOrder;

public interface productOrderRepositary extends JpaRepository<ProductOrder, Integer> {
	
	 List<ProductOrder> findByUserId(Integer userId);
	 
	   public ProductOrder findByOrderId(String orderId); 

}
