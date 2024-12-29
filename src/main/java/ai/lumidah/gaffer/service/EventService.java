package ai.lumidah.gaffer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.lumidah.gaffer.model.Event;
import ai.lumidah.gaffer.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepo;

    public void saveEvents(List<Event> events){
        eventRepo.saveEvents(events);
    }

    public List<Event> getAllEvents(){
        return eventRepo.getAllEvents();
    }

    public Event getEventById(String id){
        return eventRepo.getEventById(id);
    }
    
}
