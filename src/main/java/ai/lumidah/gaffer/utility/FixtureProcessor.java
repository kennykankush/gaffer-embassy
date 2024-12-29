package ai.lumidah.gaffer.utility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import ai.lumidah.gaffer.model.Fixture;
import ai.lumidah.gaffer.model.FixtureStat;

@Component
public class FixtureProcessor {

    public static List<FixtureStat> parseStats(JsonNode statsArray){

        List<FixtureStat> statList = new ArrayList<>();
            if (statsArray != null) {
                for (JsonNode statNode : statsArray) {
                    FixtureStat fixtureStat = new FixtureStat();
                    fixtureStat.setValue(statNode.get("value").asInt());
                    fixtureStat.setId(statNode.get("element").asText());
                    statList.add(fixtureStat);
                }
            }
    return statList;

    }

    public static int fdrCalculator(int mainTeamStrength, int challengerTeamStrength){
        int strengthDifference = mainTeamStrength - challengerTeamStrength;

        if (strengthDifference >= 3) {
            return 1;
        } else if (strengthDifference >= 1) {
            return 2;
        } else if (strengthDifference == 0) {
            return 3;
        } else if (strengthDifference >= -2) {
            return 4;
        } else {
            return 5;
        }

    }

    public static List<Integer> teamFDR(List<Fixture> fixtures, String id){

        List<Integer> fdrList = new ArrayList<>();

        for (Fixture fixture: fixtures){

            if (fixture.getHomeTeamId().equals(id)){
                int fdr = fixture.getHomeTeamDifficulty();

                fdrList.add(fdr);
                
            } else if (fixture.getAwayTeamId().equals(id)){
                int fdr = fixture.getAwayTeamDifficulty();

                fdrList.add(fdr);
            } 


        }

        return fdrList;

    }
 
    public static List<String> versusList(List<Fixture> fixtures, String id){
        
        List<String> versusList = new ArrayList<>();

        for (Fixture fixture: fixtures){

            if (fixture.getHomeTeamId().equals(id)){
                String versusId = fixture.getAwayTeamId();

                versusList.add(versusId);
                
            } else if (fixture.getAwayTeamId().equals(id)){
                String versusId = fixture.getHomeTeamId();

                versusList.add(versusId);
            } 


        }

        return versusList;

    }

    public static List<String> homeOrAwayList(List<Fixture> fixtures, String id){
        
        List<String> homeOrAwayList = new ArrayList<>();

        for (Fixture fixture: fixtures){

            if (fixture.getHomeTeamId().equals(id)){

                homeOrAwayList.add("A");
                
            } else if (fixture.getAwayTeamId().equals(id)){

                homeOrAwayList.add("H");
            } 


        }

        return homeOrAwayList;

    }
}
