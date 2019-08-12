'use strict';
angular
    .module('app.core')
    .filter('encodeURIComponent', function() {
      return window.encodeURIComponent;
    })
    .controller('ComoUsarApiController', function($route, $scope, $http) {
      
      $scope.aListConjuntos = true;
      $scope.aListMetadados = true;
      $scope.aSubconjunto = true;
      $scope.aDadosFormato = true;
      $scope.aListSugestoes = true;
      $scope.aVersionList = true;
      $scope.aVersion = true;

    });
