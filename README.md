Project Overview :
This is a full-featured E-Commerce web application built with Spring Boot, Spring Security, Spring Data JPA, and MySQL.
It supports role-based access control where:
👉Admins can manage products and users.
👉Customers can browse, view, and purchase products.
The project uses Lombok to reduce boilerplate code and environment variables to protect sensitive information (like database credentials).

⚙️ Tech Stack
| Layer      | Technology                             |
| ---------- | -------------------------------------- |
| Backend    | Spring Boot                            |
| Security   | Spring Security                        |
| ORM        | Spring Data JPA                        |
| Database   | MySQL                                  |
| Utility    | Lombok                                 |
| Build Tool | Maven                                  |
| IDE        | Spring Tool Suite / Eclipse            |	

👤 User Roles
🧑‍💼 Admin
👉Add, update, or delete products
👉Manage users and their activity
👉View user orders and purchase history
🧑‍💻 User
👉Register and log in securely
👉Browse and search products
👉Add items to cart
👉Purchase products
👉View order history

Security Features
👉Role-based login and authorization (ADMIN / USER)
👉Password encryption with BCrypt
👉Session-based authentication and logout
👉Restricted access for admin-only endpoints

⚙️ Application Configuration
application.properties
server.port=8080

# Database configuration using environment variables
	spring.datasource.url=jdbc:mysql://localhost:3306/database name
	spring.datasource.username=${DB_USERNAME}
	spring.datasource.password=${DB_PASSWORD}
	spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

	spring.jpa.hibernate.ddl-auto=update
	spring.jpa.show-sql=true
	spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

🧩 Dependencies (pom.xml)
	<dependencies>
  	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>	
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
  </dependency> 
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
  <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <scope>runtime</scope>
    </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>  
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
   <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
  </dependencies>



    
Author
Kathanshu Patil
Linkdin:-https://www.linkedin.com/in/kathanshupatil01/
Email:-kathanshupatil11@gmail.com

