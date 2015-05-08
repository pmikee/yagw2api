@Unit
Feature: Map Wrapper

	Scenario: no continent available
		Given a map continent service that knows no continent at all
			And a map floor service that returns empty floors
			And a map tile service that returns empty map tiles
			And a map domain service that uses the given map floor service
			And a continent wrapper under test
		When the user tries to retrieve all continents
		Then '0' continents have been retrieved

	Scenario: single continent available
		Given a map continent service that knows the given continents
			And a map floor service that returns empty floors
			And a map tile service that returns empty map tiles
			And a map domain service that uses the given map floor service
			And a continent with id="xyz" and name="Pusemuckel"
			And a continent wrapper under test
		When the user tries to retrieve all continents
		Then '1' continents have been retrieved
			And continent with id="xyz" and name="Pusemuckel" is one of them

	Scenario: multiple continents available
		Given a map continent service that knows the given continents
			And a map floor service that returns empty floors
			And a map tile service that returns empty map tiles
			And a map domain service that uses the given map floor service
			And a continent with id="xyz" and name="Pusemuckel"
			And another continent with id="123" and name="Stuttgart"
			And another continent with id="456" and name="Berlin"
			And a continent wrapper under test
		When the user tries to retrieve all continents
		Then '3' continents have been retrieved
			And continent with id="xyz" and name="Pusemuckel" is one of them
			And continent with id="123" and name="Stuttgart" is one of them
			And continent with id="456" and name="Berlin" is one of them