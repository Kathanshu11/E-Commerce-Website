package in.com.main.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.com.main.entities.AdminCategory;
import in.com.main.entities.LoginAndRegistration;
import in.com.main.entities.Products;
import in.com.main.service.AdminCategoryService;
import in.com.main.service.CartService;
import in.com.main.service.LoginAndRegistrationService;
import in.com.main.service.ProductsService;
import in.com.main.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class pageController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LoginAndRegistrationService LandGservice;

	@Autowired
	private AdminCategoryService AdCateService;

	@Autowired
	private ProductsService productService;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private CartService cartService;

//	------------------------------------------------------------

	@GetMapping("/")
	public String indexPage(Model model) {
		List<Products> allProducts = productService.getAllActiveProducts();
		allProducts.sort((p1, p2) -> p2.getId() - p1.getId());
		List<Products> latestProducts = allProducts.size() > 8 ? allProducts.subList(0, 8) : allProducts;
		model.addAttribute("latestProducts", latestProducts);
		List<AdminCategory> categories = AdCateService.getAllAdcate();
		model.addAttribute("categories", categories);

		return "index";
	}

	@ModelAttribute
	public void getUserDtls(Principal p, Model model) {

		if (p != null) {
			String email = p.getName();
			LoginAndRegistration e = LandGservice.getEmail(email);
			model.addAttribute("user", e);
			Integer cartCount = cartService.getCountByUserId(e.getId());
			model.addAttribute("count", cartCount);

		} else {
			model.addAttribute("user", null);
		}

	}

//	------------------------------------------------------------

	@GetMapping("/productPage")
	public String OpeneProduct(Model model, @RequestParam(value = "category", defaultValue = " ") String category) {
		List<AdminCategory> categories = AdCateService.getAllAdcate();
		model.addAttribute("categories", categories);
		List<Products> allActiveProducts = productService.getAllActiveProducts();
		model.addAttribute("Product", allActiveProducts);
		model.addAttribute("currentCategory", null);
		return "product";
	}

	@GetMapping("/admin/categories/{category}")
	public String getProductsByCategory(@PathVariable("category") String category, Model model) {
		List<AdminCategory> categories = AdCateService.getAllAdcate();
		model.addAttribute("categories", categories);
		List<Products> productsByCategory = productService.getProductsByCategory(category);
		model.addAttribute("Product", productsByCategory);
		model.addAttribute("currentCategory", category);
		return "product";
	}

	@GetMapping("/viewProduct/{id}")
	public String openViewProduct(@PathVariable int id, Model model) {
		Products productsById = productService.getProductsById(id);
		model.addAttribute("product", productsById);
		return "view_product";
	}

//--------------------------------------------------------------------

	@GetMapping("/signin")
	public String openLog() {
		return "login";
	}

	@GetMapping("/regiPage")
	public String OpenReg(Model model) {
		model.addAttribute("LandR", new LoginAndRegistration());
		return "register";
	}

	@PostMapping("/RegForm")
	public String SubmitRegForm(
	        @ModelAttribute("LandR") LoginAndRegistration LandG,
	        @RequestParam("profileImgFile") MultipartFile file,
	        Model model) {
	    
	    LandG.setRole("ROLE_USER");
	    LandG.setIsEnabled(true);
	    LandG.setAccountNonLocked(true);
	    LandG.setFailedAttempt(0);
	    LandG.setPassword(passwordEncoder.encode(LandG.getPassword()));

	    try {
	        String uploadDir = System.getProperty("user.home") + "/uploads/userRegisterImg/";
	        File dir = new File(uploadDir);
	        if (!dir.exists()) dir.mkdirs(); // create folder if not exists

	        if (!file.isEmpty()) {
	            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	            File dest = new File(uploadDir + fileName);
	            file.transferTo(dest); // save file
	            LandG.setProfileImage(fileName);
	        } else {
	            LandG.setProfileImage("default.png"); // use default image if no file uploaded
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        model.addAttribute("failed", "Error uploading profile image. Please try again.");
	        return "register";
	    }
	    Boolean check = LandGservice.RegiForm(LandG);
	    if (check) model.addAttribute("success", "Registered Successfully");
	    else model.addAttribute("failed", "Try Again");

	    return "register";
	}


	@GetMapping("/forgotPassPage")
	public String openForgPass() {
		return "forgot_pass";
	}

	@PostMapping("/forgPass")
	public String openrestPassPage(@RequestParam String email, RedirectAttributes redirectAttributes,
			HttpServletRequest req) throws UnsupportedEncodingException, MessagingException {

		LoginAndRegistration e = LandGservice.getEmail(email);

		if (ObjectUtils.isEmpty(e)) {
			redirectAttributes.addFlashAttribute("ErrorMsg", "Invalid Email");
		} else {

			String resTok = UUID.randomUUID().toString();
			LandGservice.UpadteUserResetToken(email, resTok);

			String url = CommonUtil.genrateUrl(req) + "/resetPassPage?token=" + resTok;

			Boolean e2 = commonUtil.email(url, email);

			if (e2) {
				redirectAttributes.addFlashAttribute("Succmsg", "Check Your email resset link sent");
			} else {
				redirectAttributes.addFlashAttribute("ErrorMsg", "something is wrong on server. Try Agin later");
			}
		}

		return "redirect:/forgotPassPage";
	}

	@GetMapping("/resetPassPage")
	public String openResetPasswordPage(@RequestParam("token") String token, Model model,
			RedirectAttributes redirectAttributes) {
		LoginAndRegistration user = LandGservice.getUserByResetToken(token);
		if (user == null) {
			redirectAttributes.addFlashAttribute("ErrorMsg", "Invalid or expired token");
			return "redirect:/forgotPassPage";
		}
		model.addAttribute("token", token);
		return "reset_pass";
	}

	@PostMapping("/resPass")
	public String successPass(@RequestParam String token, @RequestParam String password,
			@RequestParam String confirmPassword, Model model) {

		model.addAttribute("token", token);

		if (!password.equals(confirmPassword)) {
			model.addAttribute("ErrorMsg", "Passwords do not match");
			return "reset_pass";
		}

		LoginAndRegistration user = LandGservice.getUserByResetToken(token);
		if (user == null) {
			model.addAttribute("ErrorMsg", "Invalid or expired token");
			return "reset_pass";
		}

		// Encode password correctly
		user.setPassword(passwordEncoder.encode(password));

		user.setReset_token(null);
		LoginAndRegistration updatedUser = LandGservice.updateUser(user);

		if (updatedUser != null && updatedUser.getPassword() != null) {
			model.addAttribute("Succmsg", "Password updated successfully!");
		} else {
			model.addAttribute("ErrorMsg", "Something went wrong. Password was not updated.");
		}

		return "reset_pass";
	}
// ---------------------------------------------------------------------

	@GetMapping("/search")
	public String SerchPage(@RequestParam String ch, Model model) {
		List<Products> serachProducts = productService.serachProduct(ch);
		model.addAttribute("Product", serachProducts);
		List<AdminCategory> categories = AdCateService.getAllAdcate();
		model.addAttribute("categories", categories);
		 return "product";
	}
	
	

}
