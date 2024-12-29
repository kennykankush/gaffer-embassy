package ai.lumidah.gaffer.utility;

import org.springframework.stereotype.Component;

@Component
public class TeamProcessor {
    
    public static String mapIdToShortTeamName(String id){

            switch (id) {
                case "1": return "ARS";
                case "2": return "AVL";
                case "3": return "BOU";
                case "4": return "BRE";
                case "5": return "BHA";
                case "6": return "CHE";
                case "7": return "CRY";
                case "8": return "EVE";
                case "9": return "FUL";
                case "10": return "IPS";
                case "11": return "LEI";
                case "12": return "LIV";
                case "13": return "MCI";
                case "14": return "MUN";
                case "15": return "NEW";
                case "16": return "NFO";
                case "17": return "SOU";
                case "18": return "TOT";
                case "19": return "WHU";
                case "20": return "WOL";
                default: return "NTF";
            }
        }

    public static String mapIdToFullTeamName(String id){

            switch (id) {
                case "1": return "Arsenal";
                case "2": return "Aston Villa";
                case "3": return "Bournemouth";
                case "4": return "Brentford";
                case "5": return "Brighton";
                case "6": return "Chelsea";
                case "7": return "Crystal Palace";
                case "8": return "Everton";
                case "9": return "Fulham";
                case "10": return "Ipswich";
                case "11": return "Leichester City";
                case "12": return "Liverpool";
                case "13": return "Manchester City";
                case "14": return "Manchester United";
                case "15": return "Newcastle United";
                case "16": return "Nottingham Forest";
                case "17": return "Southampton";
                case "18": return "Tottenham Spurs";
                case "19": return "West Ham";
                case "20": return "Wolves"; 
                default: return "NTF";
            }
        }

    public static String mapTeamLogo(String id){

        return id + ".png";

    }

    public static String mapRankBadge(double percentile){

        if (percentile < 5.0) {
            return "Challenger.png";
        } else if (percentile < 20.0) {
            return "Platinum.png";
        } else if (percentile < 40.0) {
            return "Gold.png";
        } else if (percentile < 65.0) {
            return "Silver.png";
        } else if (percentile < 80.0){
            return "Bronze.png";
        } else {
            return "Iron.png";
        }

    }

    public static String mapOverallBadge(int rank){

        if (rank < 100000) {
            return "Challenger.png";
        } else if (rank < 1000000) {
            return "Platinum.png";
        } else if (rank < 2500000) {
            return "Gold.png";
        } else if (rank < 6500000) {
            return "Silver.png";
        } else if (rank < 900000){
            return "Bronze.png";
        } else {
            return "Iron.png";
        }

    }
}
