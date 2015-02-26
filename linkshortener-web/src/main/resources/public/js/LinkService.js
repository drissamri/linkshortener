(function (angular) {
  'use strict';

  angular
    .module('linkApp')
    .service('LinkService', ['$http', '$q', '$log', function LinkService($http, $q, $log) {

      return {

        shorten: function (url) {
          var deferred = $q.defer();

          $http.get('/links?longUrl='+url)
            .success(function(data) {
              return deferred.resolve(data);
            }).error(function(data) {
              return deferred.resolve(data);
            });

          return deferred.promise;
        }
      };
    }]);
})(angular);