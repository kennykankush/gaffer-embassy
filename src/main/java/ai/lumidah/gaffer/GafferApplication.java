package ai.lumidah.gaffer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ai.lumidah.gaffer.model.Event;
import ai.lumidah.gaffer.model.Fixture;
import ai.lumidah.gaffer.model.LivePlayer;
import ai.lumidah.gaffer.model.Player;
import ai.lumidah.gaffer.model.TeamFD;
import ai.lumidah.gaffer.model.TeamFPL;
import ai.lumidah.gaffer.repository.EventRepository;
import ai.lumidah.gaffer.repository.FixtureRepository;
import ai.lumidah.gaffer.repository.PlayerRepository;
import ai.lumidah.gaffer.repository.TeamRepository;
import ai.lumidah.gaffer.utility.ApiFetch;

@SpringBootApplication
@EnableScheduling
public class GafferApplication implements CommandLineRunner{

	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private FixtureRepository fixtureRepo;

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private ApiFetch apiFetch;


	public static void main(String[] args) {
		SpringApplication.run(GafferApplication.class, args);
	}

	@Override
    public void run(String... args) {

		System.out.println("Checking for REDIS-players");

		if (!playerRepo.doesPlayersKeyExist()) {
			System.out.println("REDIS-players does not exist.");
			System.out.println("Fetching API for Bootstrap-Static and saving players.");

			try {
				List<Player> players = apiFetch.jsonPlayerToModelPlayer();
				playerRepo.savePlayers(players);

				System.out.println("Players successfully fetched from API and saved to Redis.");
			} catch (JsonMappingException e) {

				System.err.println("Error mapping JSON to Player model: " + e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {

				System.err.println("Error processing JSON from API: " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
	
				System.err.println("An unexpected error occurred while fetching players: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("REDIS-players exist");
		}

		System.out.println("Checking for REDIS-teams");

		if (!teamRepo.doesTeamKeyExist()) {
			System.out.println("REDIS-teams does not exist.");
			System.out.println("Fetching API for teams");

			try {
				List<TeamFD> teamFDs = apiFetch.jsonTeamToModelTeamFD();
				List<TeamFPL> teamFPLs = apiFetch.jsonTeamToModelTeamFPL();
				teamRepo.saveTeams(teamFDs, teamFPLs);

				System.out.println("Teams successfully fetched from API and saved to Redis.");

			} catch (JsonMappingException e) {

				System.err.println("Error mapping JSON to Player model: " + e.getMessage());
				e.printStackTrace();

			} catch (JsonProcessingException e) {

				System.err.println("Error processing JSON from API: " + e.getMessage());
				e.printStackTrace();

			} catch (Exception e) {

				System.err.println("An unexpected error occurred while fetching players: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("REDIS-teams exist");
		}

		System.out.println("Checking for REDIS-fixtures");

		if (!fixtureRepo.doesFixturesKeyExist()) {
			System.out.println("REDIS-fixtures does not exist.");
			System.out.println("Fetching API for fixtures");

			try {
				List<Fixture> fixtures = apiFetch.jsonFixtureToModelFixture();
				fixtureRepo.saveFixtures(fixtures);

				System.out.println("Fixture successfully fetched from API and saved to Redis.");

			} catch (JsonMappingException e) {

				System.err.println("Error mapping JSON to Player model: " + e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {

				System.err.println("Error processing JSON from API: " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {

				System.err.println("An unexpected error occurred while fetching players: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("REDIS-fixtures exist");
		}

		System.out.println("Checking for REDIS-events");

		if (!eventRepo.doesEventKeyExist()) {
			System.out.println("REDIS-events does not exist.");
			System.out.println("Fetching API for events");

			try {
				List<Event> events = apiFetch.jsonEventToModelEvent();
				eventRepo.saveEvents(events);

				System.out.println("Events successfully fetched from API and saved to Redis.");

			} catch (JsonMappingException e) {
				
				System.err.println("Error mapping JSON to Event model: " + e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {
	
				System.err.println("Error processing JSON from API: " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {

				System.err.println("An unexpected error occurred while fetching events: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("REDIS-events exist");
		}

		System.out.println("Checking for REDIS-livePlayers");

		for (int i = 1; i < 39; i++){

			if (!playerRepo.doesLivePlayersExist(String.valueOf(i))){
				System.out.println("REDIS-livePlayer " + i + " does not exist.");
				System.out.println("Fetching API for liveplayers: " + i);

				try {
					List<LivePlayer> players = apiFetch.jsonLivePlayerToModelLivePlayer(i);
					playerRepo.saveLivePlayers(players, "live:" + i);
	
					System.out.println("Events successfully fetched from API and saved to Redis.");

			} catch (JsonMappingException e) {

				System.err.println("Error mapping JSON to LivePlayer model: " + e.getMessage());
				e.printStackTrace();
			} catch (JsonProcessingException e) {

				System.err.println("Error processing JSON from API: " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {

				System.err.println("An unexpected error occurred while fetching LivePlayer: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("REDIS-events exist");
		}


		}

	}

}
