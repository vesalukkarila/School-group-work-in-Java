/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 *An abstract class for storing information on Modules and Courses.
 */
public abstract class DegreeModule {
    
    private String name;
    private String id;
    private String groupId;
    private int minCredits;
    private String moduleType;
    private ArrayList<DegreeModule> childNodes;
    
    
    /**
     * A constructor for initializing the member variables.
     * @param name name of the Module or Course.
     * @param id id of the Module or Course.
     * @param groupId group id of the Module or Course.
     * @param minCredits minimum credits of the Module or Course.
     */
    public DegreeModule(String name, String id, String groupId, 
            int minCredits, String moduleType) {
        
        this.name = name;
        this.id = id;
        this.groupId = groupId;
        this.minCredits = minCredits;
        this.moduleType = moduleType;
        this.childNodes = new ArrayList<>();
    }
    
    
    /**
     * Returns the name of the Module or Course.
     * @return name of the Module or Course.
     */
    public String getName() {
        return this.name;
    }
    
    
    /**
     * Returns the id of the Module or Course.
     * @return id of the Module or Course.
     */
    public String getId() {
        return this.id;
    }
    
    
    /**
     * Returns the group id of the Module or Course.
     * @return group id of the Module or Course.
     */
    public String getGroupId() {
        return this.groupId;
    }
    
    
    /**
     * Returns the minimum credits of the Module or Course.
     * @return minimum credits of the Module or Course.
     */
    public int getMinCredits() {
        return this.minCredits;
    }
    
    
    public void addChildNodeToArrayList (DegreeModule childNode) {
        this.childNodes.add(childNode);
    }
    
    
    public ArrayList<DegreeModule> getArrayList () {
        return this.childNodes;
    }
    
    
    public String getType () {
        return this.moduleType;
    }
}
