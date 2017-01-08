'use strict';

var ecomApp = angular.module('ecomApp', [
    'ngRoute',
    'ngAnimate',
    'ngResource'
]).
config(['$routeProvider', '$compileProvider', function($routeProvider, $compileProvider) {
    $compileProvider.imgSrcSanitizationWhitelist('img/');
    $routeProvider.when('/', {
        templateUrl: '/assets/pages/home.htm',
        controller: 'HomeCtrl'
    });
    
}]);