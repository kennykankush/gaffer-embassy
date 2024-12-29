package ai.lumidah.gaffer.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ai.lumidah.gaffer.model.Concept;

@Repository
public class ConceptRepository {

    @Autowired
    @Qualifier("jsonTemplate")
    private RedisTemplate<String, Object> template;

    private String conceptKey = "conceptSquad";

    public void saveConcept(Concept concept){
        template.opsForHash().put(conceptKey, concept.getId(), concept);
    }

    public Concept getConceptById(String id){
        return (Concept) template.opsForHash().get(conceptKey, id);
    }

}
