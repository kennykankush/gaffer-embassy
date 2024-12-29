package ai.lumidah.gaffer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GafferPick {

    private String id;
    private int position; //sequence, not football position
    private int multiplier;
    private boolean isCaptain;
    private boolean isVice;
    private int elementType;
    
}
