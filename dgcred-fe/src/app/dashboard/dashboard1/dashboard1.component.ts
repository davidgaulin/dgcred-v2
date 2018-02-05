import { Component } from '@angular/core';
import * as Chartist from 'chartist';
import * as tooltip from 'chartist-plugin-tooltip';
import { ChartType, ChartEvent } from "ng-chartist/dist/chartist.component";

import { User } from '../../_models/user';
import { RateHubService } from '../../_services/index';
import { UserService } from '../../_services/index'
import { FinancialGraphService } from '../../_services/index';

import { GraphSerie } from '../../_models/graphserie';

import { PropertyService } from '../../_services/index';

import { MapMarker } from '../../_models/mapmarker';
import { PropertyMapGoogle } from '../../propertymap/property-map-google.component';


//declare var require: any;
declare function require(path: string): any;
require('chartist-plugin-tooltip');
const data: any = require('../../shared/data/chartist.json');

export interface Chart {
    type: ChartType;
    data: Chartist.IChartistData;
    options?: any;
    responsiveOptions?: any;
    events?: ChartEvent;
}

@Component({
    selector: 'app-dashboard1',
    templateUrl: './dashboard1.component.html',
    styleUrls: ['./dashboard1.component.scss']
})

export class Dashboard1Component {

    public isLoading = false;
    public user: User;
    public canadianRateYears: string[] = ["5", "4", "3", "2", "1"];

    public rates: any;

    public month: any;
    public year: any;

    public errorMessage: string;

    public loanBalancePrev: GraphSerie[];
    public loanValueCapitalPrev: GraphSerie[];

    public loanBalancePrevisionData: any;
    public loanValueCapitalPrevisionData: any;

    public propertyMidPoint: MapMarker = new MapMarker();
    public midPoint: MapMarker;

    public properties: Array<any>;
    public markers: MapMarker[] = [];

    public lineColors: string[] = ['#88C4EE', '#ccc', '#660066', '#0066ff', '#00ffff',
        '#ff0000', '#800000', '#ff3399', '#ff9933', '#9933ff',
        '#666699', '#006666', '#333300', '#003366', '#ffff66']



    // Line area chart configuration Starts
    lineArea: Chart = {
        type: 'Line',
        data: data['lineAreaDashboard'],
        options: {
            low: 0,
            showArea: true,
            fullWidth: false,
            onlyInteger: true,
            axisY: {
                low: 0,
                scaleMinSpace: 50,
            },
            axisX: {
                showGrid: false
            }
        },
        events: {
            created(data: any): void {
                var defs = data.svg.elem('defs');
                defs.elem('linearGradient', {
                    id: 'gradient',
                    x1: 0,
                    y1: 1,
                    x2: 1,
                    y2: 0
                }).elem('stop', {
                    offset: 0,
                    'stop-color': 'rgba(0, 201, 255, 1)'
                }).parent().elem('stop', {
                    offset: 1,
                    'stop-color': 'rgba(146, 254, 157, 1)'
                });

                defs.elem('linearGradient', {
                    id: 'gradient1',
                    x1: 0,
                    y1: 1,
                    x2: 1,
                    y2: 0
                }).elem('stop', {
                    offset: 0,
                    'stop-color': 'rgba(132, 60, 247, 1)'
                }).parent().elem('stop', {
                    offset: 1,
                    'stop-color': 'rgba(56, 184, 242, 1)'
                });
            },

        },
    };
    // Line area chart configuration Ends

    // Stacked Bar chart configuration Starts
    Stackbarchart: Chart = {
        type: 'Bar',
        data: data['Stackbarchart'],
        options: {
            stackBars: true,
            fullWidth: true,
            axisX: {
                showGrid: false,
            },
            axisY: {
                showGrid: false,
                showLabel: false,
                offset: 0
            },
            chartPadding: 30
        },
        events: {
            created(data: any): void {
                var defs = data.svg.elem('defs');
                defs.elem('linearGradient', {
                    id: 'linear',
                    x1: 0,
                    y1: 1,
                    x2: 0,
                    y2: 0
                }).elem('stop', {
                    offset: 0,
                    'stop-color': 'rgba(0, 201, 255,1)'
                }).parent().elem('stop', {
                    offset: 1,
                    'stop-color': 'rgba(17,228,183, 1)'
                });
            },
            draw(data: any): void {
                if (data.type === 'bar') {
                    data.element.attr({
                        style: 'stroke-width: 5px',
                        x1: data.x1 + 0.001
                    });

                }
                else if (data.type === 'label') {
                    data.element.attr({
                        y: 270
                    })
                }
            }
        },
    };
    // Stacked Bar chart configuration Ends

