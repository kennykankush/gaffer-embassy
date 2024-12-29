package ai.lumidah.gaffer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.lumidah.gaffer.model.Concept;
import ai.lumidah.gaffer.repository.ConceptRepository;

@Service
public class ConceptService {

    @Autowired
    private ConceptRepository conceptRepository;

    public void saveConcept(Concept concept){
        conceptRepository.saveConcept(concept);
    }

    public Concept getConceptById(String id){
        return conceptRepository.getConceptById(id);
    }
    
}
