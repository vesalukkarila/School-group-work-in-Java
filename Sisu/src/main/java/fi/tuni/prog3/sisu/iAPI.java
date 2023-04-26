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
 * Public class for making query to Sisu API with the chosen degree´s url
 * @author vesalukkarila, mikkojuntunen
 */
public class iAPI {
     
       /**
     * A depth-first algorithm that recursively runs through all child-nodes
     * under the rule-element making Sisu API-querys for each of them and creates
     * objects of these child-nodes (modules) which are then added to parent´s
     * container
     * @param url url to the module
     * @return Degreemodule the baseclass of all module-classes
     * @throws IOException 
     */
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
     
            
       
        //There is 4 possible "lines" in rule-element, the following goes through
        //all of them in not so elegant if-else structure
        boolean objectHasRuleElement = fileObject.has("rule");
        
        if (objectHasRuleElement) {
            String childNodeType;
            JsonObject firstRuleObject = fileObject.get("rule").
                    getAsJsonObject();
            
            if (firstRuleObject.has("rule")) {
                JsonObject secondRuleObject = firstRuleObject.get("rule").
                        getAsJsonObject();
                
                if (secondRuleObject.has("rules")) {
                    JsonArray firstRulesArray = secondRuleObject.
                            get("rules").getAsJsonArray();
                                    
                    for ( JsonElement elementOfFirstArray : firstRulesArray) {
                        JsonObject objectInFirstArray = elementOfFirstArray.
                                getAsJsonObject();
                         
                        if (objectInFirstArray.has("rules")) {
                            JsonArray secondRulesArray = objectInFirstArray.
                                    get("rules").getAsJsonArray();

                            for ( JsonElement elementOfSecondArray : secondRulesArray) {
                                JsonObject objectInSecondArray = elementOfSecondArray.
                                        getAsJsonObject();
                                
                                // rule - rule - rules - rules - course
                                if (objectInSecondArray.has("type")) {
                                    childNodeType = objectInSecondArray.
                                            get("type").getAsString();  
                                    
                                    if (typesArrayList.contains(childNodeType)) {
                                        DegreeModule childNode = findModules
                                        (urlToChildNode(childNodeType,  
                                                objectInSecondArray));   
                                        
                                        moduleObject.addChildNodeToArrayList
                                                        (childNode);
                                    }
                                }
                            }
                        }
                        
                        // rule - rule - rules - course
                        else if (objectInFirstArray.has("type")) {
                            childNodeType = objectInFirstArray.get("type").
                                                                    getAsString();   
                            
                                if (typesArrayList.contains(childNodeType)) {
                                    DegreeModule childNode = findModules
                                    (urlToChildNode(childNodeType, 
                                                objectInFirstArray));
                                    
                                    moduleObject.addChildNodeToArrayList
                                                    (childNode);

                                    }  
                        }
                    }
                }
            } 
      
           
            else if (firstRuleObject.has("rules")) {
                JsonArray firstRulesArray = firstRuleObject.get("rules").
                                                                getAsJsonArray();
                                
                for (JsonElement elementInFirstArray : firstRulesArray) {
                    JsonObject objectInFirstArray = elementInFirstArray.getAsJsonObject();
                     
                    if (objectInFirstArray.has("rules")) {
                        JsonArray secondRulesArray = objectInFirstArray.get("rules").
                                                                getAsJsonArray();
                        
                        for (JsonElement elementInSecondArray : secondRulesArray) {
                            JsonObject objectInSecondArray = elementInSecondArray.
                                                                getAsJsonObject();
                            
                            // rule - rules - rules - course
                            if (objectInSecondArray.has("type")) {
                                    childNodeType = objectInSecondArray.
                                              get("type").getAsString();
                                    
                                    if (typesArrayList.contains(childNodeType)) {  
                                        DegreeModule childNode = findModules
                                        (urlToChildNode(childNodeType, 
                                               objectInSecondArray));
                                        
                                        moduleObject.addChildNodeToArrayList(childNode);

                                    }    
                            }
                        }   
                    }
                    
                    // rule - rules - course
                    else if (objectInFirstArray.has("type")) {
                            childNodeType = objectInFirstArray.get("type").
                                                                    getAsString();   
                            
                            if (typesArrayList.contains(childNodeType)) {
                                DegreeModule childNode = findModules(urlToChildNode
                                (childNodeType, objectInFirstArray));
                                
                                moduleObject.addChildNodeToArrayList(childNode);
                            }
                    }
                }
            }
        }   
        return moduleObject; 
    }
    
       /**
     * Builds and returns an url-address for requested node
     * @param nodeType description of the nodetype
     * @param objectInArray a JsonObject holding information needed
     * @return a url for the object in question
     * @throws MalformedURLException 
     */
    public static URL urlToChildNode (String nodeType, JsonObject objectInArray) 
            throws MalformedURLException{
                                           
        
        URL url = null;
        
        if (nodeType.equals("CourseUnitRule")) {
            String id = objectInArray.get("courseUnitGroupId").
                                                                getAsString();
                                            
            if (id.startsWith("otm")) {
                String beginning = "https://sis-tuni.funidata.fi/kori/api/course-units/";
                String addr = beginning + id;
                url = new URL(addr);
            }
                                            
            else {
                String beginning = "https://sis-tuni.funidata.fi/kori/api/course-units/"
                                                        + "by-group-id?groupId=";
                String end = "&universityId=tuni-university-root-id";
                String addr = beginning + id + end;
                url = new URL(addr); 
            }
        }
                                        
        else if (nodeType.equals("ModuleRule")) {
            String id = objectInArray.get("moduleGroupId").getAsString();
                                            
            if (id.startsWith("otm")) {
                String beginning = "https://sis-tuni.funidata.fi/kori/api/modules/";
                String addr = beginning + id;
                url = new URL(addr);
            }
                                            
            else {
                String beginning = "https://sis-tuni.funidata.fi/kori/api/modules/"
                                                        + "by-group-id?groupId=";
                String end = "&universityId=tuni-university-root-id";
                String addr = beginning + id + end;
                url = new URL(addr);
            }                              
        }
        return url;    
    }

   /**
     * Creates a Course-object
     * @param fileObject a JsonObject representing a course
     * @return DegreeModule-object, the baseclass of all classes
     */
    public static DegreeModule handleCourse (JsonObject fileObject) {
       
        String id = fileObject.get("id").getAsString();
        String groupId = fileObject.get("groupId").getAsString();
        JsonObject creditsObject = fileObject.get("credits").getAsJsonObject();
        int credits = creditsObject.get("min").getAsInt();
        JsonObject nameObject = fileObject.get("name").getAsJsonObject();
        String name;
        
        if (nameObject.has("fi"))
            name = nameObject.get("fi").getAsString();
        else
            name = nameObject.get("en").getAsString();
        
        CourseModule module = new CourseModule(name, id, groupId, credits, "course");
        return module;
    }

    /**
     * Creates a studymodule-object
     * @param fileObject a JsonObject representing a studymodule
     * @return DegreeModule-object, the baseclass of all classes
     */
    public static DegreeModule handleStudyModule (JsonObject fileObject) {                  
    
        String id = fileObject.get("id").getAsString();
        String groupId = fileObject.get("groupId").getAsString();
        JsonObject nameObject = fileObject.get("name").getAsJsonObject();
        String name;
        
        if (nameObject.has("fi"))
            name = nameObject.get("fi").getAsString();
        else
            name = nameObject.get("en").getAsString();
        
        JsonObject creditsObject = fileObject.get("targetCredits").getAsJsonObject();
        int creditsMin = creditsObject.get("min").getAsInt();
        
        StudyModule module = new StudyModule(name, id, groupId, creditsMin, "study");
        return module;
  
    }

    /**
     * Creates a groupingmodule-object
     * @param fileObject a JsonObject representing a groupingmodule
     * @return DegreeModule-object, the baseclass of all classes
     */
     public static DegreeModule handleGroupingModule (JsonObject fileObject) {                
    
        String id = fileObject.get("id").getAsString();
        String groupId = fileObject.get("groupId").getAsString();
        JsonObject nameObject = fileObject.get("name").getAsJsonObject();
        String name;
        
        if (nameObject.has("fi"))
            name = nameObject.get("fi").getAsString();
        else
            name = nameObject.get("en").getAsString();
        
        GroupingModule module = new GroupingModule(name, id, groupId, 0, "grouping");
        return module;
    }  
  
     /**
      * Creates a degreemodule-object
      * @param fileObject a JsonObject representing a degreemodule
      * @return DegreeModule-object, the baseclass of all classes
      */
    public static DegreeModule handleDegreeModule (JsonObject fileObject) {                   
        
        String id = fileObject.get("id").getAsString();
        String groupId = fileObject.get("groupId").getAsString();
        JsonObject nameObject = fileObject.get("name").getAsJsonObject();
        String name;
        
        if (nameObject.has("fi"))
            name = nameObject.get("fi").getAsString();
        else
            name = nameObject.get("en").getAsString();
        
        JsonObject creditsObject = fileObject.get("targetCredits").getAsJsonObject();
        int creditsMin = creditsObject.get("min").getAsInt();

        ActualDegreeModule module = new ActualDegreeModule(name, id, groupId, 
                                    creditsMin, "degree");
        return module;
    }  
}
