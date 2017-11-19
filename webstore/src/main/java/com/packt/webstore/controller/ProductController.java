package com.packt.webstore.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.packt.webstore.domain.Product;
import com.packt.webstore.exception.NoProductsFoundUnderCategoryException;
import com.packt.webstore.exception.ProductNotFoundException;
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
		List<Product> products = productService.getProductsByCategory(productCategory);
		
		if (products == null || products.isEmpty()) {
		   throw new NoProductsFoundUnderCategoryException();
		}
		
		model.addAttribute("products", products);
		return "products";
	}

	@RequestMapping("/filter/{ByCriteria}")
	public String getProductsByFilter(@MatrixVariable(pathVar = "ByCriteria") Map<String, List<String>> filterParams,
			Model model) {
		model.addAttribute("products", productService.getProductsByFilter(filterParams));
		return "products";
	}

	@RequestMapping("/product")
	public String getProductById(@RequestParam("id") String productId, Model model) {
		model.addAttribute("product", productService.getProductById(productId));
		return "product";
	}

	@RequestMapping("/{category}/price{price}")
	public String filterProducts(Model model, @PathVariable("category") String category,
			@MatrixVariable(pathVar = "price") Map<String, List<String>> filterParams,
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

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getAddNewProductForm(Model model) {
		Product newProduct = new Product();
		model.addAttribute("newProduct", newProduct);
		return "addProduct";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String processAddNewProductForm(@ModelAttribute("newProduct") Product productToBeAdded, BindingResult result,
			HttpServletRequest request) {
		String[] suppressedFields = result.getSuppressedFields();
		if (suppressedFields.length > 0) {
			throw new RuntimeException("Attempting to bind disallowed fields: "
					+ StringUtils.arrayToCommaDelimitedString(suppressedFields));
		}
		
		String rootDirectory =request.getSession().getServletContext().getRealPath("/");
		
		//copy upload image to resourse image folder
		MultipartFile productImage = productToBeAdded.getProductImage();
		
		if (productImage!=null && !productImage.isEmpty()) {
		  try {
		    productImage.transferTo(new File(rootDirectory+"resources\\images\\"+productToBeAdded.getProductId() + ".png"));
		  } catch (Exception e) {
		    throw new RuntimeException("Product Image saving failed",e);
		  }
		}
		
		//copy upload manual to resourse pdf folder
				MultipartFile productManual = productToBeAdded.getManualPDF();
				
				if (productManual!=null && !productManual.isEmpty()) {
				  try {
					  productManual.transferTo(new File(rootDirectory+"resources\\pdf\\"+productToBeAdded.getProductId() + ".pdf"));
				  } catch (Exception e) {
				    throw new RuntimeException("Product manual saving failed",e);
				  }
				}

		productService.addProduct(productToBeAdded);
		return "redirect:/products";
	}

	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields("productId", "name", "unitPrice", "description", "manufacturer", "category",
				"unitsInStock", "productImage", "manualPDF","language");
		binder.setDisallowedFields("unitsInOrder", "discontinued");
	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ModelAndView handleError(HttpServletRequest req,ProductNotFoundException exception) {
	  ModelAndView mav = new ModelAndView();
	  mav.addObject("invalidProductId", exception.getProductId());
	  mav.addObject("exception", exception);
	  mav.addObject("url",req.getRequestURL()+"?"+req.getQueryString());
	  mav.setViewName("productNotFound");
	  return mav;
	}
}