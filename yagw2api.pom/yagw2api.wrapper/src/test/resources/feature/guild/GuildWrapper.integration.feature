@Integration
Feature: Guild Wrapper

	Scenario: retrieve a specific guilds from multiple available guilds
		Given the real guild service
			And a working guild domain factory
			And a guild wrapper under test
		When the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF"
		Then the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF", name="Veterans Of Lions Arch" and tag="LA" has been retrieved

	Scenario: retrieve a specific available guilds multiple times from cache
		Given the real guild service
			And a working guild domain factory
			And a guild wrapper under test
		When the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF"
			And the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF" a second time
			And the user tries to retrieve the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF" a third time
		Then the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF", name="Veterans Of Lions Arch" and tag="LA" has been retrieved
			And the service has been questioned for the guild with id="75FD83CF-0C45-4834-BC4C-097F93A487AF" exactly '1' times