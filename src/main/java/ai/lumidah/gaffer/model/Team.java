package ai.lumidah.gaffer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    private TeamFD teamFd;
    private TeamFPL teamFpl;
    private List<Integer> fdr;
    private List<String> versus;
    private List<String> homeOrAway;

    public Team(TeamFD teamFd, TeamFPL teamFpl) {
        this.teamFd = teamFd;
        this.teamFpl = teamFpl;
    }
    

    
    
}
