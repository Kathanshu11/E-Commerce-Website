package in.com.main.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.com.main.entities.LoginAndRegistration;
import in.com.main.repositary.LoginAndRegistartionRepositary;
import in.com.main.util.AppConst;

@Service
public class LoginAndRegistrationServiceImpl implements LoginAndRegistrationService {

	@Autowired
	private LoginAndRegistartionRepositary LogandReg;

	@Override
	public Boolean RegiForm(LoginAndRegistration LandG) {
		try {
			LogandReg.save(LandG);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public LoginAndRegistration getEmail(String email) {
		return LogandReg.findByEmail(email);
	}

	@Override
	public List<LoginAndRegistration> getDtls(String role) {
		return LogandReg.findByRole(role);
	}

	@Override
	public Boolean updateUser(int id, Boolean status) {
		Optional<LoginAndRegistration> byId = LogandReg.findById(id);
		if (byId.isPresent()) {
			LoginAndRegistration userId = byId.get();
			userId.setIsEnabled(status);
			LogandReg.save(userId);
			return true;
		}
		return false;
	}

	@Override
	public void increaseFailedAttempt(LoginAndRegistration lg) {
		Integer attempt = lg.getFailedAttempt() + 1;
		lg.setFailedAttempt(attempt);
		LogandReg.save(lg);
	}

	@Override
	public void userAccLock(LoginAndRegistration lg) {
		lg.setAccountNonLocked(false);
		lg.setLockTime(new Date());
		LogandReg.save(lg);
	}

	@Override
	public Boolean unlockTimeAccExpired(LoginAndRegistration lg) {
		long lockTime = lg.getLockTime().getTime();
		long unlockTime = lockTime+AppConst.UNLOCK_DURATION_TIME;
		
		long currentTime = System.currentTimeMillis();
		
		if(unlockTime<currentTime) {
			lg.setAccountNonLocked(true);
			lg.setFailedAttempt(0);
			lg.setLockTime(null);
			LogandReg.save(lg);
			return true;
		}
		
		return false;
	}

	@Override
	public void UpadteUserResetToken(String email, String resTok) {
	
		LoginAndRegistration byEmail = LogandReg.findByEmail(email);
		byEmail.setReset_token(resTok);
		LogandReg.save(byEmail);
	}

	@Override
	public LoginAndRegistration getUserByResetToken(String token) {
	
		return LogandReg.findByResetToken(token);
	}

	@Override
	public LoginAndRegistration updateUser(LoginAndRegistration user) {
	
		return LogandReg.save(user);
	}

	@Override
	public void resetAttempt(int id) {
			
	}

	@Override
	public List<LoginAndRegistration> getAllDtls() {
		return  LogandReg.findAll();
	}
	
	@Override
	public LoginAndRegistration getUserById(int id) {
	    return LogandReg.findById(id).orElse(null);
	}

}