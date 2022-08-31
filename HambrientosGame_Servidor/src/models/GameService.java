package models;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.parser.ParseException;
import persistence.FileManager;

public class GameService {
	
	private static final int MIN_NUMBER = 400;
	private static final int MAX_NUMBER = 1000;
	private ArrayList<Player> players;
	private ArrayList<Item> itemList;
	
	public GameService() {
		players = new ArrayList<Player>();
		itemList = new ArrayList<Item>();
		try {
			chargeDataFromPersistence(FileManager.readCoordenatesItemJSON());
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void chargeDataFromPersistence(ArrayList<Item> items) {
		for (Item item : items) {
			itemList.add(item);
		}
	}
	
	public static Player createPlayer(String userName, String pathFile, int posX, int posY) {
		return new Player(userName, pathFile, posX, posY);
	}
	
	public static Player createPlayer(String userName, int posX, int posY) {
		return new Player(userName, posX, posY);
	}
	
	public static Player createScorePlayer(String userName, int point) {
		return new Player(userName, point);
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public static Item createItem(int id, Point pointItem) {
		return new Item(id, pointItem);
	}
	
	public void addItem(Item item) {
		itemList.add(item);
	}
	
	public static int generateRandomNumber() {
		Random random = new Random();
		return random.ints(MIN_NUMBER, MAX_NUMBER).findFirst().getAsInt();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Item> getItemList() {
		return itemList;
	}
}