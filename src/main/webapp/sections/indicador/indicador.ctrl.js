'use strict';
angular
    .module('app.core')
    .controller('IndicadorController', function($scope, PageValues, $http, DatasetService) {
        PageValues.title = "Conjuntos de Dados";
        PageValues.description = "";
        var vm = this;

        vm.rowCollection = [];    
        vm.rowCategories = [];    
        vm.getters = [];
        vm.publishers = [];
        vm.keywords = [];

        vm.rowCollectionPageSize = 1;
        
        DatasetService.getAllDatasets().success(
          function(data, status, headers, config) {
              
              vm.rowCollection = angular.fromJson(data); 
              
              var keywordsAux = [];
              
              for( var i = 0; i < vm.rowCollection.length; i++)
              {
                keywordsAux = keywordsAux.concat(vm.rowCollection[i].keywords.split(","));
                
                if(vm.publishers.indexOf(vm.rowCollection[i].publisher) == -1)
                {
                  vm.publishers.push(vm.rowCollection[i].publisher);
                }   
              }
              
              for( var i = 0; i < keywordsAux.length; i++)
              {
                if(vm.keywords.indexOf(keywordsAux[i]) == -1)
                {
                  vm.keywords.push(keywordsAux[i]);
                }     
              }   

          }).error(function(data, status, headers, config) {         
            
            console.log("ERRO");
            console.log(config);
          
        });

        DatasetService.getAllCategories().success(
          function(data, status, headers, config) {

              vm.rowCategories = angular.fromJson(data);  

          }).error(function(data, status, headers, config) {         
            
            console.log("ERRO");
            console.log(config);
          
        });

        vm.getters={

            collector_dataset: function (value) {
                //this will sort by the length of the first name string
                return value.collector_dataset.length;
            }
        }

        
    });