package in.com.main.entities;

import java.sql.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String orderId;
    @Column
    private Date orderDate;
    
    @ManyToOne
    private  Products products;
    @Column
    private Double price;
    @Column
    private int quantity;
    @ManyToOne
    private LoginAndRegistration user;
    @Column
    private String status;
    @Column
    private String pymtType;
    @OneToOne(cascade = CascadeType.ALL)
    private orderAdd orderAdd;
    @Column
    private Double totalAmount;
    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Products getProducts() {
		return products;
	}
	public void setProducts(Products products) {
		this.products = products;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public LoginAndRegistration getUser() {
		return user;
	}
	public void setUser(LoginAndRegistration user) {
		this.user = user;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPymtType() {
		return pymtType;
	}
	public void setPymtType(String pymtType) {
		this.pymtType = pymtType;
	}
	public orderAdd getOrderAdd() {
		return orderAdd;
	}
	public void setOrderAdd(orderAdd orderAdd) {
		this.orderAdd = orderAdd;
	} 

    
    
    
    
    
    

}
