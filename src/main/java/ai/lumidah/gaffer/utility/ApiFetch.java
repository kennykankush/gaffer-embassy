package ai.lumidah.gaffer.utility;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ai.lumidah.gaffer.configuration.Endpoints;
import ai.lumidah.gaffer.model.Event;
import ai.lumidah.gaffer.model.Fixture;
import ai.lumidah.gaffer.model.Gaffer;
import ai.lumidah.gaffer.model.GafferPick;
import ai.lumidah.gaffer.model.LiveDreamTeam;
import ai.lumidah.gaffer.model.LivePlayer;
import ai.lumidah.gaffer.model.Player;
import ai.lumidah.gaffer.model.TeamFD;
import ai.lumidah.gaffer.model.TeamFPL;
import ai.lumidah.gaffer.service.FixtureService;

@Component
public class ApiFetch {

    @Autowired
    private Endpoints endpoints;

    @Autowired
    private FixtureService fixtureService;

    private JsonNode endpointAPICollector(String destination) throws JsonMappingException, JsonProcessingException{
        
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(destination, String.class);

        String body = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(body);

    }

    private JsonNode endpointAPICollectorWithKey(String destination, String key) throws JsonMappingException, JsonProcessingException{
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", key);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(destination, HttpMethod.GET, entity, String.class);

        String body = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readTree(body);

    }
    
    public List<Player> jsonPlayerToModelPlayer() throws JsonMappingException, JsonProcessingException{

        List<Player> players = new ArrayList<>();

        JsonNode raw = endpointAPICollector(endpoints.getBootStrap());
        JsonNode jsonPlayers = raw.get("elements");

        for (JsonNode jsonPlayer : jsonPlayers) {
            Player player = new Player();
    
            // ----- Basic Information
            player.setId(jsonPlayer.get("id").asText());
            player.setFirstName(jsonPlayer.get("first_name").asText());
            player.setLastName(jsonPlayer.get("second_name").asText());
            player.setWebName(jsonPlayer.get("web_name").asText());
            player.setPhoto(jsonPlayer.get("photo").asText());
            player.setStatus(jsonPlayer.get("status").asText());
            player.setTeam(jsonPlayer.get("team").asInt());
            player.setElementType(jsonPlayer.get("element_type").asInt());
            player.setPhotoCode(jsonPlayer.get("code").asInt());
    
            // ----- Transaction and Selection Info
            player.setCanTransact(jsonPlayer.get("can_transact").asBoolean());
            player.setCanSelect(jsonPlayer.get("can_select").asBoolean());
            player.setSelectedByPercent(jsonPlayer.get("selected_by_percent").asDouble());
            player.setTransfersIn(jsonPlayer.get("transfers_in").asInt());
            player.setTransfersOut(jsonPlayer.get("transfers_out").asInt());
            player.setTransfersInEvent(jsonPlayer.get("transfers_in_event").asInt());
            player.setTransfersOutEvent(jsonPlayer.get("transfers_out_event").asInt());
    
            // ----- Cost Information
            player.setNowCost(jsonPlayer.get("now_cost").asInt());
            player.setCostChangeEvent(jsonPlayer.get("cost_change_event").asInt());
            player.setCostChangeStart(jsonPlayer.get("cost_change_start").asInt());
    
            // ----- Performance Metrics
            player.setTotalPoints(jsonPlayer.get("total_points").asInt());
            player.setPointsPerGame(jsonPlayer.get("points_per_game").asDouble());
            player.setForm(jsonPlayer.get("form").asDouble());
            player.setEventPoints(jsonPlayer.get("event_points").asInt());
            player.setEpNext(jsonPlayer.get("ep_next").asDouble());
            player.setEpThis(jsonPlayer.get("ep_this").asDouble());
            player.setEpThis(jsonPlayer.get("ep_this").asDouble());
            player.setDreamTeam(jsonPlayer.get("in_dreamteam").asBoolean());
    
            // ----- Playing Statistics
            player.setMinutes(jsonPlayer.get("minutes").asInt());
            player.setGoalsScored(jsonPlayer.get("goals_scored").asInt());
            player.setAssists(jsonPlayer.get("assists").asInt());
            player.setCleanSheets(jsonPlayer.get("clean_sheets").asInt());
            player.setGoalsConceded(jsonPlayer.get("goals_conceded").asInt());
            player.setYellowCards(jsonPlayer.get("yellow_cards").asInt());
            player.setRedCards(jsonPlayer.get("red_cards").asInt());
            player.setBonus(jsonPlayer.get("bonus").asInt());
            player.setBps(jsonPlayer.get("bps").asInt());
    
            // ----- Advanced Metrics
            player.setInfluence(jsonPlayer.get("influence").asDouble());
            player.setCreativity(jsonPlayer.get("creativity").asDouble());
            player.setThreat(jsonPlayer.get("threat").asDouble());
            player.setIctIndex(jsonPlayer.get("ict_index").asDouble());
    
            players.add(player);
        }
    
    return players;

    }
    
