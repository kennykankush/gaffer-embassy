package ai.lumidah.gaffer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

// -----Basic Information
private String id;
private String firstName;
private String lastName;
private String webName;
private String photo;
private int photoCode;
private String status;
private int team;
private int elementType;

// -----Transaction and Selection Info
private boolean canTransact;
private boolean canSelect;
private double selectedByPercent;
private int transfersIn;
private int transfersOut;
private int transfersInEvent;
private int transfersOutEvent;


// -----Cost Information
private int nowCost;
private int costChangeEvent;
private int costChangeStart;

// -----Performance Metrics
private int totalPoints;
private double pointsPerGame;
private double form;
private int eventPoints;
private double epNext;
private double epThis;
private boolean dreamTeam;

// -----Playing Statistics
private int minutes;
private int goalsScored;
private int assists;
private int cleanSheets;
private int goalsConceded;
private int yellowCards;
private int redCards;
private int bonus;
private int bps;

// ------Advanced Metrics
private double influence;
private double creativity;
private double threat;
private double ictIndex;


}
