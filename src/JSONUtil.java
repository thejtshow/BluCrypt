import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil {

	public static String generateJSON(String respContent) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("RESP", respContent);
		return jsonObject.toString();
	}
	
	public static void parseJSON(String jsonString,Credential credential) {
		JSONObject jsonObject = new JSONObject(jsonString);
		credential.device_key=jsonObject.getString("DEVICE_KEY");
		credential.user_key=jsonObject.getString("USER_KEY");
	}
	
	public static int parseJSONHeartBeat(String jsonString) {
		JSONObject jsonObject = new JSONObject(jsonString);
		if (jsonObject.has("HEARTBEAT")) {
			int heartBeat=jsonObject.getInt("HEARTBEAT");
			return heartBeat;
		}else {
			return 0;
		}
		
	}
}
