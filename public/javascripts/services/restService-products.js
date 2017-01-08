'use strict';

ecomApp.service('rsProduct', ['$http', function($http) {

    this.moduleBaseUrl = "/product/";

    this.getList = function() {
    	console.log('Going to get products');
        var request = $http({
            method: "get",
            callback: "JSON_CALLBACK",
            url: this.moduleBaseUrl + "list",
        });
        var promise = request.then(function(result) {
            return result.data;
        }, function errorCallback(response) {
            if (response.status == 401) {
                return "Error occured.";
            }
            return "Unknown error occured";
        });
        return promise;

    }
    
    this.placeOrder = function(productId){
    	console.log('Going to Order :' + productId);
        var request = $http({
            method: "get",
            callback: "JSON_CALLBACK",
            url: this.moduleBaseUrl + "placeOrder/" + productId,
            params: {
            	productId: productId
            }
        });
        var promise = request.then(function(result) {
            return result.data;
        }, function errorCallback(response) {
            if (response.status == 401) {
                return "Error occured.";
            }
            return "Unknown error occured";
        });
        return promise;
    }
}]);