package in.com.main.service;

import java.util.List;

import in.com.main.entities.AdminCategory;

public interface AdminCategoryService {
	
 public AdminCategory saveCategory(AdminCategory AdCate);
 
 public Boolean existsCategory(String CategoryName);
 
 public List<AdminCategory> getAllAdcate();
 
 public Boolean deleteCate(int id);
 
 public AdminCategory getCategoryById(int id);
 
 
 
 

}
