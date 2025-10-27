package in.com.main.service;

import java.util.List;

import in.com.main.entities.LoginAndRegistration;

public interface LoginAndRegistrationService {
	
	public Boolean RegiForm(LoginAndRegistration LandG);

	public LoginAndRegistration getEmail(String email);
	
	public List<LoginAndRegistration> getDtls(String role);

	public Boolean updateUser(int id, Boolean status);
	
	public void increaseFailedAttempt(LoginAndRegistration lg);
	
	public void userAccLock(LoginAndRegistration lg);
	
	public Boolean unlockTimeAccExpired(LoginAndRegistration lg);
	
	public void resetAttempt(int id);

	public void UpadteUserResetToken(String email, String resTok);
	
	LoginAndRegistration getUserByResetToken(String token);
	
	public LoginAndRegistration updateUser(LoginAndRegistration user);
	
	public List<LoginAndRegistration> getAllDtls();

	public LoginAndRegistration getUserById(int id);

}
