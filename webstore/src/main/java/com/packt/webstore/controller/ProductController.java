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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.packt.webstore.domain.Product;
import com.packt.webstore.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

	private Path rootLocation;
	
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
	
	  @RequestMapping(value = "/add", method = RequestMethod.GET)
	  public String getAddNewProductForm(Model model) {
	     Product newProduct = new Product();
	     model.addAttribute("newProduct", newProduct);
	     return "addProduct";
	  }
	  @RequestMapping(value = "/add", method = RequestMethod.POST)
	  public String processAddNewProductForm(@ModelAttribute("newProduct")
	Product productToBeAdded, BindingResult result, HttpServletRequest request) {
		  String[] suppressedFields = result.getSuppressedFields();
		  if (
		suppressedFields.length > 0
		) {
		    throw new RuntimeException("Attempting to bind disallowed fields: "
		+ StringUtils.arrayToCommaDelimitedString(suppressedFields));
		  }
		  
		  MultipartFile productImage =productToBeAdded.getProductImage();
		  String rootDirectory
		  =request.getSession().getServletContext().getRealPath("/");
		  File teste = new File(rootDirectory+"teste.txt");
		  InputStream input = request.getSession().getServletContext().getResourceAsStream("/index.jsp"); // Right!
		  this.rootLocation = Paths.get("upload-dir");
		  String filename = StringUtils.cleanPath(productImage.getOriginalFilename());
		  try {
			Files.copy(productImage.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  System.out.println("Product Image " +productImage);
		  
		  if (productImage!=null && !productImage.isEmpty()) {
		    try {
		  productImage.transferTo(new File(rootDirectory+"resources\\images\\"+productToBeAdded.getProductId() + ".png"));
		    } catch (Exception e) {
		      throw new RuntimeException("Product Image saving failed",e);
		    }
		  }
		  
		  MultipartFile manualPDF =productToBeAdded.getManualPDF();
		  
		  System.out.println("Manual PDF " +manualPDF);
		  if (manualPDF!=null && !manualPDF.isEmpty()) {
		    try {
		    	manualPDF.transferTo(new File(rootDirectory+"resources\\pdf\\"+productToBeAdded.getManualPDF() + ".pdf"));
		    } catch (Exception e) {
		      throw new RuntimeException("Manual PDF saving failed",e);
		    }
		  }
		  
	     productService.addProduct(productToBeAdded);
	     return "redirect:/products";
	  }
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields("productId","name","unitPrice","description","manufacturer","category","unitsInStock", "productImage", "manualPDF");
	   binder.setDisallowedFields("unitsInOrder", "discontinued");
	}
}