package ai.lumidah.gaffer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Endpoints {

    @Value("${fplURL}")
    private String fplURL;

    @Value("${bootStrap}")
    private String bootStrap;

    @Value("${fixtures}")
    private String fixtures;

    @Value("${resources}")
    private String resources;

    @Value("${livePlayers}")
    private String livePlayers;

    @Value("${managerSquad}")
    private String managerSquad;

    @Value("${fdURL}")
    private String fdURL;

    @Value("${fdKey}")
    private String fdKey;

    @Value("${fdTeams}")
    private String fdTeams;

    @Value("${fdStandings}")
    private String fdStandings;

    //FPL

    public String getBootStrap() {
        return fplURL + bootStrap;
    }

    public String getFixtures(){
        return fplURL + fixtures;

    }

    public String getLiveData(int gameweek){
        return fplURL + livePlayers + gameweek + "/live";
    }

    public String getManagerTeam(String id, int gameweek){
        return fplURL + managerSquad + id + "/event/" + gameweek + "/picks";
    }

    public String getManagerInfo(String id){
        return fplURL + managerSquad + id;

    }
    
    //FD

    public String getFDTeams(){
        return fdURL + fdTeams;
    }

    public String getFDStandings(){
        return fdURL + fdStandings;

    }

    public String getFDKey(){
        return fdKey;
    }

}