    // Line area chart 2 configuration Starts
    lineArea2: Chart = {
        type: 'Line',
        data: data['lineArea2'],
        options: {
            showArea: true,
            showLabels: true,
            fullWidth: true,
            plugins: [
                Chartist.plugins.tooltip()
            ],
            chartPadding: {
                left: 18
            },
            lineSmooth: Chartist.Interpolation.none(),
            axisX: {
                showGrid: false,
            },
            axisY: {
                low: 0,
                scaleMinSpace: 50,
                labelInterpolationFnc: function (value) {
                    if (value >= 100000 && value < 1000000) {
                        return (value / 1000) + 'K';
                    } else if (value >= 1000000) {
                        return (Math.round(value / 1000000 * 100) / 100) + 'M';
                    } else {
                        return value;
                    }
                },
            }
        },
        responsiveOptions: [
            ['screen and (max-width: 640px) and (min-width: 381px)', {
                axisX: {
                    labelInterpolationFnc: function (value, index) {
                        return index % 2 === 0 ? value : null;
                    }
                }
            }],
            ['screen and (max-width: 380px)', {
                axisX: {
                    labelInterpolationFnc: function (value, index) {
                        return index % 3 === 0 ? value : null;
                    }
                }
            }]
        ],
        events: {
            created(data: any): void {
                var defs = data.svg.elem('defs');
                defs.elem('linearGradient', {
                    id: 'gradient2',
                    x1: 0,
                    y1: 1,
                    x2: 0,
                    y2: 0
                }).elem('stop', {
                    offset: 0,
                    'stop-opacity': '0.2',
                    'stop-color': 'rgba(255, 255, 255, 1)'
                }).parent().elem('stop', {
                    offset: 1,
                    'stop-opacity': '0.2',
                    'stop-color': 'rgba(0, 201, 255, 1)'
                });

                defs.elem('linearGradient', {
                    id: 'gradient3',
                    x1: 0,
                    y1: 1,
                    x2: 0,
                    y2: 0
                }).elem('stop', {
                    offset: 0.3,
                    'stop-opacity': '0.2',
                    'stop-color': 'rgba(255, 255, 255, 1)'
                }).parent().elem('stop', {
                    offset: 1,
                    'stop-opacity': '0.2',
                    'stop-color': 'rgba(132, 60, 247, 1)'
                });
            },
            draw(data: any): void {
                var circleRadius = 4;
                if (data.type === 'point') {

                    var circle = new Chartist.Svg('circle', {
                        cx: data.x,
                        cy: data.y,
                        r: circleRadius,
                        class: 'ct-point-circle'
                    });
                    data.element.replace(circle);
                }
                else if (data.type === 'label') {
                    // adjust label position for rotation
                    const dX = data.width / 2 + (30 - data.width)
                    data.element.attr({ x: data.element.attr('x') - dX })
                }
            }
        },
    };
    // Line area chart 2 configuration Ends

    // Line chart configuration Starts
    lineChart: Chart = {
        type: 'Line', data: data['LineDashboard'],
        options: {
            axisX: {
                showGrid: false
            },
            axisY: {
                showGrid: false,
                showLabel: false,
                low: 0,
                high: 100,
                offset: 0,
            },
            fullWidth: true,
            offset: 0,
        },
        events: {
            draw(data: any): void {
                var circleRadius = 4;
                if (data.type === 'point') {
                    var circle = new Chartist.Svg('circle', {
                        cx: data.x,
                        cy: data.y,
                        r: circleRadius,
                        class: 'ct-point-circle'
                    });

                    data.element.replace(circle);
                }
                else if (data.type === 'label') {
                    // adjust label position for rotation
                    const dX = data.width / 2 + (30 - data.width)
                    data.element.attr({ x: data.element.attr('x') - dX })
                }
            }
        },

    };
    // Line chart configuration Ends

    // Donut chart configuration Starts
    DonutChart: Chart = {
        type: 'Pie',
        data: data['donutDashboard'],
        options: {
            donut: true,
            startAngle: 0,
            labelInterpolationFnc: function (value) {
                var total = data['donutDashboard'].series.reduce(function (prev, series) {
                    return prev + series.value;
                }, 0);
                return total + '%';
            }
        },
        events: {
            draw(data: any): void {
                if (data.type === 'label') {
                    if (data.index === 0) {
                        data.element.attr({
                            dx: data.element.root().width() / 2,
                            dy: data.element.root().height() / 2
                        });
                    } else {
                        data.element.remove();
                    }
                }

            }
        }
    };
    // Donut chart configuration Ends