    public List<LivePlayer> jsonLivePlayerToModelLivePlayer(int gameweek) throws JsonMappingException, JsonProcessingException{
        
        List<LivePlayer> livePlayers = new ArrayList<>();
        JsonNode raw = endpointAPICollector(endpoints.getLiveData(gameweek));
        JsonNode jsonPlayers = raw.get("elements");
        
        if (jsonPlayers == null) {
            System.out.println("No live data available for gameweek: " + gameweek);
            livePlayers.add(new LivePlayer());
            
        } else {

        for (JsonNode jsonPlayer : jsonPlayers) {
            LivePlayer player = new LivePlayer();
            player.setId(jsonPlayer.get("id").asText());

            JsonNode jsonStats = jsonPlayer.get("stats");

            player.setMinutes(jsonStats.get("minutes").asInt());
            player.setGoals(jsonStats.get("goals_scored").asInt());
            player.setAssist(jsonStats.get("assists").asInt());
            player.setInfluence(jsonStats.get("influence").asDouble());
            player.setIctIndex(jsonStats.get("ict_index").asDouble());
            player.setExpectedGoals(jsonStats.get("expected_goals").asText());
            player.setExpectedAssist(jsonStats.get("expected_assists").asText());
            player.setExpectedGoalInvolvement(jsonStats.get("expected_goal_involvements").asText());
            player.setExpectedGoalConceded(jsonStats.get("expected_goals_conceded").asText());
            player.setTotalPoints(jsonStats.get("total_points").asInt());
            
            livePlayers.add(player);

            }

        }

        return livePlayers;

    }

    public List<TeamFD> jsonTeamToModelTeamFD() throws JsonMappingException, JsonProcessingException{

        List<TeamFD> teams = new ArrayList<>();

        JsonNode raw = endpointAPICollectorWithKey(endpoints.getFDTeams(), endpoints.getFDKey());
        JsonNode jsonTeams = raw.get("teams");

        for (JsonNode jsonTeam: jsonTeams){
            TeamFD teamFD = new TeamFD();

            teamFD.setId(jsonTeam.get("id").asText());
            teamFD.setName(jsonTeam.get("name").asText());
            teamFD.setShortName(jsonTeam.get("shortName").asText());
            teamFD.setTla(jsonTeam.get("tla").asText());
            teamFD.setCrest(jsonTeam.get("crest").asText());
            teamFD.setWebsite(jsonTeam.get("website").asText());
            teamFD.setClubColors(jsonTeam.get("clubColors").asText());
            teamFD.setVenue(jsonTeam.get("venue").asText());
            teamFD.setFounded(jsonTeam.get("founded").asInt());

            teams.add(teamFD);

            

        }

        return teams;



    }
    
    public List<TeamFPL> jsonTeamToModelTeamFPL() throws JsonMappingException, JsonProcessingException{

        List<TeamFPL> teams = new ArrayList<>();

        JsonNode raw = endpointAPICollector(endpoints.getBootStrap());
        JsonNode jsonTeams = raw.get("teams");

        for (JsonNode jsonTeam : jsonTeams){
            TeamFPL teamFPL = new TeamFPL();

            teamFPL.setId(jsonTeam.get("id").asText());
            teamFPL.setTla(jsonTeam.get("short_name").asText());
            teamFPL.setPulse_id(jsonTeam.get("pulse_id").asText());
            teamFPL.setStrength(jsonTeam.get("strength").asInt());
            teamFPL.setStrengthOverallHome(jsonTeam.get("strength_overall_home").asInt());
            teamFPL.setStrengthOverallAway(jsonTeam.get("strength_overall_away").asInt());
            teamFPL.setStrengthAttackHome(jsonTeam.get("strength_attack_home").asInt());
            teamFPL.setStrengthAttackAway(jsonTeam.get("strength_attack_away").asInt());
            teamFPL.setStrengthDefenceHome(jsonTeam.get("strength_defence_home").asInt());
            teamFPL.setStrengthDefenceAway(jsonTeam.get("strength_defence_away").asInt());

            teams.add(teamFPL);

        }

        return teams;
        
    }

