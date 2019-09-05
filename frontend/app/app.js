'use strict';

import {enableProdMode} from '@angular/core'
import {environment} from './environments/environment'
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import {AppModule} from "./module/app.module";

if (environment.production) {
    enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
    .catch(err => console.error(err))


// // Declare app level module which depends on views, and core components
// angular.module('myApp', [
//   'ngRoute',
//   'myApp.view1',
//   'myApp.view2',
//   'myApp.version'
// ]).
// config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
//   $locationProvider.hashPrefix('!');
//
//   $routeProvider.otherwise({redirectTo: '/view1'});
// }]);
