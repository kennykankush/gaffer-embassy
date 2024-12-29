package ai.lumidah.gaffer.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ai.lumidah.gaffer.model.Team;
import ai.lumidah.gaffer.model.TeamFD;
import ai.lumidah.gaffer.model.TeamFPL;
import ai.lumidah.gaffer.service.FixtureService;
import ai.lumidah.gaffer.utility.FixtureProcessor;

@Repository
public class TeamRepository {

    @Autowired
    @Qualifier("jsonTemplate")
    private RedisTemplate<String,Object> template;

    @Autowired
    private FixtureService fixtureService;

    private String teamKey = "teams";

    public boolean doesTeamKeyExist(){
        return template.hasKey(teamKey);
    }

    public void saveTeams(List<TeamFD> teamFDs, List<TeamFPL> teamFPLs){

        List<Team> teams = new ArrayList<>();

        Map<String, TeamFPL> fplMap = new HashMap<>(); //COMPARATOR
        for (TeamFPL teamFPL : teamFPLs) {
            fplMap.put(teamFPL.getTla(), teamFPL);
        }

        for (TeamFD teamFD : teamFDs) {
            TeamFPL matchingFpl = fplMap.get(teamFD.getTla());
            if (matchingFpl != null) {
                Team team = new Team(teamFD, matchingFpl);
                String id = matchingFpl.getId();
                List<Integer> fdr = FixtureProcessor.teamFDR(fixtureService.getAllFixtures(), id);
                System.out.println(fdr);
                List<String> versus = FixtureProcessor.versusList(fixtureService.getAllFixtures(), id);
                List<String> homeOrAway = FixtureProcessor.homeOrAwayList(fixtureService.getAllFixtures(), id);
                team.setFdr(fdr);
                team.setVersus(versus);
                team.setHomeOrAway(homeOrAway);
                teams.add(team);
            }
        }
        
        for (Team team : teams){
            template.opsForHash().put(teamKey, team.getTeamFpl().getId(), team);
        }
    }

    public List<Team> getAllTeams(){

        List<Team> teams = new ArrayList<>();

        List<Object> rawTeams = template.opsForHash().values(teamKey);

        for (Object obj: rawTeams){
            teams.add((Team) obj);
        }

        return teams;

    }
    
}