    public List<Fixture> jsonFixtureToModelFixture() throws JsonMappingException, JsonProcessingException{

        List<Fixture> fixtures = new ArrayList<>();

        JsonNode jsonFixtures = endpointAPICollector(endpoints.getFixtures());

        for (JsonNode jsonFixture : jsonFixtures){
            
            Fixture fixture = new Fixture();

            fixture.setCode(jsonFixture.get("code").asText());
            fixture.setId(jsonFixture.get("id").asText());
            fixture.setPulse_id(jsonFixture.get("pulse_id").asText());

            if (!jsonFixture.get("kickoff_time").isNull()) {
                String absoluteTime = jsonFixture.get("kickoff_time").asText();
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(absoluteTime);
                long epoch = offsetDateTime.toInstant().getEpochSecond();
                fixture.setKickoff_time(epoch);
            }

            fixture.setGameWeek(jsonFixture.get("event").asInt());

            fixture.setHomeTeamId(jsonFixture.get("team_h").asText());
            fixture.setAwayTeamId(jsonFixture.get("team_a").asText());

            fixture.setHomeScore(jsonFixture.get("team_h_score").asInt());
            fixture.setAwayScore(jsonFixture.get("team_a_score").asInt());
            fixture.setHomeTeamDifficulty(jsonFixture.get("team_h_difficulty").asInt());
            fixture.setAwayTeamDifficulty(jsonFixture.get("team_a_difficulty").asInt());

            fixture.setStarted(jsonFixture.get("started").asBoolean());
            fixture.setFinished(jsonFixture.get("finished").asBoolean());

            JsonNode statsArray = jsonFixture.get("stats");

            if (statsArray != null) {
                for (JsonNode stat : statsArray) {
                    String identifier = stat.get("identifier").asText();

                    JsonNode homeStats = stat.get("h");
                    JsonNode awayStats = stat.get("a");

                    switch (identifier) {
                        case "goals_scored":
                            fixture.setHomeScorer(FixtureProcessor.parseStats(homeStats));
                            fixture.setAwayScorer(FixtureProcessor.parseStats(awayStats));
                            break;
                        case "assists":
                            fixture.setHomeAssist(FixtureProcessor.parseStats(homeStats));
                            fixture.setAwayAssist(FixtureProcessor.parseStats(awayStats));
                            break;
                        case "yellow_cards":
                            fixture.setHomeYellowCards(FixtureProcessor.parseStats(homeStats));
                            fixture.setAwayYellowCards(FixtureProcessor.parseStats(awayStats));
                            break;
                        case "saves":
                            fixture.setHomeSaves(FixtureProcessor.parseStats(homeStats));
                            fixture.setAwaySaves(FixtureProcessor.parseStats(awayStats));
                            break;
                        case "bonus":
                            fixture.setHomeBonus(FixtureProcessor.parseStats(homeStats));
                            fixture.setAwayBonus(FixtureProcessor.parseStats(awayStats));
                            break;
                        case "bps":

                        fixture.setHomeBonus(FixtureProcessor.parseStats(homeStats));
                        fixture.setAwayBonus(FixtureProcessor.parseStats(awayStats));
                            break;
                        default:
                            break;
                        }
                    }
                }

            fixtures.add(fixture);
        }

        return fixtures;








    }
    
    public List<LiveDreamTeam> jsonDreamTeamToModelDreamTeam(int gameweek) throws JsonMappingException, JsonProcessingException{

        List<LiveDreamTeam> ids = new ArrayList<>();

        JsonNode raw = endpointAPICollector(endpoints.getLiveData(gameweek));
        JsonNode jsonPlayers = raw.get("elements");

        for (JsonNode jsonPlayer : jsonPlayers) {
            JsonNode jsonPlayerStat = jsonPlayer.get("stats");
            if (jsonPlayerStat.get("in_dreamteam").asBoolean()){
                LiveDreamTeam liveDreamTeam = new LiveDreamTeam();

                liveDreamTeam.setId(jsonPlayer.get("id").asText());
                ids.add(liveDreamTeam);
            }
        }

        return ids; 
    }

