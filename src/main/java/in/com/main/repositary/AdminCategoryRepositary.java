package in.com.main.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import in.com.main.entities.AdminCategory;

public interface AdminCategoryRepositary extends JpaRepository<AdminCategory, Integer> {

	public Boolean existsByCategoryName(String categoryName);
	
}


