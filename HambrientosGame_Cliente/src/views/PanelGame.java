package views;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import models.Item;
import models.Player;

public class PanelGame extends Canvas{
	
	private static final int CURRENT_INDEX = 0;
	private static final Color MY_SALMON_COLOR = Color.decode("#ff0000");
	private static final String SCORECARD_TXT = "SCORECARD";
	private static final Color MY_BLACK_COLOR = Color.decode("#000000");
	private static final Font FONT_COOPER_BLACK_TIME = new Font("COOPER BLACK", Font.PLAIN, 40);
	private static final long serialVersionUID = 1L;
	protected static final String PATH_MUSIC_BACKGROUND = "src/music/Le Grand Chase.mp3";
	private BufferStrategy bufferStrategy;
	private ArrayList<Player> players;
	private ArrayList<Item> items;
	private Image imgMap;
	
	public PanelGame(Image imgMap, ArrayList<Player> players, ArrayList<Item> items) {
		this.players = players;
		this.imgMap = imgMap;
		this.items = items;
	}
	
	public void startGame() {
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
		setIgnoreRepaint(true);
	}
	
	public void updateGame() {
		Graphics2D graph = (Graphics2D) bufferStrategy.getDrawGraphics();
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graph.setRenderingHint( RenderingHints.  KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		try {
			paintMap(graph, imgMap);
			for (Player player : players) {
				paintPlayer(graph, player.getAvatar(), player.getLocation().x, player.getLocation().y, player.getSize());
			}
			paintItem(graph, items.get(CURRENT_INDEX).getImgItem(), items.get(CURRENT_INDEX).getPointItem().x, items.get(CURRENT_INDEX).getPointItem().y, items.get(CURRENT_INDEX).getSize());
			paintScore(graph, players);
		} finally {
			graph.dispose();
		}
		bufferStrategy.show();
	}
	
	private void paintMap(Graphics2D graph, Image mapGame) {
		graph.drawImage(mapGame, 0, 0, getWidth(), getHeight(), this);
	}
	
	private void paintPlayer(Graphics2D graph, Image avatar, int posX, int posY, int size) {
		graph.drawImage(avatar, posX, posY, size, size, this);
	}
	
	private void paintItem(Graphics2D graph, Image item, int posX, int posY, int size) {
		graph.drawImage(item, posX, posY, size, size, this);
	}
	
	private void paintScore(Graphics2D graph, ArrayList<Player> players) {
		int posInitX = 820;
		graph.setColor(MY_BLACK_COLOR);
		graph.fillRoundRect(getWidth() / 2 - 300, 30, 600, 140, 80, 80);
		graph.setColor(MY_SALMON_COLOR);
		graph.setFont(FONT_COOPER_BLACK_TIME);
		graph.drawString(SCORECARD_TXT, getWidth() / 2 - 150, 80);
		for (Player player : players) {
			graph.drawString(player.getUserName(), posInitX, 120);
			graph.drawString(String.valueOf(player.getScore()), posInitX, 150);
			posInitX += 100;
		}
	}
	
	public void playSound(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(PATH_MUSIC_BACKGROUND));
					clip.open(inputStream);
					clip.start();
				} catch (LineUnavailableException |UnsupportedAudioFileException | IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}