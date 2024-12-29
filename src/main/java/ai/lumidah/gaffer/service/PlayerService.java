package ai.lumidah.gaffer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.lumidah.gaffer.model.LivePlayer;
import ai.lumidah.gaffer.model.Player;
import ai.lumidah.gaffer.repository.PlayerRepository;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepo;

    public boolean doesLivePlayersExist(String key){
        return playerRepo.doesLivePlayersExist(key);

    }

    public void savePlayers(List<Player> players){
        playerRepo.savePlayers(players);
    }

    public List<Player> getAllPlayers(){
        return playerRepo.getAllPlayers();
        
    }

    public Player getPlayerById(String id){
        return playerRepo.getPlayerById(id);
    }

    public String getPlayerNameById(String Id){
        return playerRepo.getPlayerNameById(Id);
    }

    public void saveLivePlayers(List<LivePlayer> players, String key){
        playerRepo.saveLivePlayers(players, key);
        
    }

    public List<LivePlayer> getAllLivePlayers(String gameweek){
        return playerRepo.getAllLivePlayers(gameweek);

    }

}
