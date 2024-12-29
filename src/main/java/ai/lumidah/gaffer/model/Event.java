package ai.lumidah.gaffer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private String id; //id
    private String name; //name
    private long deadlineTime; //deadline_time_epoch
    private boolean finished;

    private int averageScore; //average_entry_score
    private int highestScore; //highest_score
    private String highScorer; //highest_scoring_entry

    private int bboost; //bboost;
    private int freehit; //freehit;
    private int wildcard; //wildcard;
    private int tripleCaptain; //3xc;

    private String mostSelected; //most_selected
    private String mostTransferredIn; //most_transferred_in
    private String bestPlayer; // top_element;

    private String mostCaptained; // most_captained;
    private String mostViceCaptain; //most_vice_captained;

    
}
