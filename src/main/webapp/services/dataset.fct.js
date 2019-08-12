'use strict';

angular
    .module('app.services', [])
	.factory('DatasetService', DatasetService);

    DatasetService.$inject = ['$q', '$filter', '$timeout', '$rootScope', '$http'];

    function DatasetService ($q, $filter, $timeout, $rootScope, $http) {

        var service = {
            getAllDatasets: _getAllDatasets,
            getByIdDataset: _getByIdDataset,
            getAllFormats: _getAllFormats,
            getAllVersions: _getAllVersions,
            getAllCategories: _getAllCategories,
            getByVersion: _getByVersion,
            getPreservacao: _getPreservacao, 
            setSuggestion: _setSuggestion,           
            getAllSuggestions: _getAllSuggestions,           
            getPreviewData: _getPreviewData,           
            getQuery: _getQuery,           
        };

        return service;

        function _getAllDatasets ($callback) {
            return $http({
                    url: apiUrl() + 'open/list_datasets',
                    method: 'GET',
                    data: getCurrentUser()
                });
            console.log(url);
        }

        function _getAllSuggestions ($callback) {
            return $http({
                    url: apiUrl() + 'open/list_suggestions',
                    method: 'GET',
                    data: getCurrentUser()
                });
        }

        function _getPreviewData (id, version) {
            return $http({
                    url: apiUrl() + 'open/preview/'+id+'/version/'+version+'/limit/10',
                    method: 'GET',
                    data: getCurrentUser()
                });
           
        }

        function _getAllCategories ($callback) {
            return $http({
                    url: apiUrl() + 'open/list_categories',
                    method: 'GET',
                    data: getCurrentUser()
                });
        }
        
        function _getAllFormats ($callback) {
            return $http({
                    url: apiUrl() + 'open/list_formats',
                    method: 'GET',
                    data: getCurrentUser()
                });
        }

        function _getByIdDataset (id) {
                var urlCompleta = apiUrl() + "open/about/" + id + "/";
                return $http.get(urlCompleta);
        };
        

        function _getAllVersions (dataset) {
            return $http({
                    url: apiUrl() + 'open/' + dataset + '/list_versions',
                    method: 'GET'
                });
        }; 

        function _getByVersion (dataset, version) {
            return $http({
                    url: apiUrl() + 'open/' + dataset + "/about/" + version,
                    method: 'GET'
                });
        }; 
        
        function _getPreservacao (dataset) {
            return $http({
                    url: apiUrl() + 'open/' + dataset + "/verificar_preservacao",
                    method: 'GET'
                });
        }; 

        function _setSuggestion(suggestion, $callback) {
            $http({
                url: apiUrl() + 'open/insert_suggestion',
                method: 'POST',
                data: suggestion
            }).then(
                function (response) {
                    $callback(true);     
                }, function() {
                    $callback(false);
                });
        };

        function _getQuery(query, $callback) {
            $http({
                url: apiUrl() + 'open/execute_query',
                method: 'POST',
                data: query
            }).then(
                function (response) {
                    $callback(true, response);     
                }, function(data) {
                    $callback(false, data);
                });
        };

        

    }

