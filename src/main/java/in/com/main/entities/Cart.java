package in.com.main.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	public int id;
	@ManyToOne
	public LoginAndRegistration user;
	@ManyToOne
	public Products product;
	@Column
	public int quantity;
	@Transient
	public double totalPrice;
	@Transient
	public double totalOrderAmt;
	
	
	
	public double getTotalOrderAmt() {
		return totalOrderAmt;
	}
	public void setTotalOrderAmt(double totalOrderAmt) {
		this.totalOrderAmt = totalOrderAmt;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LoginAndRegistration getUser() {
		return user;
	}
	public void setUser(LoginAndRegistration user) {
		this.user = user;
	}
	public Products getProduct() {
		return product;
	}
	public void setProduct(Products product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

}
