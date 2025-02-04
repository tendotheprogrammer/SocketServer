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

    public static String getSuccessfulResponse(
            HashMap<String, Object> data,
            HashMap<String, Object> state)
    {
        JSONObject response = new JSONObject();
        response.put("result", "OK");
        response.put("data", data);
        response.put("state", state);
        return response.toJSONString();
    }

    public static HashMap<String, Object> createStateMap(
            int[] position,
            String direction,
            int shields,
            int shots,
            String status)
    {
        HashMap<String, Object> state_map = new HashMap<>();
        state_map.put("position", Arrays.asList(position[0],position[1]));
        state_map.put("direction", direction);
        state_map.put("shields", shields);
        state_map.put("shots", shots);
        state_map.put("status", status);
        return state_map;
    }
}
