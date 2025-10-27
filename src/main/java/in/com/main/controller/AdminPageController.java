package in.com.main.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.com.main.dto.AdminCategoryDTO;
import in.com.main.entities.AdminCategory;
import in.com.main.entities.LoginAndRegistration;
import in.com.main.entities.ProductOrder;
import in.com.main.entities.Products;
import in.com.main.service.AdminCategoryService;
import in.com.main.service.CartService;
import in.com.main.service.LoginAndRegistrationService;
import in.com.main.service.ProductsService;
import in.com.main.service.productOrderService;
import in.com.main.util.OrderStatus;


@Controller
@RequestMapping("/Admin")
public class AdminPageController {

	@Autowired
	private LoginAndRegistrationService LandGservice;

	@Autowired
	private AdminCategoryService AdCateService;

	@Autowired
	private ProductsService productService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private productOrderService prodOrderService;

//-----------------------------------------------
	@GetMapping("/adminPage")
	public String OpenAdmin() {
		return "Admin/admin";
	}
	@ModelAttribute
	public void getUserDtls(Principal p, Model model) {

		if (p != null) {
			String email = p.getName();
			LoginAndRegistration e = LandGservice.getEmail(email);
			model.addAttribute("user", e);
			Integer  cartCount = cartService.getCountByUserId(e.getId());
			model.addAttribute("count", cartCount);

		} else {
			model.addAttribute("user", null);
		}

	}
//	------------------------------------------------
	@GetMapping("/AddProduct")
	public String openAddProd(Model model) {
		List<AdminCategory> categories = AdCateService.getAllAdcate();
		model.addAttribute("categories", categories);
		return ("Admin/addProduct");
	}

	@GetMapping("/dltProduct/{id}")
	public String dltProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
		Boolean dltProduct = productService.dltProduct(id);
		if (dltProduct) {
			redirectAttributes.addFlashAttribute("success", "Product successfully removed");
		} else {
			redirectAttributes.addFlashAttribute("Error", "Something went wrong");
		}

