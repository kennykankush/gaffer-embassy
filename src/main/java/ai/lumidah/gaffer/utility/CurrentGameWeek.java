package ai.lumidah.gaffer.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import ai.lumidah.gaffer.model.Fixture;

@Component
public class CurrentGameWeek {
    
    public static int getCurrentGameWeek(long epoch, List<Fixture> fixtures){

        List<Long> epochs = new ArrayList<>();

        for (int i = 0; i < fixtures.size(); i++){
            epochs.add(fixtures.get(i).getKickoff_time());

        }

        Collections.sort(epochs);

        for (int j = 0; j < epochs.size() - 1; j++){
            long time1 = epochs.get(j);
            long time2 = epochs.get(j + 1);

            if ((time1 < epoch) && (time2 > epoch)){
                // System.out.println(time1 + " < " + epoch + " > " + time2);
                return fixtures.get(j + 1).getGameWeek();
            }
        }

        return -1;
    }

    public static long countdown(long epoch, List<Fixture> fixtures){

        int gameWeekNumber = getCurrentGameWeek(epoch, fixtures);

        List<Fixture> sortedFixtures = fixtures.stream()
        .filter(fixture -> fixture.getGameWeek() == gameWeekNumber)
        .sorted(Comparator.comparingLong(Fixture::getKickoff_time))
        .toList();

        long onehour30minsbefore = 5400;
        long countdown = sortedFixtures.get(0).getKickoff_time() - onehour30minsbefore;

        return countdown;

    }

    public static boolean isLive(long epoch, List<Fixture> fixtures){

        List<Long> epochs = new ArrayList<>();

        for (int i = 0; i < fixtures.size(); i++){
            epochs.add(fixtures.get(i).getKickoff_time());

        }

        Collections.sort(epochs);

        for (int j = 0; j < epochs.size() - 1; j++){
            long time1 = epochs.get(j);
            long time2 = epochs.get(j + 1);

            if ((time1 < epoch) && (time2 > epoch)){
                int game1 = fixtures.get(j).getGameWeek();
                int game2 = fixtures.get(j + 1).getGameWeek();
                
                if ((game2 - game1 == 0)){
                    return true;
                
                } else return false;

            }
        }

        return false;

    }

    
}
