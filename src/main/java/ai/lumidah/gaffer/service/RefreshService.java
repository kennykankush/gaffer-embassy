package ai.lumidah.gaffer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ai.lumidah.gaffer.model.Event;
import ai.lumidah.gaffer.model.Fixture;
import ai.lumidah.gaffer.model.LivePlayer;
import ai.lumidah.gaffer.model.Player;
import ai.lumidah.gaffer.repository.EventRepository;
import ai.lumidah.gaffer.repository.FixtureRepository;
import ai.lumidah.gaffer.repository.PlayerRepository;
import ai.lumidah.gaffer.utility.ApiFetch;
import ai.lumidah.gaffer.utility.CurrentGameWeek;

@Service
public class RefreshService{

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private FixtureRepository fixtureRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ApiFetch apiFetch;

    private void deleteDynamicCache(){
        playerRepository.deleteAllPlayers();
        System.out.println("deleted Players");
        fixtureRepository.deleteAllFixtures();
        System.out.println("deleted All Fixtures");
        eventRepository.deleteAllEvent();
        System.out.println("deleted All Events");
    }

    private void fetchDynamicCache() throws JsonMappingException, JsonProcessingException{

        List<Player> players = apiFetch.jsonPlayerToModelPlayer();
        playerRepository.savePlayers(players);
        System.out.println("Players Saved");
        List<Fixture> fixtures = apiFetch.jsonFixtureToModelFixture();
        fixtureRepository.saveFixtures(fixtures);
        System.out.println("Fixtures Saved");
        List<Event> events = apiFetch.jsonEventToModelEvent();
        eventRepository.saveEvents(events);
        System.out.println("Event Saved Saved");

        long currentEpoch = System.currentTimeMillis() / 1000;
        int currentGameWeek = CurrentGameWeek.getCurrentGameWeek(currentEpoch, fixtures);

        if (!playerRepository.doesLivePlayersExist(String.valueOf(currentGameWeek))){
            System.out.println("The current game week " + currentGameWeek + "does not exist");
            List<LivePlayer> livePlayers = apiFetch.jsonLivePlayerToModelLivePlayer(currentGameWeek);
            playerRepository.saveLivePlayers(livePlayers, "live:" + currentGameWeek);

        }
        
        // for (int i = 1; i < currentGameWeek; i++){
        //     List<LivePlayer> livePlayers = apiFetch.jsonLivePlayerToModelLivePlayer(i);
        //     playerRepository.saveLivePlayers(livePlayers, "live:" + i);
        //     System.out.println("Liveplayers: " + i + " Saved");

        // }
    
    }

    @Scheduled(initialDelay = 3600001, fixedRate = 3600001)
    public void refreshDynamicCache() throws JsonMappingException, JsonProcessingException{
        System.out.println("I have awoken...");
        System.out.println("Cache Refresh Activated.....");

        deleteDynamicCache();
        fetchDynamicCache();

        System.out.println("Going back to sleep");

    }












}