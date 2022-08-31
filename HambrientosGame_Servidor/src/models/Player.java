package models;

import java.awt.Rectangle;

public class Player {
	
	private static final int SIZE_REC_PLAYER = 60;
	private String userName;
	private String pathFileImg;
	private volatile int locationX;
	private volatile int locationY;
	private Rectangle recPlayer;
	private int score;
	
	
	public Player(String userName, String pathFileImg, int posX, int posY) {
		this.userName = userName;
		this.pathFileImg = pathFileImg;
		this.locationX = posX;
		this.locationY = posY;
		this.recPlayer = new Rectangle(getLocationX(), getLocationY(), SIZE_REC_PLAYER, SIZE_REC_PLAYER);
		this.score = 0;
	}
	
	public Player(String userName, int posX, int posY) {
		this.userName = userName;
		this.locationX = posX;
		this.locationY = posY;
	}
	
	public Player(String userName, int point) {
		this.userName = userName;
		this.score = point;
	}

	public String getUserName() {
		return userName;
	}

	public String getPathFileImg() {
		return pathFileImg;
	}

	public int getLocationX() {
		return locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int points) {
		this.score = points;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}
	
	public void setMoveRecPlayer(int posX, int posY) {
		recPlayer.setLocation(posX, posY);
	}
	
	public int getPointXRecPlayer() {
		return recPlayer.x;
	}
	
	public int getPointYRecPlayer() {
		return recPlayer.y;
	}

	public Rectangle getRecPlayer() {
		return recPlayer;
	}
}