    public List<Event> jsonEventToModelEvent() throws JsonMappingException, JsonProcessingException{

        long currentEpoch = System.currentTimeMillis() / 1000;
        List<Fixture> fixtures = fixtureService.getAllFixtures();
        int currentGameWeek = CurrentGameWeek.getCurrentGameWeek(currentEpoch, fixtures);

        List<Event> events = new ArrayList<>();

        JsonNode raw = endpointAPICollector(endpoints.getBootStrap());
        JsonNode jsonEvents = raw.get("events");

        for (JsonNode jsonEvent : jsonEvents){

            if (Integer.parseInt(jsonEvent.get("id").asText()) < currentGameWeek){
                Event event = new Event();

                event.setId(jsonEvent.get("id").asText());
                event.setName(jsonEvent.get("name").asText());
                event.setDeadlineTime(jsonEvent.get("deadline_time_epoch").asLong());
                event.setAverageScore(jsonEvent.get("average_entry_score").asInt());
                event.setHighestScore(jsonEvent.get("highest_score").asInt());
                event.setHighScorer(jsonEvent.get("highest_scoring_entry").asText());

                JsonNode chipPlays = jsonEvent.get("chip_plays");
                JsonNode bboost = chipPlays.get(0);
                JsonNode freehit = chipPlays.get(1);
                
                event.setBboost(bboost.get("num_played").asInt());
                event.setFreehit(freehit.get("num_played").asInt());

                if (jsonEvent.get("id").asInt() > 1){
                JsonNode wildcard = chipPlays.get(2);
                JsonNode tripleCaptain = chipPlays.get(3);
                event.setWildcard(wildcard.get("num_played").asInt());
                event.setTripleCaptain(tripleCaptain.get("num_played").asInt());
                }

                event.setMostSelected(jsonEvent.get("most_selected").asText());
                event.setMostTransferredIn(jsonEvent.get("most_transferred_in").asText());
                event.setBestPlayer(jsonEvent.get("top_element").asText());

                event.setMostCaptained(jsonEvent.get("most_captained").asText());
                event.setMostViceCaptain(jsonEvent.get("most_vice_captained").asText());

                events.add(event);

            } 
        }

        return events;
    }
    
    public Gaffer jsonGafferToModelGaffer(String user, int gameweek) throws JsonMappingException, JsonProcessingException{
        System.out.println("Fetching Gaffer for ID: " + user + " and GameWeek: " + gameweek);

        System.out.println("Consuming API Coin");

        List<GafferPick> picks = new ArrayList<>();
        Gaffer gaffer = new Gaffer();

        JsonNode jsonGaffer = endpointAPICollector(endpoints.getManagerTeam(user, gameweek));
        JsonNode jsonGafferInfo = endpointAPICollector(endpoints.getManagerInfo(user));
        JsonNode entryHistory = jsonGaffer.get("entry_history");

        gaffer.setFirstName(jsonGafferInfo.get("player_first_name").asText());
        gaffer.setLastName(jsonGafferInfo.get("player_last_name").asText());

        List<Integer> gameweeksEntered = new ArrayList<>();
        JsonNode jsonGameWeekArray = jsonGafferInfo.get("entered_events");

        for (JsonNode jsonGameWeek: jsonGameWeekArray){
            gameweeksEntered.add(jsonGameWeek.asInt());
        }

        gaffer.setSquadName(jsonGafferInfo.get("name").asText());
        gaffer.setActive_chip(jsonGaffer.get("active_chip").asText());
        gaffer.setEvent(entryHistory.get("event").asText());
        gaffer.setPoints(entryHistory.get("points").asInt());
        gaffer.setTotalPoints(entryHistory.get("total_points").asInt());
        gaffer.setGameweekRank(entryHistory.get("rank").asInt());
        gaffer.setOverallRank(entryHistory.get("overall_rank").asInt());

        int overallAverage = gaffer.getTotalPoints() / gameweeksEntered.size();
        gaffer.setOverallAverage(overallAverage);
        gaffer.setPercentileRank(entryHistory.get("percentile_rank").asDouble());
        gaffer.setValue(entryHistory.get("value").asInt());
        gaffer.setEventTransfer(entryHistory.get("event_transfers").asInt());
        gaffer.setEventTransferCost(entryHistory.get("event_transfers_cost").asInt());
        gaffer.setPointsBench(entryHistory.get("points_on_bench").asInt());

        JsonNode jsonPicks = jsonGaffer.get("picks");

        for (JsonNode jsonPick : jsonPicks){
            GafferPick gafferPick = new GafferPick();
            gafferPick.setId(jsonPick.get("element").asText());
            gafferPick.setPosition(jsonPick.get("position").asInt());
            gafferPick.setMultiplier(jsonPick.get("multiplier").asInt());
            gafferPick.setCaptain(jsonPick.get("is_captain").asBoolean());
            gafferPick.setVice(jsonPick.get("is_vice_captain").asBoolean());
            gafferPick.setElementType(jsonPick.get("element_type").asInt());

            picks.add(gafferPick);
        }

        gaffer.setPicks(picks);

        return gaffer;

    }
    
}



