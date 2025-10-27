package in.com.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.com.main.entities.LoginAndRegistration;
import in.com.main.repositary.LoginAndRegistartionRepositary;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private LoginAndRegistartionRepositary userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {        
		LoginAndRegistration byEmail = userRepo.findByEmail(username);
		if(byEmail == null) {
			throw new UsernameNotFoundException ("user not found");
		}
		return new CustomUser(byEmail);
	}

}
