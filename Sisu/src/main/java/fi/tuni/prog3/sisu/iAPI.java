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
