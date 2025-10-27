package in.com.main.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import in.com.main.entities.Products;

public interface ProductsService {

	public Products saveProduct(Products products);
	
	public List<Products> getAllProducts();
	
	public Boolean dltProduct(int id);
	
	public Products getProductsById(int id);
	
	public Products updateProduct(Products product, MultipartFile file);
	
	public List<Products> getAllActiveProducts();
	
	 List<Products> getProductsByCategory(String category);
	 
	 public List<Products> serachProduct(String ch);
	 
	
}
