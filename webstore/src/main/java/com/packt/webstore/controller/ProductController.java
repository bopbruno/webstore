package com.packt.webstore.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.packt.webstore.domain.Product;
import com.packt.webstore.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping
	public String list(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "products";
	}

	@RequestMapping("/all")
	public String allProducts(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "products";
	}

	@RequestMapping("/{category}")
	public String getProductsByCategory(Model model, @PathVariable("category") String productCategory) {
		model.addAttribute("products", productService.getProductsByCategory(productCategory));
		return "products";
	}

	@RequestMapping("/filter/{ByCriteria}")
	public String getProductsByFilter(@MatrixVariable(pathVar = "ByCriteria") Map<String, List<String>> filterParams,
			Model model) {
		model.addAttribute("products", productService.getProductsByFilter(filterParams));
		return "products";
	}
	
	@RequestMapping("/product")
	public String getProductById(@RequestParam("id") String productId,
	Model model) {
	model.addAttribute("product",
	productService.getProductById(productId));
	return "product";
	}
	
	@RequestMapping("/{category}/price{price}")
	public String filterProducts(Model model, @PathVariable("category") String category, @MatrixVariable(pathVar = "price") Map<String, List<String>> filterParams, 
			@RequestParam("manufacturer") String manufacturer) {
		
		Set<Product> productsByCategory = new HashSet<Product>();
		Set<Product> productsByPrice = new HashSet<Product>();
		Set<Product> productsByManufacturer = new HashSet<Product>();
		
		productsByCategory.addAll(productService.getProductsByCategory(category));
		productsByPrice.addAll(productService.getProductsByFilterPrice(filterParams));
		productsByManufacturer.addAll(productService.getProductsByManufacturer(manufacturer));
		
		productsByCategory.retainAll(productsByPrice);
		productsByCategory.retainAll(productsByManufacturer);
		
		model.addAttribute("products", productsByCategory);
		return "products";
	}
}