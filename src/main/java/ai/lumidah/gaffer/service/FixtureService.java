package ai.lumidah.gaffer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.lumidah.gaffer.model.Fixture;
import ai.lumidah.gaffer.repository.FixtureRepository;

@Service
public class FixtureService {

    @Autowired
    private FixtureRepository fixtureRepo;

    public void saveFixtures(List<Fixture> fixtures){
        fixtureRepo.saveFixtures(fixtures);
        
    }

    public List<Fixture> getAllFixtures(){
        return fixtureRepo.getAllFixtures();
    }

    public void deleteAllFixtures(){
        
    }

    
    
}
