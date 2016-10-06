(function () {

    'use strict';


    angular.module('ezTravel', [])
        .constant('BaseUrl', 'webapi/event')
        .service('location', function ($http, $q) {
            var service = this;


            function success(pos) {
                service.position = pos;
                var lat = pos.coords.latitude;
                var lon = pos.coords.longitude;

                // var src = 'https://maps.googleapis.com/maps/api/staticmap?center=' + lat + ',' + lon + '&zoom=15&size=300x300&sensor=false&key=AIzaSyBTWzQ9sHMfnbPjRlWcV7Y9GZrompEGV58';
            }


            navigator.geolocation.getCurrentPosition(success);

            service.getLocation = function () {
                var defferd = $q.defer();

                navigator.geolocation.getCurrentPosition(defferd.resolve);

                return defferd.promise;
            }

            service.getAddress = function (lat, lon) {
                return $http.get('https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lon + '&key=AIzaSyBTWzQ9sHMfnbPjRlWcV7Y9GZrompEGV58')
                    .then(function (result) {
                        return result.data;
                    });
            }

        })
        .controller('ctrl', function ($http, BaseUrl, location) {
            var vm = this;
            // $http.get('data.json')

            $http.get(BaseUrl + '/all')
                .then(
                function (response) {
                    vm.events = response.data;
                },
                function (err) {
                    console.log(err);
                });

            vm.getAddress = function (event) {
                location.getAddress(event.lat, event.lon).then(function (data) {
                    event.address = data.results[0].formatted_address;
                })
            }

            location.getLocation().then(function (data) {
                console.log(data)
            })
        });

})();