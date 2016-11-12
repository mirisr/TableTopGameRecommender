'use strict';

angular.module('recommender.version', [
  'recommender.version.interpolate-filter',
  'recommender.version.version-directive'
])

.value('version', '0.1');
