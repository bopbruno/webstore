<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=ISO-8859-
1">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
<title>Products</title>
</head>
<body>
	<section>
		<div class="jumbotron">
			<div class="container">
				<h1>Products</h1>
				<p>Add products</p>
				<a href="<c:url value="/j_spring_security_logout" />" class="btn btn-danger btn-mini pull-right">logout</a>
			</div>
		</div>
	</section>
	<section class="container">
		<form:form modelAttribute="newProduct" class="form-horizontal" enctype="multipart/form-data">
			<fieldset>
				<legend>Add new product</legend>
				<div class="form-group">
					<label class="control-label col-lg-2 col-lg-2" for="productId"><spring:message code=
						"addProduct.form.productId.label"/></label>
					<div class="col-lg-10">
						<form:input id="productId" path="productId" type="text" class="form:input-large" />
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-lg-2" for="name">Name</label>
					<div class="col-lg-10"><form:textarea id="name" path="name" rows = "2"/></div>
				</div>				
				
				<div class="form-group">
					<label class="control-label col-lg-2" for="description">Description</label>
					<div class="col-lg-10"><form:textarea id="description" path="description" rows = "2"/></div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-lg-2" for="unitPrice">Unit Price</label>
					<div class="col-lg-10"><form:textarea id="unitPrice" path="unitPrice" rows = "2"/></div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-lg-2" for="manufacturer">Manufacturer</label>
					<div class="col-lg-10"><form:textarea id="manufacturer" path="manufacturer" rows = "2"/></div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-lg-2" for="category">Category</label>
					<div class="col-lg-10"><form:textarea id="category" path="category" rows = "2"/></div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-lg-2" for="unitsInStock">Units In Stock</label>
					<div class="col-lg-10"><form:textarea id="unitsInStock" path="unitsInStock" rows = "2"/></div>
				</div>
				
				<div class="form-group">
  					<label class="control-label col-lg-2" for="productImage">
						<spring:message code="addProdcut.form.productImage.label"/></label>
  					<div class="col-lg-10">
						<form:input id="productImage" path="productImage" type="file" class="form:input-large" />
  					</div>
				</div>

				<div class="form-group">
  					<label class="control-label col-lg-2" for="manualPDF">
						<spring:message code="addProdcut.form.manualPDF.label"/></label>
  					<div class="col-lg-10">
						<form:input id="manualPDF" path="manualPDF" type="file" class="form:input-large" />
  					</div>
				</div>
				
				<div class="form-group">
					<div class="col-lg-offset-2 col-lg-10">
						<input type="submit" id="btnAdd" class="btn btn-primary"
							value="Add" />
					</div>
				</div>
			</fieldset>
		</form:form>
	</section>
</body>
</html>