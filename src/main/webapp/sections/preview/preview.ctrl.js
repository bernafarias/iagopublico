'use strict';
angular
    .module('app.core')
    .controller('PreviewController', function($route, $scope, PageValues, $http, DatasetService, i18nService, uiGridConstants) {
        //Set page title and description
        PageValues.title = "HOME";
        PageValues.description = "Learn AngularJS using best practice real world examples.";
        
         var vm = this;
        vm.datasetURI             = $route.current.params.collector_dataset;
        vm.download               = {};
        vm.api                    = {};
        vm.linkMetadadosApi       = getApiMetadados();
        vm.linkUri                = getUriDownload();
        vm.linkUriApi             = getUriAPI();
        vm.datasetAtualizar       = {};
        vm.linkVersoes            = getApiVersoes();
        vm.linkVersaoEspecifica   = getApiVersaoEspecifica();
        vm.linkUriAtual           = getUriAtual();
        vm.rowCollection          = [];   
        vm.lastVersion            = [];

        DatasetService.getPreservacao(vm.datasetURI).success(
          function(data) {     
            //console.log(data); 
              if (data==true) {
                 window.location = "410.html";
              } 
          }); 

        DatasetService.getAllFormats().success(
          function(data, status, headers, config) {      

              vm.download = angular.fromJson(data);

          }).error(function(data, status, headers, config) {      

              console.log("ERRO Formatos");
             //toastr.error("Erro ao carregar os Formatos de dados", 'Erro!'); //success,info,warning
        });   

        function getUriAtual() {
          return window.location.href;
        }

        function getUriDownload(){
          return apiUrl() + "open/" + vm.datasetURI + "/format/";
        };
        
        function getUriAPI(){
            return apiUrl() + "open/" + vm.datasetURI+ "/";
        };

        function getApiMetadados(){
          return apiUrl() + "open/about/" + vm.datasetURI + "/";
        }

        function getApiVersoes() {
          return apiUrl() + "open/" + vm.datasetURI + "/list_versions";
        }

        function getApiVersaoEspecifica() {
          return apiUrl() + "open/" + vm.datasetURI + "/version";
        }

        function getLastVersion() {
            DatasetService.getAllVersions(vm.datasetURI).success(
              function(data, status, headers, config) {
                vm.lastVersion = angular.fromJson(data); 
            });
        }

        DatasetService.getByIdDataset(vm.datasetURI).success(
          function(data, status, headers, config) {
             
              vm.rowCollectionDetails = data;
              vm.proxAtualizacao = vm.rowCollectionDetails.nextUpdate;
              vm.StructuralMetadata = vm.rowCollectionDetails.attributesDescription;
              vm.frequency = (vm.rowCollectionDetails.frequency).replace("_", " ");
              getLastVersion();

              DatasetService.getPreviewData(vm.datasetURI, vm.rowCollectionDetails.lastVersion).success(
                function(data, status, headers, config) {
                  vm.previewData = angular.fromJson(data); 
                }).error(function(data, status, headers, config) {         
                  console.log("ERRO Preview Data");
              });

          }).error(function(data, status, headers, config) {      
            console.log("ERRO Conjunto de Dados");
             //toastr.error("Erro ao carregar os dados dos Conjuntos de Dados", 'Erro!'); //success,info,warning

        });    
          


        i18nService.setCurrentLang('pt-br');
        vm.lang = 'pt-br';

        vm.gridOptions = {
          data: vm.previewData, //required parameter - array with data
          //optional parameter - start sort options
          i18n: "pt-br",
          showGridFooter: true,
          showColumnFooter: true,
          sort: {
          },
          onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
          }
        };     

        $scope.toggleFooter = function() {
          $scope.gridOptions.showGridFooter = !$scope.gridOptions.showGridFooter;
          $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.OPTIONS);
        };
         
        $scope.toggleColumnFooter = function() {
          $scope.gridOptions.showColumnFooter = !$scope.gridOptions.showColumnFooter;
          $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.OPTIONS);
        };
        
        DatasetService.getAllVersions(vm.datasetURI).success(
          function(data, status, headers, config) {
              
              vm.rowCollection = angular.fromJson(data); 
             // toastr.success("Versões carregadas", 'Sucesso!');

          }).error(function(data, status, headers, config) {         
            console.log("ERRO Versões");
            //toastr.error("Erro ao carregar as versões do conjunto de dados", 'Erro!'); //success,info,warning
            
        });


    });
