@Integration
Feature: Map Wrapper
	Scenario: multiple continents available
		Given the real map continent service
			And the real map floor service
			And the real map tile service
			And a map domain service that uses the given map floor service
			And a continent wrapper under test
		When the user tries to retrieve all continents
		Then '2' continents have been retrieved
			And the continent with id="1" continent has a floor with index '0'
			And the continent with id="1" continent has a floor with index '1'
			And the continent with id="1" continent has a floor with index '2'
			And the continent with id="1" continent has a floor with index '10'