    //  Bar chart configuration Starts
    BarChart: Chart = {
        type: 'Bar', data: data['DashboardBar'], options: {
            axisX: {
                showGrid: false,
            },
            axisY: {
                showGrid: false,
                showLabel: false,
                offset: 0
            },
            low: 0,
            high: 60, // creative tim: we recommend you to set the high sa the biggest value + something for a better look
        },
        responsiveOptions: [
            ['screen and (max-width: 640px)', {
                seriesBarDistance: 5,
                axisX: {
                    labelInterpolationFnc: function (value) {
                        return value[0];
                    }
                }
            }]
        ],
        events: {
            created(data: any): void {
                var defs = data.svg.elem('defs');
                defs.elem('linearGradient', {
                    id: 'gradient4',
                    x1: 0,
                    y1: 1,
                    x2: 0,
                    y2: 0
                }).elem('stop', {
                    offset: 0,
                    'stop-color': 'rgba(238, 9, 121,1)'
                }).parent().elem('stop', {
                    offset: 1,
                    'stop-color': 'rgba(255, 106, 0, 1)'
                });
                defs.elem('linearGradient', {
                    id: 'gradient5',
                    x1: 0,
                    y1: 1,
                    x2: 0,
                    y2: 0
                }).elem('stop', {
                    offset: 0,
                    'stop-color': 'rgba(0, 75, 145,1)'
                }).parent().elem('stop', {
                    offset: 1,
                    'stop-color': 'rgba(120, 204, 55, 1)'
                });

                defs.elem('linearGradient', {
                    id: 'gradient6',
                    x1: 0,
                    y1: 1,
                    x2: 0,
                    y2: 0
                }).elem('stop', {
                    offset: 0,
                    'stop-color': 'rgba(132, 60, 247,1)'
                }).parent().elem('stop', {
                    offset: 1,
                    'stop-color': 'rgba(56, 184, 242, 1)'
                });
                defs.elem('linearGradient', {
                    id: 'gradient7',
                    x1: 0,
                    y1: 1,
                    x2: 0,
                    y2: 0
                }).elem('stop', {
                    offset: 0,
                    'stop-color': 'rgba(155, 60, 183,1)'
                }).parent().elem('stop', {
                    offset: 1,
                    'stop-color': 'rgba(255, 57, 111, 1)'
                });

            },
            draw(data: any): void {
                var barHorizontalCenter, barVerticalCenter, label, value;
                if (data.type === 'bar') {

                    data.element.attr({
                        y1: 195,
                        x1: data.x1 + 0.001
                    });

                }
            }
        },

    };
    // Bar chart configuration Ends

    // line chart configuration Starts
    WidgetlineChart: Chart = {
        type: 'Line', data: data['WidgetlineChart'],
        options: {
            axisX: {
                showGrid: true,
                showLabel: false,
                offset: 0,
            },
            axisY: {
                showGrid: false,
                low: 40,
                showLabel: false,
                offset: 0,
            },
            lineSmooth: Chartist.Interpolation.cardinal({
                tension: 0
            }),
            fullWidth: true,
        },
    };
    // Line chart configuration Ends

    ngOnInit(): void {

        this.user = JSON.parse(localStorage.getItem('currentUser')).user;

        // should not get here but what the heck
        if (!this.user) {
            this.user = new User();
        }

        if (!this.user.preferences) {
            this.user.preferences = {};
            this.user.preferences['dashboard.canadianrate.years'] = '5';
        }
        //if (!this.user.preferences) {
        //              this.user.preferences = { 'empty':'true' };  
        //}
        //if (!this.user.preferences['dashboard.canadianrate.years']) {
        //              this.user.preferences['dashboard.canadianrate.years'] = "5";
        //}

        let now = new Date();
        this.month = now.getMonth() + 1;
        this.year = now.getFullYear();
        this.isLoading = true;


        this.propertyService.getMidPoint().subscribe(
            p => { this.midPoint = p; },
            e => { this.errorMessage = e; console.log(e) },
            () => {
                // Find smallest lat
                this.isLoading = false;
                this.propertyMidPoint.lat = this.midPoint.lat;
                this.propertyMidPoint.lng = this.midPoint.lng;
                this.propertyMidPoint.zoom = this.midPoint.zoom;
            }
        );

        this.propertyService.getAll().subscribe(
            p => { this.properties = p; },
            e => { this.errorMessage = e; console.log(e) },
            () => {
                // Find smallest lat
                this.isLoading = false;
                for (var i = 0; i < this.properties.length; i++) {
                    var marker = new MapMarker();
                    if (this.properties[i].latitude > 0) {
                        marker.lat = this.properties[i].latitude;
                        marker.lng = this.properties[i].longitude;
                        marker.label = this.properties[i].name;
                        marker.property = this.properties[i];
                        marker.draggable = false;
                        this.markers.push(marker);
                    }
                }
            }
        );
        this.updateCanadianRate();
        this.updateLoanBalanceGraph();
        this.updateLoanValueCapitalGraph();

    }

