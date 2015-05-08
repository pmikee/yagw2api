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