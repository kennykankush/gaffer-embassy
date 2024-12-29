package ai.lumidah.gaffer.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ai.lumidah.gaffer.model.Event;

@Repository
public class EventRepository {

    @Autowired
    @Qualifier("jsonTemplate")
    private RedisTemplate<String,Object> template;

    private String eventKey = "events";
    
    public boolean doesEventKeyExist(){
        return template.hasKey(eventKey);
    }

    public void saveEvents(List<Event> events){

        for (Event event: events){
            template.opsForHash().put(eventKey, event.getId(), event);
        }
    }

    public List<Event> getAllEvents(){

        List<Event> events = new ArrayList<>();

        List<Object> rawEvents = template.opsForHash().values(eventKey);

        for (Object obj: rawEvents){
            events.add((Event) obj);
        }

        List<Event> sortedEvents = events.stream()
            .sorted(Comparator.comparingInt(event -> Integer.parseInt(event.getId())))
            .toList();

        return sortedEvents;

    }

    public Event getEventById(String id){
        return (Event) template.opsForHash().get(eventKey,id);
    }
    
    public void deleteAllEvent(){
        template.delete(eventKey);
    }
}
