package src.main.java;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Response {

    public static String getErrorResponse(String message){
        JSONObject response = new JSONObject();
        HashMap<String,String> dataMap = new HashMap<>(Map.of("message",message));
        response.put("result", "ERROR");
        response.put("data", dataMap);
        return response.toJSONString();
    }


}
