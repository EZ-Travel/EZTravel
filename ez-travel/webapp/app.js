(function () {

    'use strict';


    angular.module('ezTravel', [])
        .constant('BaseUrl', 'webapi/event')
        .service('location', function(){
            var service = this;
            var output = document.getElementById('locationImg');
            
            function success(pos) {
              service.position = pos;
                var lat = pos.coords.latitude;
                var lon = pos.coords.longitude;

                console.log('the position is: ' + lat + ' ' + lon);

                var src = 'https://maps.googleapis.com/maps/api/staticmap?center='+lat+','+lon+'&zoom=15&size=300x300&sensor=false&key=AIzaSyBTWzQ9sHMfnbPjRlWcV7Y9GZrompEGV58';
                output.setAttribute('src', src);
            }


            navigator.geolocation.getCurrentPosition(success);



        })
        .controller('ctrl', function ($scope, $http, BaseUrl, location) {

console.log(location.position);
            $scope.getAll = function () {
                $http.get(BaseUrl).then(
                    function (response) {
                        console.log(response);
                        $scope.events = response.data;
                    },
                    function (err) {
                        console.log(err);
                    });
            }

            $scope.createEvent = function (event) {
                $http.post(BaseUrl, event).then(
                    function (result) {
                        console.log(result);
                        $scope.createdEvent = result.data;
                    },
                    function (err) {
                        console.log(err);
                    });
            }

            $scope.updateEvent = function (event) {

                $http.put(BaseUrl, event).then(
                    function (result) {
                        console.log(result);
                        $scope.updatedEvent = result.data;
                    },
                    function (err) {
                        console.log(err);
                    }
                )
            }

            $scope.removeEvent = function (id) {
                $http.delete(BaseUrl, { params: { 'id': id } }).then(
                    function (result) {
                        console.log(result);
                        $scope.removed = result.data;
                    },
                    function (err) {
                        console.log(err);
                    });
            }

            $scope.getEvent = function (id) {
                $http.get(BaseUrl + '/' + id).then(
                    function (result) {
                        $scope.event = result.data;

                    },
                    function (err) {
                        console.log(err);
                    });
            }

            $scope.getWeather = function (search) {
                $http.get(BaseUrl + '/weather', { params: search }).then(
                    function (result) {
                        console.log(result);
                        $scope.searchedEvents = result.data;
                    },
                    function (err) {
                        console.log(err);
                    });
            }
        });

})();