package in.com.main.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.com.main.entities.Products;

public interface ProductRepositary extends JpaRepository<Products, Integer> {
	

	List<Products> findByIsActiveTrue();

	List<Products> findByCategoryAndIsActiveTrue(String category);
	
	List<Products> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);

	

}
