package in.com.main.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import in.com.main.entities.LoginAndRegistration;
import in.com.main.repositary.LoginAndRegistartionRepositary;
import in.com.main.service.LoginAndRegistrationService;
import in.com.main.util.AppConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailerHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private LoginAndRegistrationService userService;

	@Autowired
	private LoginAndRegistartionRepositary userRepo;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
	                                    AuthenticationException exception) throws IOException, ServletException {

		 setDefaultFailureUrl("/signin?error=true");

		
	    String email = request.getParameter("username"); 

	    LoginAndRegistration user = userRepo.findByEmail(email); 

	    if (user != null) {
	        if (Boolean.TRUE.equals(user.getIsEnabled())) {
	            if (Boolean.TRUE.equals(user.getAccountNonLocked())) {
	                if (user.getFailedAttempt() < AppConst.ATTEMPT_TIME) {
	                    userService.increaseFailedAttempt(user);
	                } else {
	                    userService.userAccLock(user);
	                    exception = new LockedException("Your Account is Locked! Failed attempts exceeded.");
	                }
	            } else {
	                if (userService.unlockTimeAccExpired(user)) {
	                    exception = new LockedException("Your Account is Unlocked. Please try again.");
	                } else {
	                    exception = new LockedException("Your Account is Locked! Try after some time.");
	                }
	            }
	        } else {
	            exception = new LockedException("Your Account is Inactive.");
	        }
	    } else {
	        exception = new LockedException("User not found with email: " + email);
	    }

	    super.onAuthenticationFailure(request, response, exception);
	}


}
