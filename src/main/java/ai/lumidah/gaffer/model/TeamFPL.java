package ai.lumidah.gaffer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamFPL {

    private String id;
    private String tla;
    private int strength;
    private int strengthOverallHome;
    private int strengthOverallAway;
    private int strengthAttackHome;
    private int strengthAttackAway;
    private int strengthDefenceHome;
    private int strengthDefenceAway;
    private String pulse_id;
    
}
