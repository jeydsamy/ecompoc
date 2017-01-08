'use strict';

ecomApp
    .controller('HomeCtrl', ['$scope', '$rootScope', '$window', '$location', 'rsProduct',
        function($scope, $rootScope, $window, $location, rsProduct) {
            
            rsProduct.getList().then(function(resultData) {
            	$scope.products = resultData;
            	console.log($scope.products);
            });
            
            $scope.placeOrder = function(productId){
            	 rsProduct.placeOrder(productId).then(function(resultData) {
                 	$scope.orderConfirmation = resultData.message;
                 	$scope.orderedProductId = productId;
                 	console.log($scope.products);
                 });
            }
        }
    ]);
