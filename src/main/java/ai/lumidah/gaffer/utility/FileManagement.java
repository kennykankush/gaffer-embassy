package ai.lumidah.gaffer.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.http.ResponseEntity;

public class FileManagement {

    public static void saveFile(ResponseEntity<String> responseEntity, String name) throws IOException{

        String responseBody = responseEntity.getBody();

        String uri = "./src/main/resources/jsons";
        File directory = new File(uri);


        if (!directory.exists()){
            directory.mkdirs();
        } else {
            System.out.println("File is at" + directory.getAbsoluteFile());
        }

        String fileName = name + ".json";

        File file = new File(directory, fileName);

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(responseBody);
        System.out.println("File saved successfully at: " + file.getAbsolutePath());

        bw.close();
        fw.close();

    }


    
}
