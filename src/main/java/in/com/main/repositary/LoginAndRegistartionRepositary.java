package in.com.main.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.com.main.entities.LoginAndRegistration;

public interface LoginAndRegistartionRepositary extends JpaRepository<LoginAndRegistration, Integer> {



	public LoginAndRegistration findByEmail(String email);
	
	public List<LoginAndRegistration> findByRole(String role);
	
	 @Query("SELECT u FROM LoginAndRegistration u WHERE u.reset_token = :token")
	    LoginAndRegistration findByResetToken(@Param("token") String token);
}
