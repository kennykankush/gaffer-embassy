package ai.lumidah.gaffer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.lumidah.gaffer.model.Team;
import ai.lumidah.gaffer.model.TeamFD;
import ai.lumidah.gaffer.model.TeamFPL;
import ai.lumidah.gaffer.repository.TeamRepository;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepo;

    public void saveTeams(List<TeamFD> teamFDs, List<TeamFPL> teamFPLs){
        teamRepo.saveTeams(teamFDs, teamFPLs);
    }

    public List<Team> getAllTeams(){
        return teamRepo.getAllTeams();
        
    }



    
}
