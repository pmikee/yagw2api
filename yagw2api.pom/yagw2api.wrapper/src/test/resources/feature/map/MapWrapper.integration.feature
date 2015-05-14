@Integration
Feature: Map Wrapper
	Scenario: multiple continents available
		Given the real map continent service
			And the real map floor service
			And the real map tile service
			And a synchronous eventbus
			And a map event factory
			And a map domain factory
			And a continent wrapper under test
		When the user tries to retrieve all continents
		Then '2' continents have been retrieved
			And the continent with id="1" has a floor with index '0'
			And the continent with id="1" has a floor with index '1'
			And the continent with id="1" has a floor with index '2'
			And the continent with id="1" has a floor with index '10'
			And the continent with id="2" has a floor with index '1'