package com.packt.webstore.validator;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<Category, String>{

	List<String> allowedCategories;
	
	@Override
	public void initialize(Category arg0) {
		// TODO Auto-generated method stub
		allowedCategories = new ArrayList<>();
		allowedCategories.add("laptops");
		allowedCategories.add("celulares");
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext contex) {
		if(this.allowedCategories.contains(value.toLowerCase()))
			return true;
		else
			return false;
	}

}
