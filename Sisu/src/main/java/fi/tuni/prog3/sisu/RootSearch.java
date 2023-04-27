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
 * This class handles the rootsearch to Sisu API
 * returns all degrees in 2023
 * @author vesalukkarila, mikkojuntunen
 */
public class RootSearch {
    
    
    /**
     * Makes a query to Sisu API of all the degreeprogrammes in 2023
     * and with the help of handleAllDegreeModules, creates objects of these
     * degreeprogrammes and adds them to an arraylist
     * @return Arraylist full of degreeprogramme-objects
     * @throws MalformedURLException
     * @throws IOException 
     */
    public static ArrayList<ActualDegreeModule> allDegreeProgrammes () 
                        throws MalformedURLException, IOException {
        
        URL allDegreesUrl = new URL ("https://sis-tuni.funidata.fi/kori/api/"
                + "module-search?curriculumPeriodId=uta-lvv-2023&universityId="
                + "tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");

        HttpURLConnection con = (HttpURLConnection) allDegreesUrl.openConnection();
        con.setRequestMethod("GET");   //asiointitietoja
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        
        StringBuilder content;
        try (BufferedReader textIn = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            content = new StringBuilder();
            while ((inputLine = textIn.readLine()) != null) {
                content.append(inputLine);
            }
        }
        
        String mjono = content.toString();
        JsonElement fileElement = JsonParser.parseString(mjono);
        JsonObject fileObject = fileElement.getAsJsonObject();
        JsonArray allDegreesArray = fileObject.get("searchResults").getAsJsonArray();
        
        ArrayList<ActualDegreeModule> degreeList = new ArrayList<>();                          
        
        for (JsonElement element : allDegreesArray) {
            JsonObject degreeObject = element.getAsJsonObject();
            degreeList.add(handleAllDegreeModules(degreeObject));
        }    
        return degreeList;
    }
      
   /**
    * Creates an object from a degreeprogramme
    * @param fileObject JsonObject that inholds all information needed
    * @return ActualDegreeModule-object
    */
    public static ActualDegreeModule handleAllDegreeModules (JsonObject fileObject) {                   
        
        String id = fileObject.get("id").getAsString();
        String groupId = fileObject.get("groupId").getAsString();
        String name = fileObject.get("name").getAsString();
        JsonObject creditsObject = fileObject.get("credits").getAsJsonObject();
        int creditsMin = creditsObject.get("min").getAsInt();
        
        ActualDegreeModule olio = new ActualDegreeModule(name, id, groupId, 
                                    creditsMin, "degree");
        return olio;                                                
    } 
}
