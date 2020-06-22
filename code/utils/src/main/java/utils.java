import com.google.gson.stream.JsonReader;

import java.io.FileReader;

public class utils {
    static public JsonReader _getReader(String filename){
        FileReader file = null;
        filename = "src/main/resource/" + filename;

        try{
            file = new FileReader(filename);
        }catch(Exception e){
            //System.out.println("<"+filename+">: File not found");
            return null;
        }

        return new JsonReader(file);
    }
}

