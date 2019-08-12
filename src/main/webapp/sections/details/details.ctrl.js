'use strict';
angular
    .module('app.core')
    .filter('encodeURIComponent', function() {
      return window.encodeURIComponent;
    })
    .controller('DetailsController', function($route, $scope, PageValues, $http, DatasetService, $timeout,$q, $log) {
        //Set page title and description
        PageValues.title = "HOME";
        PageValues.description = "Learn AngularJS using best practice real world examples.";
        


         var vm = this;
        vm.datasetURI             = $route.current.params.collector_dataset;
        vm.download               = {};
        vm.api                    = {};
        vm.linkCustomAPI          = getUriCustomApi();
        vm.linkMetadadosApi       = getApiMetadados();
        vm.linkUri                = getUriDownload();
        vm.linkUriApiDocumentacao = getUriAPIDocumentacao();
        vm.datasetAtualizar       = {};
        vm.linkVersoes            = getApiVersoes();
        vm.linkVersaoEspecifica   = getApiVersaoEspecifica();
        vm.linkUriAtual           = getUriAtual();
        vm.linkUriAtualEncode     = getUriAtualEncode();
        vm.previewData            = [];
        vm.rowCollection          = [];   
        vm.lastVersion            = [];
        vm.lastVersionValue;
        vm.frequency;
        vm.fieldsCount;

        $scope.scrollTo = function(id) {
          var elmnt = document.getElementById(id);
          elmnt.scrollIntoView();
        }

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

        function getUriAtualEncode() {
          return encodeURI(window.location.href);
        }

        function getUriCustomApi() {
          return apiUrl() + "open/execute_query";
        }

        function getUriDownload(){
          return apiUrl() + "open/" + vm.datasetURI + "/format/";
        }

        function getUriAPIDocumentacao(){
            return apiUrl() + "./documentacao/";
        }

        function getApiMetadados(){
          return apiUrl() + "open/dcat/" + vm.datasetURI + "/";
        }

        function getApiVersoes() {
          return apiUrl() + "open/" + vm.datasetURI + "/list_versions";
        }

        function getApiVersaoEspecifica() {
          return apiUrl() + "open/" + vm.datasetURI + "/version";
        }
        
        function getFrequency(f)
        { 	
        	switch(f)
        	{
        		case "POR_HORA":
        			vm.frequency = "Por Hora";
        			break;
        			
        		case "DIARIO":
        			vm.frequency = "Diário";
        			break;
        			
        		case "SEMANAL":
        			vm.frequency = "Semanal";
        			break;
        			
        		case "MENSAL":
        			vm.frequency = "Mensal";
        			break;
        			
        		case "SEMESTRAL":
        			vm.frequency = "Semestral";
        			break;	
        			
        		case "ANUAL":
        			vm.frequency = "Anual";
        			break;
        			
        		case "STATIC":
        			vm.frequency = "Estático";
        			break;
        	}
        }

        function getLastVersion() {
            DatasetService.getAllVersions(vm.datasetURI).success(
              function(data, status, headers, config) {
                vm.lastVersion = angular.fromJson(data); 
            });
        }

        //Campos
        $scope.choices = [];
        $scope.addNewChoice = function() {
          var newItemNo = $scope.choices.length+1;
          $scope.choices.push({});
        };          
        $scope.addNewChoice();
        $scope.removeChoice = function() {
          var lastItem = $scope.choices.length-1;
          $scope.choices.splice(lastItem);
        };

        //Verifica os campos digitados para montar a consulta de forma correta
        $scope.verificaParametros = function() {
          $scope.subconjunto.parameters = [];
          var jsonAdd = "";
          var obj = {};
          $scope.choices.forEach(function(input) {
            var parametro = input.parameter;
            var valor = input.value;
            obj[parametro] = valor;
          });
          $scope.subconjunto.parameters = obj;
        }

        $scope.retornoConsulta = {'Data': 'Consulta não realizada'};
        //Envia a consulta
        $scope.submitFormSubconjunto = function(subconjunto) {
            DatasetService.getQuery(subconjunto,function (result, data, status, headers, config) {
              if (result === true) {
                console.log("Sucesso na consulta!");
                $scope.retornoConsulta = data.data;
                var elmnt = document.getElementById("retornoConsulta");
                elmnt.scrollIntoView();
              } else {
                $scope.retornoConsulta = data;
                console.log("Problemas na consulta.");
              }
            });
        };

        DatasetService.getByIdDataset(vm.datasetURI).success(
          function(data, status, headers, config) {
             
              vm.rowCollectionDetails = data;
              vm.proxAtualizacao = vm.rowCollectionDetails.nextUpdate;
              vm.StructuralMetadata = vm.rowCollectionDetails.fieldDescriptions;
              //vm.frequency = (vm.rowCollectionDetails.frequency).replace("_", " ");
              vm.lastVersionValue = vm.rowCollectionDetails.lastVersion;
              getLastVersion();
              getFrequency(vm.rowCollectionDetails.frequency);
              $scope.subconjunto = {'datasetName': vm.datasetURI, 'version' : vm.lastVersionValue, 'page': 1, 'limit':1};
              vm.linkUriApi = getUriAPI(vm.lastVersionValue);
              vm.states        = loadStates(vm.StructuralMetadata);
              vm.aStructuralMetadata = JSON.parse(JSON.stringify(vm.rowCollectionDetails.fieldDescriptions));

              
              //toastr.success("Dados carregados", 'Sucesso!');
              
              DatasetService.getPreviewData(vm.datasetURI, vm.lastVersionValue).success(
                      function(data, status, headers, config) {
                        vm.previewData = angular.fromJson(data); 
                        vm.fieldsCount = Object.keys(vm.previewData[0]).length;
                      }).error(function(data, status, headers, config) {         
                        console.log("ERRO Preview Data");
                    });

          }).error(function(data, status, headers, config) {      
            console.log("ERRO Conjunto de Dados");
             //toastr.error("Erro ao carregar os dados dos Conjuntos de Dados", 'Erro!'); //success,info,warning

        });    

   
               
        DatasetService.getAllVersions(vm.datasetURI).success(
          function(data, status, headers, config) {
              
              vm.rowCollection = angular.fromJson(data); 
             // toastr.success("Versões carregadas", 'Sucesso!');

          }).error(function(data, status, headers, config) {         
            console.log("ERRO Versões");
            //toastr.error("Erro ao carregar as versões do conjunto de dados", 'Erro!'); //success,info,warning
            
        });

        function getUriAPI(version){
            return apiUrl() + "open/preview/" + vm.datasetURI+ "/version/"+version+"/limit/100";
        }

         vm.simulateQuery = true;
            vm.isDisabled    = false;
            
            // list of states to be displayed
            vm.querySearch   = querySearch;
            vm.selectedItemChange = selectedItemChange;
            vm.searchTextChange   = searchTextChange;
            vm.newState = newState;
            
            function newState(state) {
               alert("This functionality is yet to be implemented!");
            }
            
            function querySearch (query) {
               var results = query ? vm.states.filter( createFilterFor(query) ) :
                  vm.states, deferred;
                  
               if (vm.simulateQuery) {
                  deferred = $q.defer();
                     
                  $timeout(function () { 
                     deferred.resolve( results ); 
                  }, 
                  Math.random() * 1000, false);
                  return deferred.promise;
               } else {
                  return results;
               }
            }
            
            function searchTextChange(text) {
               //$log.info('Text changed to ' + text);
            }
            
            function selectedItemChange(item) {
              if (item) {
               for (var j=0; j < vm.aStructuralMetadata.length; ++j) {
                 if (vm.aStructuralMetadata[j].fieldName == item.value) {
                    $scope.vm.suggestionText = vm.aStructuralMetadata[j].title + " (" + vm.aStructuralMetadata[j].dataType + "): " + vm.aStructuralMetadata[j].description;
                 }
                } 
              } else {
                $scope.vm.suggestionText = '';
              }
            }
            
            function loadStates(dadosEstruturais) {
              var allStates = '';
              dadosEstruturais.forEach(function(element, index, array){ 
                if (allStates=='') {
                  allStates = '' + element.fieldName + '';
                } else {
                  allStates = allStates + ', ' + element.fieldName + '';
                }
              });
             
              return allStates.split(/, +/g).map( function (state) {
                return {
                  value: state,
                  display: state
                };
              });
            }
                       
            //filter function for search query
            function createFilterFor(query) {
               var lowercaseQuery = angular.lowercase(query);
               return function filterFn(state) {
                  return (state.value.indexOf(lowercaseQuery) === 0);
               };
            }


    });
