package ai.lumidah.gaffer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivePlayer {

    private String id;
    private int minutes;
    private int goals;
    private int assist;
    private double influence;
    private double creativity;
    private double threat;
    private double ictIndex;
    private String expectedGoals;
    private String expectedAssist;
    private String expectedGoalInvolvement;
    private String expectedGoalConceded;
    private int totalPoints;

}
