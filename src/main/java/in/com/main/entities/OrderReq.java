package in.com.main.entities;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderReq {

	    private String firstName;
	  
	    private String lastName;
	    
	    private String email;
	
	    private String mobileNo;
	
	    private String address;

	    private String city;
	 
	    private String state;

	    private String pincode;
	    
	    private String PymtType;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getMobileNo() {
			return mobileNo;
		}

		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getPincode() {
			return pincode;
		}

		public void setPincode(String pincode) {
			this.pincode = pincode;
		}

		public String getPymtType() {
			return PymtType;
		}

		public void setPymtType(String pymtType) {
			PymtType = pymtType;
		}

	

}
