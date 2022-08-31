package models;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

public class GameService {

	private static final int RELOCATE = 60;
	private ArrayList<Player> players;
	private ArrayList<Item> items;
	private Player player;
	
	public GameService() {
		players = new ArrayList<Player>();
		items = new ArrayList<Item>();
	}

	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public static Item createItem(int id, Image imgItem, Point pointItem) {
		return new Item(id, imgItem, pointItem);
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public static Player createScorePlayer(String userName, int score) {
		return new Player(userName, score);
	}

	public void moveRight() {
		player = selectPlayer();
		player.setMovements(player.getLocation().x + RELOCATE, player.getLocation().y);
		player.getRecPlayer().setLocation(player.getLocation().x, player.getLocation().y);
	}

	public void moveLeft() {
		player = selectPlayer();
		player.setMovements(player.getLocation().x - RELOCATE, player.getLocation().y);
		player.getRecPlayer().setLocation(player.getLocation().x, player.getLocation().y);
	}

	public void moveUp() {
		player = selectPlayer();
		player.setMovements(player.getLocation().x, player.getLocation().y - RELOCATE);
		player.getRecPlayer().setLocation(player.getLocation().x, player.getLocation().y);
	}

	public void moveDown() {
		player = selectPlayer();
		player.setMovements(player.getLocation().x, player.getLocation().y + RELOCATE);
		player.getRecPlayer().setLocation(player.getLocation().x, player.getLocation().y);
	}

	public Player selectPlayer() {
		return getPlayers().get(0);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
}