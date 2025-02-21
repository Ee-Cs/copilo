angular.module('department-app', [])
.controller('Department', function($scope, $http) {
    $http.get('/company/departments/1').
        then(function(response) {
            $scope.department = response.data;
        });
});
