package in.com.main.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.com.main.entities.Products;
import in.com.main.repositary.ProductRepositary;

@Service
public class ProductsServiceImpl implements ProductsService {

	@Autowired
	private ProductRepositary productRepo;

	@Override
	public Products saveProduct(Products products) {
		return productRepo.save(products);
	}

	@Override
	public List<Products> getAllProducts() {
		return productRepo.findAll();
	}

	@Override
	public Boolean dltProduct(int id) {
		Products byId = productRepo.findById(id).orElse(null);

		if (byId != null) {
			productRepo.delete(byId);
			return true;
		}

		return false;
	}

	@Override
	public Products getProductsById(int id) {
		Products byId = productRepo.findById(id).orElse(null);
		return byId;
	}

	@Override
	public Products updateProduct(Products product, MultipartFile file) {
		
		Products productsById = getProductsById(product.getId());
		
		String imgCheck = file.isEmpty() ? productsById.getImgName() : file.getOriginalFilename();
		
		productsById.setTitle(product.getTitle());
		productsById.setDescription(product.getDescription());
		productsById.setCategory(product.getCategory());
		productsById.setPrice(product.getPrice());
		productsById.setStocks(product.getStocks());
		productsById.setIsActive(product.getIsActive());
		
		productsById.setDiscount(product.getDiscount());
		
		Double DCount = product.getPrice()*(product.getDiscount()/100.0);
		Double dCount = product.getPrice() - DCount ;
		
		productsById.setDiscountPrice(dCount);
		
		
		
		Products save = productRepo.save(productsById);
		
		 try {
		        if (file != null && !file.isEmpty()) {
		            // Generate unique file name
		            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

		            Path uploadPath = Paths.get("src/main/resources/static/ProductImg/");
		            if (!Files.exists(uploadPath)) {
		                Files.createDirectories(uploadPath);
		            }

		            // Copy file to folder
		            Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

		            // Set new image name to the entity BEFORE saving
		            productsById.setImgName(fileName);
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		        System.out.println("Failed to upload image: " + e.getMessage());
		    }
		
		return save;
	}

	@Override
	public List<Products> getAllActiveProducts() {
		List<Products> productList = productRepo.findByIsActiveTrue();
		return productList;
	}

	@Override
	public List<Products> getProductsByCategory(String category) {
	    return productRepo.findByCategoryAndIsActiveTrue(category);
	}

	@Override
	public List<Products> serachProduct(String ch) {
		
		return productRepo.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
	}



}
