package in.com.main.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;



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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import in.com.main.entities.Cart;
import in.com.main.entities.LoginAndRegistration;
import in.com.main.entities.OrderReq;
import in.com.main.entities.ProductOrder;
import in.com.main.entities.Products;
import in.com.main.service.CartService;
import in.com.main.service.LoginAndRegistrationService;
import in.com.main.service.ProductsService;
import in.com.main.service.productOrderService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private productOrderService prodOrderService;

	@Autowired
	private LoginAndRegistrationService LandGservice;

	@Autowired
	private ProductsService productsService;

	@Autowired
	private CartService cartService;

	@GetMapping("/")
	public String open() {
		return "user/home";
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
	
	


//	------------------------------------------------------------------------------
	@GetMapping("/addCart")
	public String openCartPage(@RequestParam Integer pid, @RequestParam Integer uid,
			RedirectAttributes redirectAttributes) {

		Cart saveCart = cartService.saveCart(pid, uid);
		if (ObjectUtils.isEmpty(saveCart)) {
			redirectAttributes.addFlashAttribute("errorMsg", "Failed to cart");
		} else {
			redirectAttributes.addFlashAttribute("succMsg", "Added to cart Succsfully");
		}
		return "redirect:/user/view_product/" + pid;
	}

	@GetMapping("/view_product/{pid}")
	public String viewProduct(@PathVariable("pid") Integer pid, Model model) {
		Products product = productsService.getProductsById(pid);
		if (product == null) {
			model.addAttribute("errorMsg", "Product not found");
			return "redirect:/user/";
		}
		model.addAttribute("product", product);
		return "view_product";
	}

	@GetMapping("/cartPage")
	public String openCartPage(Principal p, Model model) {

		LoginAndRegistration user = getLoggedInUserDtls(p);
		List<Cart> allCart = cartService.getAllCart(user.getId());
		  double totalOrderAmt = cartService.getTotalOrderAmt(user.getId());
		model.addAttribute("carts", allCart);
	    model.addAttribute("totalOrderAmt", totalOrderAmt);
		return "user/cart";

	}

	private LoginAndRegistration getLoggedInUserDtls(Principal p) {
		String email = p.getName();
		LoginAndRegistration userDtls = LandGservice.getEmail(email);
		return userDtls;
	}
	
	@GetMapping("/cart/increment/{cartId}")
	public String incrementQuantity(@PathVariable Integer cartId, Principal p) {
	    cartService.changeQuantity(cartId, 1);
	    return "redirect:/user/cartPage";
	}

	@GetMapping("/cart/decrement/{cartId}")
	public String decrementQuantity(@PathVariable Integer cartId, Principal p) {
	    cartService.changeQuantity(cartId, -1);
	    return "redirect:/user/cartPage";
	}
	
	@GetMapping("/pymt")
	public String openPymtapage(Principal p, Model model) {
	    // Get logged-in user details
	    LoginAndRegistration user = getLoggedInUserDtls(p);

	    // Get all cart items for the user
	    List<Cart> allCart = cartService.getAllCart(user.getId());

	    // Get total cart amount (sum of all product prices)
	    double orderPrice = cartService.getTotalOrderAmt(user.getId());

	    // Additional charges
	    double deliveryFee = 250;
	    double tax = 101;
	    double handlingFee = 10;

	    // Grand total
	    double totalOrderAmt = orderPrice + deliveryFee + tax + handlingFee;

	    // Add attributes for Thymeleaf
	    model.addAttribute("carts", allCart);
	    model.addAttribute("orderPrice", orderPrice);
	    model.addAttribute("deliveryFee", deliveryFee);
	    model.addAttribute("tax", tax);
	    model.addAttribute("handlingFee", handlingFee);
	    model.addAttribute("totalOrderAmt", totalOrderAmt);

	    // 👇 Add this to fix Thymeleaf binding error
	    model.addAttribute("orderReq", new OrderReq());

	    return "/user/payment";
	}


	
	@PostMapping("/SaveOrder")
	public String saveOrder(@ModelAttribute OrderReq orderReq, Principal p) {
		LoginAndRegistration user = getLoggedInUserDtls(p);
	    prodOrderService.saveOrder(user.getId(), orderReq);
	    return "/user/pymtSucess";
	}

	@GetMapping("/viewOrders")
	public String openViewOrders(Principal p, Model model) {
	    LoginAndRegistration user = getLoggedInUserDtls(p);
	    List<ProductOrder> orders = prodOrderService.getOrdersByUser(user.getId());
	    model.addAttribute("orders", orders);
	    return "/user/orders";
	}
	
	
	@GetMapping("/cancelOrder/{orderId}")
	public String cancelOrder(@PathVariable("orderId") Integer orderId,
	                          Principal p,
	                          RedirectAttributes redirectAttributes) {
	    LoginAndRegistration user = getLoggedInUserDtls(p);
	    boolean canceled = prodOrderService.cancelOrder(orderId, user.getId());
	    if (canceled) {
	        redirectAttributes.addFlashAttribute("succMsg", "Order canceled successfully.");
	    } else {
	        redirectAttributes.addFlashAttribute("errorMsg", "Unable to cancel order.");
	    }
	    return "redirect:/user/viewOrders";
	}

	 @GetMapping("/profilePage")
	    public String openProfile() {
	        return "/user/profile";
	    }

	 @GetMapping("/editProfilePage")
	    public String editProfilePage(Model model, Principal principal) {
	        String email = principal.getName();
	        LoginAndRegistration user = LandGservice.getEmail(email);
	        model.addAttribute("user", user);
	        return "user/editprofile";  
	    }

	 @PostMapping("/update")
	 public String updateProfile(
	         @ModelAttribute("user") LoginAndRegistration user,
	         @RequestParam(value = "profileImage", required = false) MultipartFile file,
	         Model model) {

	     // Fetch the existing user from DB
	     LoginAndRegistration existingUser = LandGservice.getUserById(user.getId());

	     if (existingUser != null) {

	         // Update basic fields
	         existingUser.setName(user.getName());
	         existingUser.setEmail(user.getEmail());
	         existingUser.setMobile(user.getMobile());
	         existingUser.setGender(user.getGender());
	         existingUser.setAddress(user.getAddress());
	         existingUser.setCity(user.getCity());
	         existingUser.setPincode(user.getPincode());
	         existingUser.setState(user.getState());

	         // Handle new image upload
	         if (file != null && !file.isEmpty()) {
	             try {
	                 // Define upload directory outside the project folder
	                 String uploadDir = System.getProperty("user.home") + "/uploads/userRegisterImg";
	                 Path uploadPath = Paths.get(uploadDir);

	                 // Create directories if not exist
	                 if (!Files.exists(uploadPath)) {
	                     Files.createDirectories(uploadPath);
	                 }

	                 // Save file
	                 String fileName = file.getOriginalFilename();
	                 Path filePath = uploadPath.resolve(fileName);
	                 Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	                 // ✅ Set new image name to DB
	                 existingUser.setProfileImage(fileName);

	             } catch (IOException e) {
	                 e.printStackTrace();
	                 model.addAttribute("msg", "❌ Failed to upload image!");
	             }
	         } else {
	             // ✅ Keep existing image if user didn’t upload a new one
	             existingUser.setProfileImage(existingUser.getProfileImage());
	         }

	         // Save updated user
	         LandGservice.updateUser(existingUser);

	         model.addAttribute("msg", "✅ Profile updated successfully!");
	         model.addAttribute("user", existingUser);

	     } else {
	         model.addAttribute("msg", "❌ Error: User not found!");
	         model.addAttribute("user", user);
	     }

	     return "redirect:/user/editProfilePage";
	 }

	
	
	
}
