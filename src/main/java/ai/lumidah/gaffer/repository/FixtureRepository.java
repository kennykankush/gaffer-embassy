package ai.lumidah.gaffer.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ai.lumidah.gaffer.model.Fixture;

@Repository
public class FixtureRepository {
    
    @Autowired
    @Qualifier("jsonTemplate")
    private RedisTemplate<String, Object> template;

    private String fixtureKey = "fixtures";

    public boolean doesFixturesKeyExist(){
        return template.hasKey(fixtureKey);
    }

    public void saveFixtures(List<Fixture> fixtures){

        for (Fixture fixture: fixtures){
            template.opsForHash().put(fixtureKey, fixture.getId(), fixture);
        }
    }

    public List<Fixture> getAllFixtures(){ //GIVES SORTED LIST
        
        List<Fixture> fixtures = new ArrayList<>();
        
        List<Object> rawFixtures= template.opsForHash().values(fixtureKey);

        for (Object obj: rawFixtures){
            fixtures.add((Fixture) obj);
        }

        List<Fixture> sortedFixtures = fixtures.stream()
            .sorted(Comparator.comparingInt(Fixture::getGameWeek)
                              .thenComparing(fixture -> Integer.parseInt(fixture.getId())))
            .toList();

        return sortedFixtures;
    }

    public void deleteAllFixtures(){
        template.delete(fixtureKey);
    }










    
}
