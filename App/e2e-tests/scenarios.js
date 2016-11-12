'use strict';

/* https://github.com/angular/protractor/blob/master/docs/toc.md */

describe('my app', function() {


  it('should automatically redirect to /profile when location hash/fragment is empty', function() {
    browser.get('index.html');
    expect(browser.getLocationAbsUrl()).toMatch("/profile");
  });


  describe('profile', function() {

    beforeEach(function() {
      browser.get('index.html#!/profile');
    });


    it('should render profile when user navigates to /profile', function() {
      expect(element.all(by.css('[ng-view] p')).first().getText()).
        toMatch(/partial for view 1/);
    });

  });


  describe('game', function() {

    beforeEach(function() {
      browser.get('index.html#!/game');
    });


    it('should render game when user navigates to /game', function() {
      expect(element.all(by.css('[ng-view] p')).first().getText()).
        toMatch(/partial for view 2/);
    });

  });
});
