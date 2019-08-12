'use strict';
angular
    .module('app.core')
    // grafico #1
    .directive('hcChart', function () {
        return {
            restrict: 'E',
            template: '<div></div>',
            scope: {
                options: '='
            },
            link: function (scope, element) {
                Highcharts.chart(element[0], scope.options);
            }
        };
    })
    // grafico #2    
    .directive('hcPieChart', function () {
        return {
            restrict: 'E',
            template: '<div></div>',
            scope: {
                title: '@',
                data: '='
            },
            link: function (scope, element) {
                Highcharts.chart(element[0], {
                    chart: {
                        type: 'pie'
                    },
                    title: {
                        text: 'Qtd de Vagas por Forma de Ingresso'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f} %'
                            }
                        }
                    },
                    series: [{
                        data: scope.data
                    }]
                });
            }
        };
    })
    // grafico #3
    .directive('hcBar', function () {
        return {
            restrict: 'E',
            template: '<div></div>',
            scope: {
                options: '='
            },
            link: function (scope, element) {
                Highcharts.chart(element[0], scope.options);
            }
        };
    })
    // grafico #4    
    .directive('hcPie', function () {
        return {
            restrict: 'E',
            template: '<div></div>',
            scope: {
                title: '@',
                data: '='
            },
            link: function (scope, element) {
                Highcharts.chart(element[0], {
                    chart: {
                        type: 'pie'
                    },
                    title: {
                        text: 'Docentes Efetivos x Centro Acadêmico'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f} %'
                            }
                        }
                    },
                    series: [{
                        data: scope.data
                    }]
                });
            }
        };
    })
    // grafico #5    
    .directive('hcPieTitulacao', function () {
        return {
            restrict: 'E',
            template: '<div></div>',
            scope: {
                title: '@',
                data: '='
            },
            link: function (scope, element) {
                Highcharts.chart(element[0], {
                    chart: {
                        type: 'pie'
                    },
                    title: {
                        text: 'Docentes Efetivos x Grau Acadêmico'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f} %'
                            }
                        }
                    },
                    series: [{
                        data: scope.data
                    }]
                });
            }
        };
    })
        
    .controller('CensoController', function($scope, PageValues, $http, $location, $anchorScroll, DatasetService) {
        PageValues.title = "";
        PageValues.description = "";
        var vm = this;

        vm.rowCollection = [];    
        vm.rowCategories = [];    
        vm.getters = [];

        vm.rowCollectionPageSize = 1;
        
        $scope.scrollTo = function(id) {
            $location.hash(id);
            $anchorScroll();
         }

        // dados grafico #1
        $scope.chartOptions = {
            chart: {
                type: 'column'
            },
            title: {
                text: "<p>Evolução da População</p>"
            },
            xAxis: {
                categories: ['2011', '2012', '2013', '2014', '2015', '2016'],
                crosshair: true
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                  '<td style="padding:0"><b>{point.y:.1f} mil</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            series: [{
                name: 'Docentes',
                data: [2.509, 2.594, 2.650, 2.739, 2.834, 2.899],
                color: '#d2152a'

            },
            {
                name: 'Técnicos',
                data: [3.917, 4.134, 4.106, 4.235, 4.233, 5.085],
                color: '#b9b7b7'
            },
            {
                name: 'Discentes',
                data: [39.498, 40.901, 40.522, 46.703, 43.460, 42.448],
                color: '#ed4a5c'
            }
            ]
        };

        // dados grafico #2
        $scope.pieData = [{
            name: "SISU",
            y: 6972,
            sliced: true,
            selected: true,
            color: '#d2152a'
        }, {
            name: "VESTIBULAR",
            y: 142,
            color: '#b9b7b7'
           
        }];

        // dados grafico #3
        $scope.barOptions = {
            chart: {
                type: 'column'
            },
            title: {
                text: "<p>Inscritos x Vagas Ofertadas por Centro</p>"
            },
            xAxis: {
                categories: ['CAA', 'CAC', 'CAV', 'CB', 'CCEN', 'CJ', 'CCS', 'CCSA', 'CE', 'CFCH', 'CIN', 'CTG'],
                crosshair: true
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                  '<td style="padding:0"><b>{point.y:.1f} mil</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            series: [{
                name: 'Inscritos',
                data: [22840, 19230, 11102, 7498, 2153, 6907, 37431, 20339, 6649, 11476, 3127, 8676],
                color: '#d2152a'

            },
            {
                name: 'Vagas',
                data: [1040, 850, 430, 400, 210, 250, 882, 840, 250, 660, 270, 890],
                color: '#b9b7b7'
            }]
        };
        
        // dados grafico #4
        $scope.pie = [{
            name: "CENTRO ACADÊMICO DO AGRESTE",
            y: 286,
            color: '#607D8B'
        }, {
            name: "CENTRO DE ARTES E COMUNICAÇÃO",
            y: 291,
            color: '#b9b7b7'
        },
        {
            name: "CENTRO DE BIOCIÊNCIAS",
            y: 196,
            color: '#EF5350'
        },
        {
            name: "CENTRO DE CIÊNCIAS EXATAS E DA NATUREZA",
            y: 133,
            color: '#FFAB00'
        },
        {
            name: "CENTRO DE CIÊNCIAS JURÍDICAS",
            y: 64,
            color: '#FF80AB'
        },
        {
            name: "CENTRO DE CIÊNCIAS DA SAÚDE",
            y: 470,
            color: '#d2152a',
            sliced: true,
            selected: true,
        },
        {
            name: "CENTRO DE CIÊNCIAS SOCIAIS APLICADAS",
            y: 161,
            color: '#8C9EFF' 
        },
        {
            name: "CENTRO DE EDUCAÇÃO",
            y: 104,
            color: '#EF9A9A'
        },
        {
            name: "CENTRO DE FILOSOFIA E CIÊNCIAS HUMANAS",
            y: 175,
            color: '#000000'
        },
        {
            name: "CENTRO DE INFORMÁTICA",
            y: 86,
            color: '#B71C1C'
        },
        {
            name: "CENTRO DE TECNOLOGIA E GEOCIÊNCIAS",
            y: 340,
            color: '#FFCDD2'
        },
        {
            name: "CENTRO ACADÊMICO DE VITÓRIA",
            y: 142,
            color: '#6D4C41'
        }];

        // dados grafico #5
        $scope.pieTitulacao = [{
            name: "MESTRE",
            y: 341,
            color: '#607D8B'
        }, {
            name: "GRADUDADO",
            y: 25,
            color: '#b9b7b7'
        },
        {
            name: "ESPECIALISTA",
            y: 51,
            color: '#000000'
        },
        {
            name: "DOUTOR",
            y: 2031,
            color: '#d2152a'
        }];

    });