		return "redirect:/Admin/ViewProductList";

	}

	@GetMapping("/editProduct/{id}")
	public String openEditProd(@PathVariable int id, Model model) {
		model.addAttribute("editProduct", productService.getProductsById(id));
		model.addAttribute("categories", AdCateService.getAllAdcate());
		return "/Admin/editproducts";
	}

	@PostMapping("/updateProduct")
	public String openUpdateProduct(@ModelAttribute Products product, @RequestParam("productImg") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		try {
			productService.updateProduct(product, file);
			redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to update product: " + e.getMessage());
		}
		return "redirect:/Admin/ViewProductList";
	}

	@PostMapping("/subAddProduct")
	public String saveProduct(@ModelAttribute Products product,
	                          @RequestParam("productImg") MultipartFile file,
	                          RedirectAttributes redirectAttributes) {
	    try {
	        if (file != null && !file.isEmpty()) {

	            // ✅ Validate file type (optional but good practice)
	            String contentType = file.getContentType();
	            if (contentType == null || !contentType.startsWith("image/")) {
	                redirectAttributes.addFlashAttribute("failed", "Only image files are allowed!");
	                return "redirect:/Admin/AddProduct";
	            }

	            // ✅ Generate unique file name
	            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

	            // ✅ Use external uploads folder (not inside src/)
	            String uploadDir = System.getProperty("user.dir") + "/uploads/ProductImg/";
	            Path uploadPath = Paths.get(uploadDir);

	            // Create directory if not exists
	            if (!Files.exists(uploadPath)) {
	                Files.createDirectories(uploadPath);
	            }

	            // ✅ Save the image file to external directory
	            Files.copy(file.getInputStream(),
	                       uploadPath.resolve(fileName),
	                       StandardCopyOption.REPLACE_EXISTING);

	            // ✅ Set file name in DB
	            product.setImgName(fileName);

	            System.out.println("✅ Image saved successfully at: " + uploadPath.resolve(fileName));
	        } else {
	            // Default image if none uploaded
	            product.setImgName("default.jpg");
	        }

	        // ✅ Product defaults
	        product.setDiscount(0);
	        product.setDiscountPrice(product.getPrice());

	        // ✅ Save product to DB
	        Products savedProduct = productService.saveProduct(product);

	        if (savedProduct != null) {
	            redirectAttributes.addFlashAttribute("success", "Product saved successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("failed", "Failed to save product!");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("failed", "Error while saving product: " + e.getMessage());
	    }

	    return "redirect:/Admin/AddProduct";
	}


//----------------------------------------------
	@GetMapping("/ViewProductList")
	public String openProduct(Model model) {
		List<Products> allProducts = productService.getAllProducts();
		model.addAttribute("product", allProducts);
		return "/Admin/products";
	}

//	------------------------------------------------
	@GetMapping("/Categ")
	public String openCategory(Model model) {
		List<AdminCategory> categories = AdCateService.getAllAdcate();
		model.addAttribute("categories", categories);
		return ("Admin/category");
	}

	@PostMapping("/subCate")
	public String submitCategory(@ModelAttribute AdminCategoryDTO dto, RedirectAttributes redirectAttributes) {
		String imgName = (dto.getCategoryImg() != null && !dto.getCategoryImg().isEmpty())
				? dto.getCategoryImg().getOriginalFilename()
				: "default.jpg";

		AdminCategory category = new AdminCategory();
		category.setCategoryName(dto.getCategoryName());
		category.setIsActive(dto.getIsActive());
		category.setCategoryImg(imgName);

		Boolean existCategory = AdCateService.existsCategory(category.getCategoryName());
		if (existCategory) {
			redirectAttributes.addFlashAttribute("errorMsg", "Category already exists");
		} else {
			AdminCategory saveCategory = AdCateService.saveCategory(category);

			if (ObjectUtils.isEmpty(saveCategory)) {
				redirectAttributes.addFlashAttribute("errorMsg", "Something went wrong, try again later");
			} else {
				try {
					// ✅ Use external uploads folder (not inside resources)
					String uploadDir = System.getProperty("user.dir") + "/uploads/CategoryImg/";
					Path uploadPath = Paths.get(uploadDir);

					// Ensure folder exists
					if (!Files.exists(uploadPath)) {
						Files.createDirectories(uploadPath);
						System.out.println("Created upload folder: " + uploadPath.toAbsolutePath());
					}

					if (!imgName.equals("default.jpg")) {
						Path filePath = uploadPath.resolve(imgName);

						// Save uploaded file
						dto.getCategoryImg().transferTo(filePath.toFile());

						System.out.println("✅ File uploaded successfully: " + filePath.toAbsolutePath());
					}

					redirectAttributes.addFlashAttribute("msg", "Saved successfully");
				} catch (IOException e) {
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("errorMsg", "Image upload failed: " + e.getMessage());
				}
			}
		}
		return "redirect:/Admin/Categ";
	}

	@GetMapping("/dltCate/{id}")
	public String deleteCat(@PathVariable int id, RedirectAttributes redirectAttributes) {
		Boolean deleteCate = AdCateService.deleteCate(id);
		if (deleteCate) {
			redirectAttributes.addFlashAttribute("Done", "Deleted Succsfylly");
		} else {
			redirectAttributes.addFlashAttribute("noDone", "Try Agin Somthing is wrong");
		}
		return "redirect:/Admin/Categ";
	}

	@GetMapping("/openEditCate/{id}")
	public String openEditCategory(@PathVariable int id, Model model) {
		AdminCategory categoryById = AdCateService.getCategoryById(id);
		model.addAttribute("category", categoryById);
		return "/Admin/editCategory";
	}

	@PostMapping("/updateCate")
	public String editCateProduct(@ModelAttribute AdminCategoryDTO dto, RedirectAttributes redirectAttributes) {

		// 1️⃣ Load old category
		AdminCategory oldCategory = AdCateService.getCategoryById(dto.getId());
		if (ObjectUtils.isEmpty(oldCategory)) {
			redirectAttributes.addFlashAttribute("error", "Category not found!");
			return "redirect:/Admin/Categ";
		}

		// 2️⃣ Update fields
		oldCategory.setCategoryName(dto.getCategoryName());
		oldCategory.setIsActive(dto.getIsActive());

		// 3️⃣ Handle file upload
		MultipartFile file = dto.getCategoryImg();
		String ImgName = oldCategory.getCategoryImg();
		if (file != null && !file.isEmpty()) {
			ImgName = file.getOriginalFilename();
			try {
				String uploadDir = System.getProperty("user.dir") + "/uploads/CategoryImg/";
				File uploadFile = new File(uploadDir + ImgName);
				uploadFile.getParentFile().mkdirs();
				file.transferTo(uploadFile);
			} catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("error", "Failed to upload image!");
			}
		}
		oldCategory.setCategoryImg(ImgName);

		// 4️⃣ Save category
		AdCateService.saveCategory(oldCategory);
		redirectAttributes.addFlashAttribute("success", "Category updated successfully!");

		return "redirect:/Admin/openEditCate/" + dto.getId();
	}

//	--------------------------------------------------

	@GetMapping("/User")
	public String openUserPage(Model model) {
		List<LoginAndRegistration> dtls = LandGservice.getDtls("ROLE_USER");
		model.addAttribute("users", dtls);
		return "/Admin/user";
	}

	@GetMapping("/updateSts")
	public String updateUser(@RequestParam Boolean status,
	                         @RequestParam int id,
	                         RedirectAttributes redirectAttributes) {
	    boolean updated = LandGservice.updateUser(id, status);

	    if(updated) {
	        redirectAttributes.addFlashAttribute("success", "User status updated successfully!");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "User not found!");
	    }

	    return "redirect:/Admin/User";
	}
	
	 @GetMapping("/orderDtls")
	    public String viewAllOrders(Model model) {
	        List<ProductOrder> allOrders = prodOrderService.getAllOrders();

	        // Debug
	        System.out.println("Total orders found: " + allOrders.size());

	        model.addAttribute("orders", allOrders);
	        return "/Admin/ordersDtls"; 
	    }
	 
	 @PostMapping("/updateOrderStatus")
	    public String updateOrderStatus(
	            @RequestParam("orderId") String orderId,
	            @RequestParam("statusId") int statusId,
	            RedirectAttributes redirectAttributes) {

	        OrderStatus newStatus = Arrays.stream(OrderStatus.values())
	                .filter(s -> s.getId() == statusId)
	                .findFirst()
	                .orElse(OrderStatus.IN_PROGRESS);

	        prodOrderService.updateOrderStatus(orderId, newStatus);

	        redirectAttributes.addFlashAttribute("success", "Order status updated successfully!");
	        return "redirect:/Admin/orderDtls"; 
	    }
	 
	 
	 
	 @GetMapping("/allAdmin")
	 public String openAdmin(Model model) {
		 
		 List<LoginAndRegistration> allDtls = LandGservice.getAllDtls();
		 
		 model.addAttribute("admin", allDtls);
		 return"/Admin/AddAdmins";
	 }
	 
	 
	 @GetMapping("/updateRole/{id}")
	 public String updateRole(@PathVariable int id) {
	     LoginAndRegistration user = LandGservice.getUserById(id);

	     if (user != null) {
	         if ("ROLE_ADMIN".equalsIgnoreCase(user.getRole())) {
	             user.setRole("ROLE_USER");
	         } else {
	             user.setRole("ROLE_ADMIN");
	         }

	         LandGservice.updateUser(user);
	     }
	     return "redirect:/Admin/allAdmin";

	 }
	 





}
