package in.com.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import in.com.main.entities.AdminCategory;
import in.com.main.repositary.AdminCategoryRepositary;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

	@Autowired
	private AdminCategoryRepositary AdCateRepo;

	@Override
	public AdminCategory saveCategory(AdminCategory AdCate) {
		return AdCateRepo.save(AdCate);
	}

	@Override
	public List<AdminCategory> getAllAdcate() {
		return AdCateRepo.findAll();
	}

	@Override
	public Boolean existsCategory(String CategoryName) {
		
		return AdCateRepo.existsByCategoryName(CategoryName);
	}

	@Override
	public Boolean deleteCate(int id) {
		AdminCategory orElse = AdCateRepo.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(orElse)) {
			AdCateRepo.delete(orElse);
			return true;
		}
		return false;
	}

	@Override
	public AdminCategory getCategoryById(int id) {
	 AdminCategory orElse = AdCateRepo.findById(id).orElse(null);
		return orElse;
	}

}
