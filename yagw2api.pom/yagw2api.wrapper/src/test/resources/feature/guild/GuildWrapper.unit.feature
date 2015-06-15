@Unit
Feature: Guild Wrapper
	
	Scenario: no guild available
		Given a guild service that knows no guild at all
			And a working guild domain factory
			And a guild wrapper under test
		When the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF"
		Then an exception should be thrown that indicates that there is no such guild

	Scenario: any other guild than the requested is available
		Given a guild service that knows any guild whose id is not "75FD83CF-0C45-4834-BC4C-097F93A487AF"
			And a working guild domain factory
			And a guild wrapper under test
		When the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF"
		Then an exception should be thrown that indicates that there is no such guild

	Scenario: retrieve the only available guild
		Given a guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF", name="Veterans Of Lions Arch" and tag="LA"
			And a service that knows only given guilds
			And a working guild domain factory
			And a guild wrapper under test
		When the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF"
		Then the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF", name="Veterans Of Lions Arch" and tag="LA" has been retrieved

	Scenario: retrieve a specific guilds from multiple available guilds
		Given a guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF", name="Veterans Of Lions Arch" and tag="LA"
			And another guild with id="E5446305-197F-E411-AEFB-AC162DC05865", name="Hüter Der Siegel" and tag="SEAL"
			And another guild with id="793489C2-F719-4DA6-A1B3-EC11E7FF99B2", name="From The Exile" and tag="Exil"
			And a service that knows only given guilds
			And a working guild domain factory
			And a guild wrapper under test
		When the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF"
		Then the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF", name="Veterans Of Lions Arch" and tag="LA" has been retrieved

	Scenario: retrieve a specific available guilds multiple times from cache
		Given a guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF", name="Veterans Of Lions Arch" and tag="LA"
			And a service that knows only given guilds
			And a working guild domain factory
			And a guild wrapper under test
		When the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF"
			And the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF" a second time
			And the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF" a third time
		Then the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF", name="Veterans Of Lions Arch" and tag="LA" has been retrieved
			And the service has been questioned for the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF" exactly '1' times