package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Public interface for making query to Sisu API with the chosen degree´s url
 * @author vesalukkarila, mikkojuntunen
 */
public interface iAPI {
     
       
    
    public static DegreeModule findModules (URL url) throws IOException {
        
        ArrayList <String> typesArrayList = new ArrayList<>();
        typesArrayList.add("CourseUnitRule");
        typesArrayList.add("ModuleRule");
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        
        BufferedReader textIn = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = textIn.readLine()) != null) {
            content.append(inputLine);
        }
        textIn.close();
        
        String mjono = content.toString();
        JsonElement fileElement = JsonParser.parseString(mjono);
        JsonObject fileObject = null;
        
        if (fileElement.isJsonArray()) {
            JsonArray array = fileElement.getAsJsonArray();
            for (JsonElement element : array) {
                fileObject = element.getAsJsonObject();         
            }
        }
        else {
            fileObject = fileElement.getAsJsonObject();
        }
        
        DegreeModule moduleObject = null; 
        
        //Identifying and creating object accordingly
        if (fileObject.has("courseUnitType")) {
            return handleCourse(fileObject);
        }
        
        
        else {
            String objectType = fileObject.get("type").getAsString();
            switch (objectType) {
                case "GroupingModule" ->  moduleObject = handleGroupingModule(fileObject);                   
                case "DegreeProgramme" -> moduleObject = handleDegreeModule(fileObject);                    
                case "StudyModule" -> moduleObject = handleStudyModule(fileObject);                    
                default -> {
                }
            }
        }
     
        
        
        
        
        
    }
    
    
       
    
    
    
    public static URL urlToChildNode (String nodeType, JsonObject objectInArray) 
                                                 {
        
        
    }
    
    
    
    

   
    public static DegreeModule handleCourse (JsonObject fileObject) {
       
        
    }
    
    

  
    public static DegreeModule handleStudyModule (JsonObject fileObject) {                  
    
        
  
    }
    
    
 
   
     public static DegreeModule handleGroupingModule (JsonObject fileObject) {                
    
        
    }

    
   
    public static DegreeModule handleDegreeModule (JsonObject fileObject) {                   
        
        
    }   
    
    
}
