package ai.lumidah.gaffer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fixture {

    private String code; //code
    private String id; //id
    private String pulse_id;
    private long kickoff_time; //kick-off-time
    private int gameWeek; //event

    private String homeTeamId; //team_h
    private String AwayTeamId; //team_a
    private int homeScore; //team_h_score
    private int awayScore; //team_a_score
    private int homeTeamDifficulty; //team_h_difficulty
    private int awayTeamDifficulty; //team_a_difficulty

    private boolean started;
    private boolean finished;

    private List<FixtureStat> homeScorer;
    private List<FixtureStat> awayScorer;
    private List<FixtureStat> homeAssist;
    private List<FixtureStat> awayAssist;
    private List<FixtureStat> homeYellowCards;
    private List<FixtureStat> awayYellowCards;
    private List<FixtureStat> homeSaves;
    private List<FixtureStat> awaySaves;
    private List<FixtureStat> homeBonus;
    private List<FixtureStat> awayBonus;
    
}
