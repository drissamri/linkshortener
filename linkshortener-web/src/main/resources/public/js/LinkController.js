(function (angular) {
  'use strict';

  angular
    .module('linkApp')
    .controller('LinkController', ['$scope', '$log', 'LinkService', function LinkController($scope, $log, LinkService) {
      $scope.longUrl = null;
      $scope.link = null;

      $scope.shorten = function (url) {

        if (url) {
          $log.log('Try to shorten: ' + url);
          var promise = LinkService.shorten(url);

          promise.then(function (link) {
            $scope.link = link;
          });

        }

      };
    }]);
})(angular);