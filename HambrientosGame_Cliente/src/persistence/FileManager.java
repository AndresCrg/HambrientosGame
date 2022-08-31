package persistence;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import models.GameService;
import models.Item;
import models.Player;

public class FileManager {

	private static final String COORDENATES_KEY = "coordenates";
	private static final Object ID_KEY = "id";
	private static final String PLAYER_KEY = "player";
	private static final String POS_Y_KEY = "positionY";
	private static final String POS_X_KEY = "positionX";
	private static final String USER_NAME_KEY = "userName";
	private static final String NAME_FILE_KEY = "nameFile";
	private static final String SCORE_LIST_KEY = "scoreList";
	private static final String TOTAL_POINTS_KEY = "points";
	private static JSONParser parser;

	@SuppressWarnings("unchecked")
	public static JSONObject writeNewPlayerJSON(Player player, String nameFile) {
		JSONObject jObject = new JSONObject();
		JSONObject objPlayer = new JSONObject();
		objPlayer.put(USER_NAME_KEY, player.getUserName());
		objPlayer.put(NAME_FILE_KEY, nameFile);
		objPlayer.put(POS_X_KEY, player.getLocation().x);
		objPlayer.put(POS_Y_KEY, player.getLocation().y);
		jObject.put(PLAYER_KEY, objPlayer);
		return jObject;
	}

	public static Player readDataPlayerJSON(String fileName, Image avatar) {
		Player player = null;
		parser = new JSONParser();
		try {
			JSONObject objFile = (JSONObject) parser.parse(fileName);
			JSONObject objPlayer = (JSONObject) objFile.get(PLAYER_KEY);
			String userName = (String) objPlayer.get(USER_NAME_KEY);
			Long posX = (Long) objPlayer.get(POS_X_KEY);
			Long posY = (Long) objPlayer.get(POS_Y_KEY);
			player = new Player(userName, avatar, Integer.valueOf(posX.intValue()), Integer.valueOf(posY.intValue()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return player;
	}

	@SuppressWarnings("unchecked")
	public static String writeJSONMovements(Player player) {
		JSONObject objFile = new JSONObject();
		JSONObject objPlayer = new JSONObject();
		objPlayer.put(USER_NAME_KEY, player.getUserName());
		objPlayer.put(POS_X_KEY, player.getLocation().x);
		objPlayer.put(POS_Y_KEY, player.getLocation().y);
		objFile.put(PLAYER_KEY, objPlayer);
		return objFile.toJSONString();
	}
	
	public static Player readDataMovePlayerJSON(String fileName) {
		Player player = null;
		parser = new JSONParser();
		try {
			JSONObject objFile = (JSONObject) parser.parse(fileName);
			JSONObject objPlayer = (JSONObject) objFile.get(PLAYER_KEY);
			String userName = (String) objPlayer.get(USER_NAME_KEY);
			Long posX = (Long) objPlayer.get(POS_X_KEY);
			Long posY = (Long) objPlayer.get(POS_Y_KEY);
			player = new Player(userName, Integer.valueOf(posX.intValue()), Integer.valueOf(posY.intValue()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return player;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Item> readCoordenatesItemJSON(Image imgItem, String fileJSON) throws ParseException {
		ArrayList<Item> itemList = new ArrayList<Item>();
		parser = new JSONParser();
		JSONObject objFIle = (JSONObject) parser.parse(fileJSON);
		JSONArray arrayCoordenate= (JSONArray) objFIle.get(COORDENATES_KEY);
		Iterator<JSONObject> iterator = arrayCoordenate.iterator();
		while(iterator.hasNext()) {
			JSONObject objItem = iterator.next();
			Long id = (Long) objItem.get(ID_KEY);
			Long posX = (Long) objItem.get(POS_X_KEY);
			Long posY = (Long) objItem.get(POS_Y_KEY);
			Point pointItem = new Point(Integer.valueOf(posX.intValue()), Integer.valueOf(posY.intValue()));
			itemList.add(GameService.createItem(Integer.valueOf(id.intValue()), imgItem, pointItem));
		}
		return itemList;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Player> readScoreListJSON(String file) {
		ArrayList<Player> playerListAux = new ArrayList<Player>();
		parser = new JSONParser();
		try {
			JSONObject objFile = (JSONObject) parser.parse(file);
			JSONArray arrayPlayer = (JSONArray) objFile.get(SCORE_LIST_KEY);
			Iterator<JSONObject> iterator = arrayPlayer.iterator();
			while(iterator.hasNext()) {
				JSONObject objPlayer = iterator.next();
				String userName = (String) objPlayer.get(USER_NAME_KEY);
				Long scorePlayer = (Long) objPlayer.get(TOTAL_POINTS_KEY);
				System.out.println(userName + " /  " + scorePlayer);
				playerListAux.add(GameService.createScorePlayer(userName, Integer.valueOf(scorePlayer.intValue())));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return playerListAux;
	}
}