package ai.lumidah.gaffer.controller;

import org.springframework.web.bind.annotation.RestController;

import ai.lumidah.gaffer.model.Player;
import ai.lumidah.gaffer.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api")
public class RestFULController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/player/{id}")
    public ResponseEntity<Object> showPlayerAPI(@PathVariable String id) {
        

        Player player = playerService.getPlayerById(id);

         if (player == null) {
            String errorMessage = "{\"error\": \"Cannot find player " + id + "\"}";
    
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorMessage);
        }

        return ResponseEntity.ok(player);

    }
    

    
    
}
