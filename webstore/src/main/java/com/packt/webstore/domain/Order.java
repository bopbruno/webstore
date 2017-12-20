package com.packt.webstore.domain;

import java.io.Serializable;

public class Order  implements Serializable{

  private static final long serialVersionUID = -3560539622417210365L;

  private Long orderId;
  private Cart cart;
  private Customer customer;
  private ShippingDetail shippingDetail;

  public Order() {
    this.customer = new Customer();
    this.shippingDetail = new ShippingDetail();
  }

  @Override
  public int hashCode() {
	  return this.orderId.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
	  if(obj == this)
		  return true;
	  if(obj == null)
		  return false;
	  if(!(obj instanceof Customer))
		  return false;
	  Order order = (Order) obj;
	  if(!order.getOrderId().equals(this.getOrderId()))
		  return false;
	  return true;
  }

public Long getOrderId() {
	return orderId;
}

public void setOrderId(Long orderId) {
	this.orderId = orderId;
}

public Cart getCart() {
	return cart;
}

public void setCart(Cart cart) {
	this.cart = cart;
}

public Customer getCustomer() {
	return customer;
}

public void setCustomer(Customer customer) {
	this.customer = customer;
}

public ShippingDetail getShippingDetail() {
	return shippingDetail;
}

public void setShippingDetail(ShippingDetail shippingDetail) {
	this.shippingDetail = shippingDetail;
}

  // add getters and setters for all the fields here.
  // Override equals and hashCode based on orderId field.
     // the code for the same is available in the code bundle which can be downloaded from www.packtpub.com/support
}