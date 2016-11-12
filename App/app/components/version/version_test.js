'use strict';

describe('recommender.version module', function() {
  beforeEach(module('recommender.version'));

  describe('version service', function() {
    it('should return current version', inject(function(version) {
      expect(version).toEqual('0.1');
    }));
  });
});
