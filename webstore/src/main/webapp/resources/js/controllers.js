var cartApp = angular.module('cartApp', []);

cartApp.controller('cartCtrl',  function ($scope, $http) {

  $scope.refreshCart = function(cartId) {
    $http.get('/webstore/rest/cart/'+$scope.cartId)
    .success(function(data) {
      $scope.cart = data;
    });
  };

  $scope.clearCart = function() {
	  console.log('clearcart loaded brn');
    $http.delete('/webstore/rest/cart/'+$scope.cartId)
    .success($scope.refreshCart($scope.cartId));

  };

  $scope.initCartId = function(cartId) {
	  console.log('initCartId loaded');
	  console.log(cartId);
    $scope.cartId=cartId;
    $scope.refreshCart($scope.cartId);
  };

  $scope.addToCart = function(productId) {
	  console.log('addToCart loaded brn');
	    $http.put('/webstore/rest/cart/add/'+productId)
	    .success(function(data) {
	      $scope.refreshCart($http.get('/webstore/rest/cart/cartId'));
	      alert("Product Successfully added to the Cart!");
	    });
	  };
	  $scope.removeFromCart = function(productId) {
		  console.log('removeFromCart loaded')
	    $http.put('/webstore/rest/cart/remove/'+productId)
	    .success(function(data) {
	      $scope.refreshCart($http.get('/webstore/rest/cart/cartId'));
	    });
	  };
	});