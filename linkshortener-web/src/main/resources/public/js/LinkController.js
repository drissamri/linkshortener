(function (angular) {
  'use strict';

  angular
    .module('linkApp')
    .controller('LinkController', ['$scope', '$log', 'LinkService', 'NotificationService',
      function LinkController($scope, $log, LinkService, NotificationService) {
        $scope.longUrl = null;
        $scope.link = null;
        $scope.notifications = NotificationService.queue;

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
