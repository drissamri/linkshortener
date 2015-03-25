(function (angular) {
  'use strict';

  angular
    .module('linkApp')
    .service('LinkService', ['$http', '$q', 'NotificationService',
      function LinkService($http, $q, NotificationService) {

      return {

        shorten: function (url) {
          var deferred = $q.defer();

          $http.get('/links?longUrl='+url)
            .success(function(link) {
              return deferred.resolve(link);
            }).error(function(error) {
              NotificationService.add(error.message, 'error');
            });

          return deferred.promise;
        }
      };
    }]);
})(angular);
