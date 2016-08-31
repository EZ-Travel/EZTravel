    (function(){

    'use strict';

    angular.module('ezTravel',[])
    .constant('BaseUrl', 'webapi/event')
    .controller('ctrl',function ($scope, $http, BaseUrl) {
        
     

      $scope.getAll = function(){
          $http.get(BaseUrl).then(
          function(response){
              console.log(response);
           $scope.events = response.data;
          },
          function (err){
              console.log(err);
          });
      }

      $scope.createEvent = function(event){
          $http.post(BaseUrl, event).then(
              function(result){
                  console.log(result);
                  $scope.createdEvent = result.data; 
          },
              function(err){
                  console.log(err);
          });
      }

      $scope.updateEvent = function (event){

          $http.put(BaseUrl, event).then(
             function(result){
                  console.log(result);
                  $scope.updatedEvent = result.data;
          },
             function (err){
 console.log(err);
          }                     
        )
      }
      
      $scope.removeEvent = function (id){
          $http.delete(BaseUrl, {params: {'id':id}}).then(
          function (result){
              console.log(result);
              $scope.removed = result.data;
          },
          function (err){
              console.log(err);
          });
      }
      
      $scope.getEvent = function (id){
          $http.get(BaseUrl+'/'+id).then(
          function (result){
              $scope.event = result.data;
        
          },
          function (err){
              console.log(err);
          });
      }
      
      $scope.getWeather = function (search){
          $http.get(BaseUrl+'/weather', {params:search }).then(
          function (result){
              console.log(result);
              $scope.searchedEvents = result.data;
          },
          function (err) {
              console.log(err);
          });
      }
      
      






    });

    })();