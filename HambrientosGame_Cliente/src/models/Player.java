package models;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class Player {
	
	private static final int POS_X_INIT = 50;
	private static final int SIZE_PLAYER = 60;
	private static final int MIN_NUMBER = 350;
	private static final int MAX_NUMBER = 950;
	private String userName;
	private Image avatar;
	private Rectangle recPlayer;
	private Point location;
	private int size;
	private int score;
	
	public Player(String userName, Image avatar) {
		this.userName = userName;
		this.avatar = avatar;
		this.location = new Point(POS_X_INIT, generateRandomNumber());
		this.recPlayer = new Rectangle(getLocation().x, getLocation().y, SIZE_PLAYER, SIZE_PLAYER);
		this.size = SIZE_PLAYER;
		this.score = 0;
	}
	
	public Player(String userName, int posX, int posY) {
		this.userName = userName;
		this.location = new Point(posX, posY);
		this.size = SIZE_PLAYER;
	}
	
	public Player(String userName, Image avatar, int posX, int posY) {
		this.userName = userName;
		this.avatar = avatar;
		this.location = new Point(posX, posY);
		this.size = SIZE_PLAYER;
	}
	
	public Player(String userName, int score) {
		this.userName = userName;
		this.score = score;
	}
	
	private int generateRandomNumber() {
		Random numberOfPrograms = new Random();
		return numberOfPrograms.ints(MIN_NUMBER, MAX_NUMBER).limit(550).findFirst().getAsInt();
	}

	public String getUserName() {
		return userName;
	}

	public Image getAvatar() {
		return avatar;
	}

	public Rectangle getRecPlayer() {
		return recPlayer;
	}

	public Point getLocation() {
		return location;
	}

	public void setMovements(int posX, int posY) {
		this.location.x = posX;
		this.location.y = posY;
	}

	public int getSize() {
		return size;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}