package in.com.main.Img.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProfileImg implements WebMvcConfigurer{
	
	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        String uploadPath = System.getProperty("user.home") + "/uploads/userRegisterImg/";
	        registry.addResourceHandler("/userRegisterImg/**")
	                .addResourceLocations("file:" + uploadPath);
	    }
	}

