package in.com.main.Img.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CategoryImg implements WebMvcConfigurer {

	
	

	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        // Serve uploaded images from /uploads/CategoryImg/ via /CategoryImg/** URL
	        registry.addResourceHandler("/CategoryImg/**")
	                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/CategoryImg/");
	    }
	

	
}
