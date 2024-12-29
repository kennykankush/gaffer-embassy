package ai.lumidah.gaffer.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import ai.lumidah.gaffer.model.Concept;
import ai.lumidah.gaffer.model.Event;
import ai.lumidah.gaffer.model.Fixture;
import ai.lumidah.gaffer.model.Gaffer;
import ai.lumidah.gaffer.model.GafferPick;
import ai.lumidah.gaffer.model.UserID;
import ai.lumidah.gaffer.model.LiveDreamTeam;
import ai.lumidah.gaffer.model.LivePlayer;
import ai.lumidah.gaffer.model.Player;
import ai.lumidah.gaffer.model.Team;
import ai.lumidah.gaffer.service.ConceptService;
import ai.lumidah.gaffer.service.EventService;
import ai.lumidah.gaffer.service.FixtureService;
import ai.lumidah.gaffer.service.GafferService;
import ai.lumidah.gaffer.service.PlayerService;
import ai.lumidah.gaffer.service.TeamService;
import ai.lumidah.gaffer.utility.ApiFetch;
import ai.lumidah.gaffer.utility.CurrentGameWeek;
import ai.lumidah.gaffer.utility.DateManagement;
import ai.lumidah.gaffer.utility.PlayerProcessor;
import ai.lumidah.gaffer.utility.TeamProcessor;
import jakarta.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private FixtureService fixtureService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private TeamProcessor teamProcessor;

    @Autowired
    private PlayerProcessor playerProcessor;

    @Autowired
    private ApiFetch apiFetch;

    @Autowired
    private GafferService gafferService;

    @Autowired
    private DateManagement dateManagement;

    @GetMapping
    public String Home(Model model) throws JsonMappingException, JsonProcessingException {

        System.out.println("home has been activated");

        if (!model.containsAttribute("userID")) {
            model.addAttribute("userID", new UserID());
        }

        List<Player> players = playerService.getAllPlayers();
        List<Fixture> fixtures = fixtureService.getAllFixtures();

        long currentEpoch = System.currentTimeMillis() / 1000;
        // long currentEpoch = 1735365600;
        int currentGameWeek = CurrentGameWeek.getCurrentGameWeek(currentEpoch, fixtures);
        long countdownEpoch = CurrentGameWeek.countdown(currentEpoch, fixtures);
        String date = DateManagement.epochToDateTime(countdownEpoch);

        //Transfer Top 10 In and Out

        List<Player> top10TransferInEvent = players.stream()
            .sorted(Comparator.comparingInt(Player::getTransfersInEvent).reversed())
            .collect(Collectors.toList());

        List<Player> top10TransferOutEvent = players.stream()
            .sorted(Comparator.comparingInt(Player::getTransfersOutEvent).reversed())
            .limit(10)
            .collect(Collectors.toList());

        List<Player> formPlayers = players.stream()
            .sorted(Comparator.comparing(Player::getForm).reversed())
            .limit(10)
            .collect(Collectors.toList());

        List<Player> premiumPlayers = players.stream()
            .filter(p -> p.getNowCost() > 80)
            .sorted(Comparator.comparing(Player::getTotalPoints).reversed())
            .limit(10)
            .collect(Collectors.toList());

        List<Player> hiddenGems =players.stream()
                .filter(p -> p.getSelectedByPercent() < 10.0)
                .sorted(Comparator.comparing(Player::getPointsPerGame).reversed())
                .limit(10)
                .collect(Collectors.toList());

        List<Player> teamOfTheWeek = new ArrayList<>();
        List<LiveDreamTeam> dreamTeamIds = apiFetch.jsonDreamTeamToModelDreamTeam(currentGameWeek);

        for (int i = 0; i < dreamTeamIds.size(); i++){
            teamOfTheWeek.add(playerService.getPlayerById(dreamTeamIds.get(i).getId()));
        }

        teamOfTheWeek.sort(Comparator.comparingInt(Player::getElementType));

        List<LivePlayer> livePlayers = playerService.getAllLivePlayers(String.valueOf(currentGameWeek - 1));

        //Event Highlight
        Event currentEvent = eventService.getEventById(String.valueOf(currentGameWeek - 1));

        //FDR
        List<Team> teams = teamService.getAllTeams();
        teams.sort(Comparator.comparingInt(team -> Integer.parseInt(team.getTeamFpl().getId())));

        model.addAttribute("top10inevent", top10TransferInEvent);
        model.addAttribute("top10outevent", top10TransferOutEvent);
        model.addAttribute("formPlayers", formPlayers);
        model.addAttribute("premiumPlayers", premiumPlayers);
        model.addAttribute("hiddenGems", hiddenGems);
        model.addAttribute("totw", teamOfTheWeek);
        model.addAttribute("livePlayers", livePlayers);
        model.addAttribute("playerProcessor", playerProcessor);
        model.addAttribute("teamProcessor", teamProcessor);

        model.addAttribute("countdown", date);
        model.addAttribute("countdownEpoch", countdownEpoch);

        model.addAttribute("currentEvent", currentEvent);
        model.addAttribute("mapName", playerService);

        model.addAttribute("teams", teams);
        model.addAttribute("currentGameWeek", currentGameWeek - 1);

        return "Home";

    }
    
    @PostMapping("/find")
    public String find(@Valid @ModelAttribute UserID requestId, BindingResult results, RedirectAttributes redirectAttributes) {

        if (results.hasErrors()){
            redirectAttributes.addFlashAttribute("userID", requestId);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userID", results);
           return "redirect:/";
        }

        String id = requestId.getId();

        return "redirect:/gaffers/" + id;
    }
    
    
    @GetMapping("/concept")
    public String showConceptCreator(Model model){
    List<Player> allPlayers = playerService.getAllPlayers();

    List<Player> goalkeepers = allPlayers.stream()
        .filter(player -> player.getElementType() == 1)
        .toList();

    List<Player> defenders = allPlayers.stream()
        .filter(player -> player.getElementType() == 2)
        .toList();

    List<Player> midfielders = allPlayers.stream()
        .filter(player -> player.getElementType() == 3)
        .toList();

    List<Player> forwards = allPlayers.stream()
        .filter(player -> player.getElementType() == 4)
        .toList();

    model.addAttribute("goalkeepers", goalkeepers);
    model.addAttribute("defenders", defenders);
    model.addAttribute("midfielders", midfielders);
    model.addAttribute("forwards", forwards);
    model.addAttribute("concept", new Concept());

    return "Concept";

    }

    @PostMapping("/concept/submit")
    public String showSubmitFollowUp(@ModelAttribute Concept concept, RedirectAttributes redirectAttributes){

        concept.setId(UUID.randomUUID().toString());
        concept.setElementType1("1");
        concept.setPosition1("1");
        
        List<Fixture> fixtures = fixtureService.getAllFixtures();
        long currentEpoch = System.currentTimeMillis() / 1000;

        int nextGameweek = CurrentGameWeek.getCurrentGameWeek(currentEpoch, fixtures);
        concept.setGameweekChallenge(String.valueOf(nextGameweek));
        conceptService.saveConcept(concept);

        System.out.println(concept);

        return "redirect:/concept/view/" + concept.getId();

    }

    @GetMapping("/concept/view/{conceptId}")
    public String showConceptSquad(@PathVariable String conceptId, Model model) {

        Concept concept = conceptService.getConceptById(conceptId);

        if (concept == null) {

            return "redirect:/concept";
        }
    
        List<LivePlayer> livePlayers = new ArrayList<>();
        List<Player> players = playerService.getAllPlayers();
        String gameWeekValue;
    
        boolean livePlayersExist = playerService.doesLivePlayersExist(concept.getGameweekChallenge());
        
        if (livePlayersExist) {
            System.out.println("livePlayerExist");
            gameWeekValue = concept.getGameweekChallenge();
            livePlayers = playerService.getAllLivePlayers(concept.getGameweekChallenge());
        } else {
            System.out.println("livePlayerExist does not exist");
            int backOne = Integer.parseInt(concept.getGameweekChallenge()) - 1;
            livePlayers = playerService.getAllLivePlayers(String.valueOf(backOne));
            gameWeekValue = String.valueOf(backOne);
        }
    
        model.addAttribute("conceptSquad", concept);
        model.addAttribute("livePlayers", livePlayers);
        model.addAttribute("playerService", playerService);
        model.addAttribute("playerProcessor", playerProcessor);
        model.addAttribute("players", players);
        model.addAttribute("gameweek", gameWeekValue);
    
        return "ConceptView";
    }
    
    @GetMapping("/players")
    public String showPlayers(Model model){

        System.out.println("Fetching Players from 'Controller/ShowHome' - /players endpoint");

        List<Player> players = playerService.getAllPlayers();
        model.addAttribute("players", players);
        model.addAttribute("teamProcessor", teamProcessor);
        

        return "Players";
    }

    @GetMapping("/players/{id}")
    public String showPlayerById(@PathVariable String id, Model model){

        System.out.println("Fetching Players from 'Controller/playerId' - /players/id endpoint");
        System.out.println("User has requested id: " + id);

        Player player = playerService.getPlayerById(id);

        if (player == null) {

            return "redirect:/concept";
        }

        model.addAttribute("player", player);
        model.addAttribute("position", PlayerProcessor.mapPlayerPosition(player, "full"));

        return "Player";
    }

    @GetMapping("/teams")
    public String showTeams(Model model){

        System.out.println("Fetching Players from 'Controller/showTeams' - /teams endpoint");

        List<Team> teams = teamService.getAllTeams();

        model.addAttribute("teams", teams);

        return "Teams";


    }

    @GetMapping("/fixtures")
    public String showFixtures(Model model){

        long currentEpoch = System.currentTimeMillis() / 1000;
        List<Fixture> fixtures = fixtureService.getAllFixtures();
        int gameWeekNumber = CurrentGameWeek.getCurrentGameWeek(currentEpoch, fixtures);

        List<Fixture> filteredFixtures = fixtures.stream()
            .filter(fixture -> fixture.getGameWeek() == gameWeekNumber)
            .sorted(Comparator.comparingLong(Fixture::getKickoff_time)) 
            .toList();

        model.addAttribute("teamProcessor", teamProcessor);
        model.addAttribute("gameWeekNumber", gameWeekNumber);
    
        model.addAttribute("fixtures", filteredFixtures);
        model.addAttribute("dateManagement", dateManagement);

        return "Fixtures";

        
    }

    @GetMapping("/fdr")
    public String showFixturesFDR(Model model){


        List<Fixture> fixtures = fixtureService.getAllFixtures();
        long currentEpoch = System.currentTimeMillis() / 1000;
        int currentGameWeek = CurrentGameWeek.getCurrentGameWeek(currentEpoch, fixtures);

        List<Team> teams = teamService.getAllTeams();
        teams.sort(Comparator.comparingInt(team -> Integer.parseInt(team.getTeamFpl().getId())));

        model.addAttribute("teams", teams);
        model.addAttribute("currentGameWeek", currentGameWeek);
        model.addAttribute("teamProcessor", teamProcessor);

        return "FixturesFDR";

    }

    @GetMapping("/fixtures/{gameweek}")
    public String showFixturesByGameWeek(@PathVariable String gameweek, Model model){
        List<Fixture> fixtures = fixtureService.getAllFixtures();

        int gameWeekNumber = Integer.parseInt(gameweek);

        List<Fixture> filteredFixtures = fixtures.stream()
        .filter(fixture -> fixture.getGameWeek() == gameWeekNumber)
        .sorted(Comparator.comparingLong(Fixture::getKickoff_time)) 
        .toList();

        model.addAttribute("teamProcessor", teamProcessor);
        model.addAttribute("gameWeekNumber", gameWeekNumber);
        model.addAttribute("fixtures", filteredFixtures);
        model.addAttribute("dateManagement", dateManagement);

        return "Fixtures";
        
    }



    @GetMapping("/gaffers/{user}")
    public String showGafferTeam(@PathVariable String user, Model model) throws JsonMappingException, JsonProcessingException{

        long currentEpoch = System.currentTimeMillis() / 1000;
        List<Fixture> fixtures = fixtureService.getAllFixtures();
        int gameWeekNumber = CurrentGameWeek.getCurrentGameWeek(currentEpoch, fixtures);

        int recentGameWeek = gameWeekNumber - 1;

        List<LivePlayer> players = playerService.getAllLivePlayers(String.valueOf(recentGameWeek));
        Gaffer gaffer = gafferService.getGaffer(user, recentGameWeek);
        List<GafferPick> picks = gaffer.getPicks();
        List<Player> photoCodePlayers = playerService.getAllPlayers();   

        String gafferRank = TeamProcessor.mapRankBadge(gaffer.getPercentileRank());
        String overallGafferRank = TeamProcessor.mapOverallBadge(gaffer.getOverallRank());

        model.addAttribute("gaffer", gaffer);
        model.addAttribute("picks", picks);
        model.addAttribute("rankImagePath", "/" + gafferRank);
        model.addAttribute("overallImagePath", "/" + overallGafferRank);
        model.addAttribute("players", players);
        model.addAttribute("playerProcessor", playerProcessor);
        model.addAttribute("playerService", playerService);
        model.addAttribute("photoCodePlayers", photoCodePlayers);

        return "Gaffer";

    }

    @GetMapping("/gaffers/{id}/{gw}")
    public String showGafferTeamByGameWeek(@PathVariable String id, @PathVariable int gw, Model model) throws JsonMappingException, JsonProcessingException{

        Gaffer gaffer = apiFetch.jsonGafferToModelGaffer(id, gw);

        model.addAttribute("gaffer", gaffer);

        return "Gaffer";
    }
    
}
