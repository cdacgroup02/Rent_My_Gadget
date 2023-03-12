package com.onlineshopping.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineshopping.dao.CartDao;
import com.onlineshopping.dao.OrderDao;
import com.onlineshopping.dao.ProductDao;
import com.onlineshopping.dao.UserDao;
import com.onlineshopping.dto.MyOrderResponse;
import com.onlineshopping.dto.OrderDataResponse;
//import com.onlineshopping.dto.UpdateDeliveryStatusRequest;
import com.onlineshopping.model.Cart;
import com.onlineshopping.model.Orders;
import com.onlineshopping.model.Product;
import com.onlineshopping.model.User;

import com.onlineshopping.utility.Helper;

@RestController
@RequestMapping("api/user/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private CartDao cartDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ProductDao productDao;

	private ObjectMapper objectMapper = new ObjectMapper();

	
	@PostMapping("order")
	public ResponseEntity customerOrder(@RequestParam("userId") int userId) throws JsonProcessingException {

		System.out.println("request came for ORDER FOR CUSTOMER ID : " + userId);

		String orderId = Helper.getAlphaNumericOrderId();
		
		List<Cart> userCarts = cartDao.findByUser_id(userId);

		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		String formatDateTime = currentDateTime.format(formatter);

		for (Cart cart : userCarts) {

			Orders order = new Orders();
			order.setOrderId(orderId);
			order.setUser(cart.getUser());
			order.setProduct(cart.getProduct());
			order.setQuantity(cart.getQuantity());
			order.setOrderDate(formatDateTime);


			orderDao.save(order);
			
			//84-88 for reducing quantity not yet tested
			Product product = cart.getProduct();
			int quantity = cart.getQuantity();
			int newQuantity = product.getQuantity() - quantity;
			product.setQuantity(newQuantity);
			productDao.save(product);

			cartDao.delete(cart);
			
		}

		System.out.println("response sent!!!");

		return new ResponseEntity("ORDER SUCCESS", HttpStatus.OK);

	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("admin/allorder")
	public ResponseEntity getAllOrder() throws JsonProcessingException {

		System.out.println("request came for FETCH ALL ORDERS");

		List<Orders> userOrder = orderDao.findAll();

		//OrderDataResponse orderResponse = new OrderDataResponse();

		List<MyOrderResponse> orderDatas = new ArrayList<>();

		for (Orders order : userOrder) {
			MyOrderResponse orderData = new MyOrderResponse();
			orderData.setOrderId(order.getOrderId());
			orderData.setProductDescription(order.getProduct().getDescription());
			orderData.setProductName(order.getProduct().getTitle());
			orderData.setProductImage(order.getProduct().getImageName());
			orderData.setQuantity(order.getQuantity());
			orderData.setOrderDate(order.getOrderDate());
			orderData.setProductId(order.getProduct().getId());

			orderData.setTotalRent(
					String.valueOf(order.getQuantity() * Double.parseDouble(order.getProduct().getRent().toString())));
			orderData.setUserId(order.getUser().getId());
			orderData.setUserName(order.getUser().getFirstName() + " " + order.getUser().getLastName());
			orderData.setUserPhone(order.getUser().getPhoneNo());
			orderData.setAddress(order.getUser().getAddress());

			orderDatas.add(orderData);

		}

		String json = objectMapper.writeValueAsString(orderDatas);

		System.out.println(json);

		System.out.println("response sent !!!");

		return new ResponseEntity(orderDatas, HttpStatus.OK);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("admin/showorder")
	public ResponseEntity getOrdersByOrderId(@RequestParam("orderId") String orderId) throws JsonProcessingException {

		System.out.println("request came for FETCH ORDERS BY ORDER ID : " + orderId);

		List<Orders> userOrder = orderDao.findByOrderId(orderId);

		List<MyOrderResponse> orderDatas = new ArrayList<>();

		for (Orders order : userOrder) {
			MyOrderResponse orderData = new MyOrderResponse();
			orderData.setOrderId(order.getOrderId());
			orderData.setProductDescription(order.getProduct().getDescription());
			orderData.setProductName(order.getProduct().getTitle());
			orderData.setProductImage(order.getProduct().getImageName());
			orderData.setQuantity(order.getQuantity());
			orderData.setOrderDate(order.getOrderDate());
			orderData.setProductId(order.getProduct().getId());
			orderData.setTotalRent(
					String.valueOf(order.getQuantity() * Double.parseDouble(order.getProduct().getRent().toString())));
			orderData.setUserId(order.getUser().getId());
			orderData.setUserName(order.getUser().getFirstName() + " " + order.getUser().getLastName());
			orderData.setUserPhone(order.getUser().getPhoneNo());
			orderData.setAddress(order.getUser().getAddress());

			orderDatas.add(orderData);

		}

		String json = objectMapper.writeValueAsString(orderDatas);

		System.out.println(json);

		System.out.println("response sent !!!");

		return new ResponseEntity(orderDatas, HttpStatus.OK);

	}
}
