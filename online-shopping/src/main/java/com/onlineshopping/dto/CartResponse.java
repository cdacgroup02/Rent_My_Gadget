package com.onlineshopping.dto;

import java.util.List;

public class CartResponse {
	
	private String totalCartRent;
	
	private List<CartDataResponse> cartData;

	public String getTotalCartRent() {
		return totalCartRent;
	}

	public void setTotalCartRent(String totalCartPrice) {
		this.totalCartRent = totalCartPrice;
	}

	public List<CartDataResponse> getCartData() {
		return cartData;
	}

	public void setCartData(List<CartDataResponse> cartData) {
		this.cartData = cartData;
	}

	@Override
	public String toString() {
		return "CartResponse [totalCartPrice=" + totalCartRent + ", cartData=" + cartData + "]";
	}

}
