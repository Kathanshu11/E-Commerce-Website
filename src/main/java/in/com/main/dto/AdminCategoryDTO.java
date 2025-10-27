package in.com.main.dto;

import org.springframework.web.multipart.MultipartFile;

public class AdminCategoryDTO {
	 private String categoryName;
	    private Boolean isActive;
	    private MultipartFile categoryImg; 
	    private int id;

	    public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	    public String getCategoryName() {
	        return categoryName;
	    }
	    public void setCategoryName(String categoryName) {
	        this.categoryName = categoryName;
	    }

	    public Boolean getIsActive() {
	        return isActive;
	    }
	    public void setIsActive(Boolean isActive) {
	        this.isActive = isActive;
	    }

	    public MultipartFile getCategoryImg() {
	        return categoryImg;
	    }
	    public void setCategoryImg(MultipartFile categoryImg) {
	        this.categoryImg = categoryImg;
	    }
}
