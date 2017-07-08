/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiokiller.gui.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Obsidiam
 */
public class JSONController {
    private static volatile JSONController JSON;
    static BufferedReader br = null;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static String NAME = "client_data.json";
    
    public static synchronized JSONController getInstance(){
        if(JSON == null){
            JSON = new JSONController();
        }
        return JSON;
    } 
    
    public void writeSettingsData(SettingsDataWrapper sync) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(NAME));
        gson.toJson(sync, writer);       
        writer.close();
    }
    
    public SettingsDataWrapper readSettingsData() throws FileNotFoundException{
        br = new BufferedReader(new FileReader(NAME));
        return gson.fromJson(br, SettingsDataWrapper.class);
    }
    

    public String readString(String field) throws FileNotFoundException{
        br = new BufferedReader(new FileReader(NAME));
        JsonArray entries = (JsonArray) new JsonParser().parse(br);
        return ((JsonObject)entries.get(0)).get(field).toString();
    }
    
    public int readInt32(String field) throws FileNotFoundException{
        br = new BufferedReader(new FileReader(NAME));
        JsonArray entries = (JsonArray) new JsonParser().parse(br);
        return ((JsonObject)entries.get(0)).get(field).getAsInt();
    }
    
    public long readInt64(String fieldId) throws FileNotFoundException{
        br = new BufferedReader(new FileReader(NAME));
        JsonArray entries = (JsonArray)new JsonParser().parse(br);
        return ((JsonObject)entries.get(0)).get(fieldId).getAsLong();
    }
    
    public void setFileName(String fileName){
        NAME = fileName;
    }
   
    /**
     * Left for testing perpouses, NOT an entry point!.
     * 
     */
    @Deprecated
    public static void main(String[] args) throws FileNotFoundException, IOException{
    }
}
