'use strict';
angular
    .module('app.core')
    .controller('SolicitacoesController', function($scope, PageValues, $http, DatasetService) {
        PageValues.title = "Solicitações de Conjuntos de Dados";
        PageValues.description = "";
        var vm = this;

        vm.rowCollection = [];    
        vm.rowCategories = [];    
        vm.getters = [];

        vm.rowCollectionPageSize = 1;
        
        DatasetService.getAllSuggestions().success(
          function(data, status, headers, config) {
              
              vm.rowCollection = angular.fromJson(data);  

          }).error(function(data, status, headers, config) {         
            
            console.log("ERRO");
            console.log(config);
          
        });

    });