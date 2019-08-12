'use strict';
angular
    .module('app.core')
    .controller('HomeController', function($scope, PageValues, $http, DatasetService, $location) {
        //Set page title and description
        PageValues.title = "HOME";
        PageValues.description = "";

        //Setup view model object
        var vm = this;
        vm.rowCollection = [];    
        vm.getters = [];

        vm.rowCollectionPageSize = 1;
        
        DatasetService.getAllDatasets().success(
          function(data, status, headers, config) {
              
              //console.log(angular.fromJson(data));
              vm.rowCollection = angular.fromJson(data);  
              //toastr.success("Dados carregados", 'Sucesso!');

          }).error(function(data, status, headers, config) {         
            
            console.log("ERRO");
            console.log(config);
            //toastr.error("Erro ao carregar os dados dos Conjuntos de Dados", 'Erro!'); //success,info,warning
            
        });

        vm.getters={

            collector_dataset: function (value) {
                //this will sort by the length of the first name string
                return value.collector_dataset.length;
            }
        }

        $scope.update = function(suggestion) {
            DatasetService.setSuggestion(suggestion,function (result) {
              if (result === true) {
                alert("Inserido com sucesso!");
                $location.path('/iago');
              } else {
                alert("Problema ao inserir.");
              }
            });
        };

    });
