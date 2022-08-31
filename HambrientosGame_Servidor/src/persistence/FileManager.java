package persistence;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import models.GameService;
import models.Item;
import models.Player;

public class FileManager {
	
	private static final String PATH_SCORE_LSIT = "./data/scoreList.json";
	private static final String SCORE_LIST_KEY = "scoreList";
	private static final String TOTAL_POINTS_KEY = "points";
	private static final String PATH_FILE_COORDENATES_ITEM_JSON = "src/data/coordenatesItem.json";
	private static final String PLAYER_LIST_KEY = "playerList";
	private static final String POS_Y_KEY = "positionY";
	private static final String POS_X_KEY = "positionX";
	private static final String USER_NAME_KEY = "userName";
	private static final String JSON_FILE_WRITTER_CORRECTLY_TXT = "JSON file writter correctly!";
	private static final String PATH_PLAYER_LIST_FILE_JSON = "./data/players.json";
	private static final String COORDENATES_KEY = "coordenates";
	private static final Object ID_KEY = "id";
	private static final String PATH_COORDENATES_ITEM_JOSN = "src/data/coordenatesItem.json";
	private static final String PLAYER_KEY = "player";
	
	@SuppressWarnings("unchecked")
	public static String writePlayerListJSON(ArrayList<Player> players) {
		JSONObject objFile = new JSONObject();
		JSONArray playerList = new JSONArray();
		for (Player player : players) {
			JSONObject objPlayer = new JSONObject();
			System.out.println("On write JSON method -> posX " + player.getLocationX() + " posY " + player.getLocationY());
			objPlayer.put(USER_NAME_KEY, player.getUserName());
			objPlayer.put(POS_X_KEY, player.getLocationX());
			objPlayer.put(POS_Y_KEY, player.getLocationY());
			playerList.add(objPlayer);
		}
		objFile.put(PLAYER_LIST_KEY, playerList);
		try {
			FileWriter writer = new FileWriter(new File(PATH_PLAYER_LIST_FILE_JSON), false);
			writer.write(objFile.toJSONString());
			writer.close();
			Logger.getGlobal().log(Level.INFO, JSON_FILE_WRITTER_CORRECTLY_TXT);
		} catch (IOException e) {
			Logger.getGlobal().log(Level.SEVERE, e.toString());
		}
		return objFile.toJSONString();
	}
	
//	@SuppressWarnings("unchecked")
//	public static String readOfPlayerListJSON() {
//		String outJSON = "";
//		try {
//			ArrayList<Player> aux = new ArrayList<Player>();
//			JSONParser parser = new JSONParser();
//			FileReader reader = new FileReader(PATH_PLAYER_LIST_FILE_JSON);
//			JSONObject objFile = (JSONObject) parser.parse(reader);
//			JSONArray playerList = (JSONArray) objFile.get(PLAYER_LIST_KEY);
//			Iterator<JSONObject> iterator = playerList.iterator();
//			while(iterator.hasNext()) {
//				JSONObject objPlayer = iterator.next();
//				String userName = (String) objPlayer.get(USER_NAME_KEY);
//				Long posX = (Long) objPlayer.get(POS_X_KEY);
//				Long posY = (Long) objPlayer.get(POS_Y_KEY);
//				System.out.println("On read JSON method -> posX " + posX + " posY " + posY);
//				aux.add(new Player(userName, Integer.valueOf(posX.intValue()), Integer.valueOf(posY.intValue())));
//			}
//			outJSON = writePlayerListJSON(aux);
//		} catch (IOException | ParseException e) {
//			e.printStackTrace();
//		}
//		return outJSON;
//	}
	
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Item> readCoordenatesItemJSON() throws ParseException, IOException {
		ArrayList<Item> itemList = new ArrayList<Item>();
		JSONParser parser = new JSONParser();
		FileReader reader = new FileReader(new File(PATH_COORDENATES_ITEM_JOSN));
		JSONObject objFIle = (JSONObject) parser.parse(reader);
		JSONArray arrayCoordenate= (JSONArray) objFIle.get(COORDENATES_KEY);
		Iterator<JSONObject> iterator = arrayCoordenate.iterator();
		while(iterator.hasNext()) {
			JSONObject objItem = iterator.next();
			Long id = (Long) objItem.get(ID_KEY);
			Long posX = (Long) objItem.get(POS_X_KEY);
			Long posY = (Long) objItem.get(POS_Y_KEY);
			Point pointItem = new Point(Integer.valueOf(posX.intValue()), Integer.valueOf(posY.intValue()));
			itemList.add(GameService.createItem(Integer.valueOf(id.intValue()),pointItem));
		}
		return itemList;
	}
	
