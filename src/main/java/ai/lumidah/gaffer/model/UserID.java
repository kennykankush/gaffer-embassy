package ai.lumidah.gaffer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserID {

    @NotBlank(message = "ID must not be blank")
    @Pattern(regexp = "\\d+", message = "ID must contain only digits")
    private String id;
    
}
