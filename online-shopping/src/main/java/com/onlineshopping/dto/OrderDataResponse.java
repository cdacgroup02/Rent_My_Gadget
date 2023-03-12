package com.onlineshopping.dto;

import java.util.List;

public class OrderDataResponse {
	
	private List<MyOrderResponse> orderResponse;
	
	private String totalCartRent;

	public List<MyOrderResponse> getOrderResponse() {
		return orderResponse;
	}

	public void setOrderResponse(List<MyOrderResponse> orderResponse) {
		this.orderResponse = orderResponse;
	}

	public String getTotalCartRent() {
		return totalCartRent;
	}

	public void setTotalCartRent(String totalCartRent ) {
		this.totalCartRent = totalCartRent;
	}

	@Override
	public String toString() {
		return "OrderDataResponse [orderResponse=" + orderResponse + ", totalCartPrice=" + totalCartRent + "]";
	}
	
}