	public static String readCoordenateItemFromPersistence() throws ParseException, IOException {
		JSONParser parser = new JSONParser();
		FileReader reader = new FileReader(PATH_FILE_COORDENATES_ITEM_JSON);
		JSONObject objFile = (JSONObject) parser.parse(reader);
		System.out.println(objFile.toJSONString());
		return objFile.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static String writeScoretList(ArrayList<Player> players) throws IOException {
		JSONObject objFile = new JSONObject();
		JSONArray scoreList = new JSONArray();
		for (Player player : players) {
			JSONObject objPlayer = new JSONObject();
			objPlayer.put(USER_NAME_KEY, player.getUserName());
			objPlayer.put(TOTAL_POINTS_KEY, player.getScore());
			scoreList.add(objPlayer);
		}
		objFile.putIfAbsent(SCORE_LIST_KEY, scoreList);
		FileWriter writer = new FileWriter(new File(PATH_SCORE_LSIT));
		writer.write(objFile.toJSONString());
		writer.close();
		Logger.getGlobal().log(Level.INFO, JSON_FILE_WRITTER_CORRECTLY_TXT);
		return objFile.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static String readScoreList() {
		String out = "";
		JSONParser parser = new JSONParser();
		try {
			ArrayList<Player> auxList = new ArrayList<Player>();
			FileReader reader = new FileReader(PATH_SCORE_LSIT);
			JSONObject objFile = (JSONObject) parser.parse(reader);
			JSONArray arrayPlayer = (JSONArray) objFile.get(SCORE_LIST_KEY);
			Iterator<JSONObject> iterator = arrayPlayer.iterator();
			while(iterator.hasNext()) {
				JSONObject objPlayer = iterator.next();
				String userName = (String) objPlayer.get(USER_NAME_KEY);
				Long scorePlayer = (Long) objPlayer.get(TOTAL_POINTS_KEY);
				auxList.add(GameService.createScorePlayer(userName, Integer.valueOf(scorePlayer.intValue())));
			}
			out = writeScoretList(auxList);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return out;
	}

	public static Player readPositionOnePlayer(String filePositionsJSON) {
		Player player = null;
		try {
			JSONParser parser = new JSONParser();
			JSONObject objFile = (JSONObject) parser.parse(filePositionsJSON);
			JSONObject objPlayer = (JSONObject) objFile.get(PLAYER_KEY);
			String userName = (String) objPlayer.get(USER_NAME_KEY);
			Long posX = (Long) objPlayer.get(POS_X_KEY);
			Long posY = (Long) objPlayer.get(POS_Y_KEY);
			player = GameService.createPlayer(userName, Integer.valueOf(posX.intValue()), Integer.valueOf(posY.intValue()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return player;
	}

	@SuppressWarnings("unchecked")
	public static String writeMovePlayer(Player player) {
		JSONObject objFile = new JSONObject();
		JSONObject objPlayer = new JSONObject();
		objPlayer.put(USER_NAME_KEY, player.getUserName());
		objPlayer.put(POS_X_KEY, player.getLocationX());
		objPlayer.put(POS_Y_KEY, player.getLocationY());
		objFile.put(PLAYER_KEY, objPlayer);
		return objFile.toJSONString();
	}
}