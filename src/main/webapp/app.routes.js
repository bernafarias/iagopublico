'use strict';

angular
    .module('app.routes', ['ngRoute'])
    .config(config);

function config ($routeProvider) {
    $routeProvider.
        when('/', {
            templateUrl: 'sections/home/home.tpl.html',
            controller: 'HomeController as home'
        })
        .when('/form', {
            templateUrl: 'sections/form/form.tpl.html',
            controller: 'FormController as vm'
        })
        .when('/sobre', {
            templateUrl: 'sections/sobre/sobre.tpl.html',
            controller: 'SobreController as sobre'
        })
        .when('/conjuntos_dados', {
            templateUrl: 'sections/conjuntos_dados/conjuntos_dados.tpl.html',
            controller: 'ConjuntosController as conjuntos_dados'  
        })
        .when('/indicadores', {
            templateUrl: 'sections/indicador/indicador.tpl.html',
            controller: 'IndicadorController as indicador'  
        })
        .when('/indicadores/censo_2017', {
            templateUrl: 'sections/censo_2017/censo_2017.tpl.html',
            controller: 'CensoController as indicadores'  
        })
        .when('/search', {
            templateUrl: 'sections/search/search.tpl.html',
            controller: 'SearchController as search'
        })
        .when('/search/:query', {
            templateUrl: 'sections/search/search.tpl.html',
            controller: 'SearchController as search'
        })
       
        .when('/contato', {
            templateUrl: 'sections/contato/contato.tpl.html',
            controller: 'ContatoController as contato'
           
        })
        .when('/details/:collector_dataset', {
            templateUrl: 'sections/details/details.tpl.html',
            controller: 'DetailsController as vm'
        })
        .when('/details/:collector_dataset/como_usar_a_api', {
            templateUrl: 'sections/como_usar_a_api/como_usar_a_api.tpl.html',
            controller: 'ComoUsarApiController as vm'
        })
        .when('/preview/:collector_dataset', {
            templateUrl: 'sections/preview/preview.tpl.html',
            controller: 'PreviewController as vm'
        })
        .when('/solicitacoes_conjuntos_dados', {
            templateUrl: 'sections/solicitacoes_conjuntos_de_dados/solicitacoes_conjuntos_de_dados.tpl.html',
            controller: 'SolicitacoesController as solicitacoes'  
        })
        .otherwise({
            redirectTo: '/'
        });
}