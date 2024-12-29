package ai.lumidah.gaffer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamFD {

    private String id;
    private String name; 
    private String shortName;
    private String tla;
    private String crest;
    private String website;
    private String clubColors;
    private String venue;
    private int founded;


}