    updateCanadianRate() {
        if (!this.user.preferences['dashboard.canadianrate.years']) {
            this.user.preferences['dashboard.canadianrate.years'] = "5";
        }
        this.ratehubService.getRate(Number(this.user.preferences['dashboard.canadianrate.years'])).subscribe(
            p => { this.rates = p.rates; },
            e => { this.errorMessage = e; console.log(e) },
            () => {
                console.log("Rates Loaded...");
                console.log(this.rates);
            }
        );
    }

    constructor(private fgService: FinancialGraphService, private userService: UserService,
        private ratehubService: RateHubService, private propertyService: PropertyService) {
    }

    updateLoanValueCapitalGraph() {
        if (!this.user.preferences['dashboard.loanValueCapitalGraph.start']) {
            this.user.preferences['dashboard.loanValueCapitalGraph.start'] = "-2";
        }
        if (!this.user.preferences['dashboard.loanValueCapitalGraph.end']) {
            this.user.preferences['dashboard.loanValueCapitalGraph.end'] = "5";
        }

        this.fgService.getLoanValueCapitalPrevision(Number(this.user.preferences['dashboard.loanValueCapitalGraph.start']),
            Number(this.user.preferences['dashboard.loanValueCapitalGraph.end'])).subscribe(
            p => { this.loanValueCapitalPrev = p; },
            e => { this.errorMessage = e; console.log(e); },
            () => {
                console.log(this.lineArea2.data);
                var thedata: Chartist.IChartistData = this.lineArea2.data;
                var labels: any[] = [];
                var series: any[][] = [];
                var i: number = 0;
                var x: number = 0;

                for (x = 0; x < this.loanValueCapitalPrev.length; x++) {
                    series[x] = [];
                    for (i = 0; i < this.loanValueCapitalPrev[x].values.length; i++) {
                        labels.indexOf(this.loanValueCapitalPrev[x].values[i].x) === -1 ? labels.push(this.loanValueCapitalPrev[x].values[i].x) : console.log("ok");
                        series[x][i] = this.loanValueCapitalPrev[x].values[i].y;
                    }
                }

                this.lineArea2.data.labels = labels;
                this.lineArea2.data.series = series;
            }
            );
    }


    updateLoanBalanceGraph() {
        if (!this.user.preferences['dashboard.loanBalanceGraph.start']) {
            this.user.preferences['dashboard.loanBalanceGraph.start'] = "-2";
        }
        if (!this.user.preferences['dashboard.loanBalanceGraph.end']) {
            this.user.preferences['dashboard.loanBalanceGraph.end'] = "5";
        }
        this.fgService.getLoansBalancePrevision(Number(this.user.preferences['dashboard.loanBalanceGraph.start']), Number(this.user.preferences['dashboard.loanBalanceGraph.end'])).subscribe(
            p => { this.loanBalancePrev = p; },
            e => { this.errorMessage = e; console.log(e); },
            () => {
                var thedata: any[] = [];
                var keys: any[] = [];
                var i: number = 0;
                var x: number = 0;
                for (i = 0; i < this.loanBalancePrev[0].values.length; i++) {
                    thedata.push({ 'y': this.loanBalancePrev[0].values[i].x });
                    for (x = 0; x < this.loanBalancePrev.length; x++) {
                        thedata[i][this.loanBalancePrev[x].key] = this.loanBalancePrev[x].values[i].y;
                    }
                }
                for (x = 0; x < this.loanBalancePrev.length; x++) {
                    keys.push(this.loanBalancePrev[x].key);
                }
                this.loanBalancePrevisionData = {
                    resize: true,
                    data: thedata,
                    xkey: 'y',
                    ykeys: keys,
                    labels: keys,
                    lineColors: this.lineColors
                };
            }
        );
    }

    canadianRateChange(year: string) {
        console.log("WHATTTTT" + year);
        this.user.preferences['dashboard.canadianrate.years'] = year;
        this.userService.updatePreferences(this.user).subscribe(
            p => { },
            e => { console.log("Error saving pref:" + e); },
            () => { console.log("User Pref Updated"); }
        );
        this.updateCanadianRate();
    }

}
