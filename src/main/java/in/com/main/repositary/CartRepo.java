package in.com.main.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import in.com.main.entities.Cart;

public interface CartRepo extends JpaRepository<Cart ,Integer> {

	public Cart findByProductIdAndUserId(Integer productId, Integer userId);

	public Integer countByUserId(Integer userId);

	public List <Cart>findByUserId(Integer userId);
}
