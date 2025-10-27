package in.com.main.Img.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TableImg implements WebMvcConfigurer{
	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        // Expose /CategoryImg/** URLs to map to your uploads/CategoryImg folder
	        String uploadPath = System.getProperty("user.dir") + "/uploads/CategoryImg/";
	        registry.addResourceHandler("/CategoryImg/**")
	                .addResourceLocations("file:" + uploadPath);
	    }
}

