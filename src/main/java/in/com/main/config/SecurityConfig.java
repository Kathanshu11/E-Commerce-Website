package in.com.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	
	@Autowired
    private AuthSuccHandlerImpl authSuccHandlerImpl;

	@Autowired
	@Lazy
    private AuthFailerHandler authFailerHandler;
  
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .cors(cors -> cors.disable())
	        .authorizeHttpRequests(req -> req
	        		.requestMatchers("/user/**").hasAnyRole("USER")
	            .requestMatchers("/Admin/**").hasRole("ADMIN") 
	            .requestMatchers("/**").permitAll()
	        )
	        .formLogin(form -> form
	            .loginPage("/signin")
	            .loginProcessingUrl("/login")
	            .defaultSuccessUrl("/")
	            .failureHandler(authFailerHandler)
	            .successHandler(authSuccHandlerImpl)
	        )
	        .logout(logout -> logout.permitAll());

	    return http.build();
	}

	




}
