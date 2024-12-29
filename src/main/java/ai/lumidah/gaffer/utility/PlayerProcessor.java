package ai.lumidah.gaffer.utility;

import java.util.List;

import org.springframework.stereotype.Component;

import ai.lumidah.gaffer.model.LivePlayer;
import ai.lumidah.gaffer.model.Player;



@Component
public class PlayerProcessor{


    public static String mapPlayerPosition(Player player, String choice) {
        if (choice.equalsIgnoreCase("full")) {
            switch (player.getElementType()) {
                case 1: return "Goalkeeper";
                case 2: return "Defender";
                case 3: return "Midfielder";
                case 4: return "Forward";
                default: return "Unknown";
            }
        } else if (choice.equalsIgnoreCase("short")) {
            switch (player.getElementType()) {
                case 1: return "GK";
                case 2: return "DEF";
                case 3: return "MID";
                case 4: return "FWD";
                default: return "UNK";
            }
        }
    
        return "Invalid choice";
    }

    public static String mapPlayerStatus(Player player) {

        if (player == null || player.getStatus() == null) {
            return "Unknown";
        }

        switch (player.getStatus()) {
            case "a": return "Available";
            case "i": return "Injured";
            case "u": return "Unavailable";
            case "d": return "Doubtful";
            case "n": return "Not in Squad";
            case "s": return "Suspended";
            case "r": return "Rested";
            default: return "Unknown";
        }
    }

    public static int mapLivePoints(String id, List<LivePlayer> players){  
        return players.stream()
        .filter(player -> player.getId().equals(id))
        .map(LivePlayer::getTotalPoints)
        .findFirst()
        .orElse(0);
        }

    public static String mapPhotoCode(String id, List<Player> players){

        return String.valueOf(players.stream()
        .filter(player -> player.getId().equals(id))
        .map(Player::getPhotoCode)
        .findFirst()
        .orElse(null));

    }
}