package com.packt.webstore.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.packt.webstore.domain.Product;

public class ProductImageValidator implements Validator {

	private long allowedSize = 4194304;
	
	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub		
		Product product = (Product)target;
		System.out.println("------++++++++++++++------------"+product.getProductImage().getSize());
		if(product.getProductImage() != null && product.getProductImage().getSize() > allowedSize)
			errors.rejectValue("productImage", "com.packt.webstore.validator.ProductImageValidator.message");
	}

}
