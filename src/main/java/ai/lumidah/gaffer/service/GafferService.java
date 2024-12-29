package ai.lumidah.gaffer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ai.lumidah.gaffer.model.Gaffer;
import ai.lumidah.gaffer.utility.ApiFetch;

@Service
public class GafferService {

    @Autowired
    private ApiFetch apiFetch;

    public Gaffer getGaffer(String user, int gw) throws JsonMappingException, JsonProcessingException{
        return apiFetch.jsonGafferToModelGaffer(user, gw);
        
    }
    
}
