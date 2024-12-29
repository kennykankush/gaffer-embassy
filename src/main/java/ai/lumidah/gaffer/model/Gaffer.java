package ai.lumidah.gaffer.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gaffer {

    private String squadName; 
    private String firstName;
    private String lastName;
    private List<Integer> gameweeksEntered;
    private String active_chip;

    private String event; 
    private int points;
    private int totalPoints;
    private int overallAverage;
    private int gameweekRank;
    private int overallRank;

    private double percentileRank;
    private int value;
    private int eventTransfer;
    private int eventTransferCost;
    private int pointsBench;
    private List<GafferPick> picks;

}
