package ai.lumidah.gaffer.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ai.lumidah.gaffer.model.Fixture;
import ai.lumidah.gaffer.model.LivePlayer;
import ai.lumidah.gaffer.model.Player;
import ai.lumidah.gaffer.utility.CurrentGameWeek;

@Repository
public class PlayerRepository {

    @Autowired
    @Qualifier("jsonTemplate")
    private RedisTemplate<String, Object> template;

    private String playersKey = "players";
    private String liveKey= "live:";

    public boolean doesPlayersKeyExist(){
        return template.hasKey(playersKey);
    }

    public void savePlayers(List<Player> players){

        for (Player player : players){
            template.opsForHash().put(playersKey, player.getId(), player);
        }
          
    }

    public List<Player> getAllPlayers(){
        
        List<Player> players = new ArrayList<>();
        
        List<Object> rawPlayers = template.opsForHash().values(playersKey);

        for (Object obj: rawPlayers){
            players.add((Player) obj);
        }

        List<Player> sortedPlayers = players.stream()
        .sorted(Comparator.comparingInt(player -> Integer.parseInt(player.getId())))
        .toList();

        return sortedPlayers;
    }

    public Player getPlayerById(String id){
        Player player = (Player) template.opsForHash().get(playersKey,id);
        return player;
    }

    public String getPlayerNameById(String id){
        Player player = (Player) template.opsForHash().get(playersKey,id);
        return player.getWebName();
    }

    public boolean doesLivePlayersExist(String key){
        return template.hasKey(liveKey + key);

    }

    public void saveLivePlayers(List<LivePlayer> players, String key){

        for (LivePlayer livePlayer: players){
            template.opsForHash().put(key, livePlayer.getId(), livePlayer);
        }

    }

    public List<LivePlayer> getAllLivePlayers(String gameweek){

        List<LivePlayer> players = new ArrayList<>();

        List<Object> rawPlayers = template.opsForHash().values(liveKey + gameweek);

        for (Object obj: rawPlayers){
            players.add((LivePlayer) obj);
        }

        return players;

    }

    public void deleteAllPlayers(long epoch, List<Fixture> fixtures){
        
        int gameweek = CurrentGameWeek.getCurrentGameWeek(epoch, fixtures);
        int gameweekBefore = gameweek - 1;

        template.delete(liveKey + gameweek);
        template.delete(liveKey + gameweekBefore);
        template.delete(playersKey);
    }
    




    
    




}
