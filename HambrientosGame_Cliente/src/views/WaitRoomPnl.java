package views;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import models.Player;


public class WaitRoomPnl extends Canvas{
	
	private static final String SECONDS_TXT = " s";
	private static final String THE_GAME_START_IN_TXT = "The game start in ";
	private static final String WAITING_ROOM_BACKGROUND = "/img/waitingRoomBackground.jpg";
	private static final Color COLOR_BASE = Color.decode("#ffc20e");
	private static final Color MY_BLACK_COLOR = Color.decode("#000000");
	private static final Font FONT_COOPER_BLACK_TIME = new Font("COOPER BLACK", Font.PLAIN, 90);
	private static final long serialVersionUID = 1L;
	private BufferStrategy bufferStrategy;
	private SwingWorker<Integer, Integer> worker;
	private Player player;
	private int currentSecond;
	private int isTerminate = 0;
	
	public WaitRoomPnl(Player player) {
		this.player = player;
		currentSecond = 30;
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
			paintMap(graph, new ImageIcon(getClass().getResource(WAITING_ROOM_BACKGROUND)).getImage());
			paintCountDown(graph, getCurrentSecond());
			paintPlayer(graph, player.getAvatar(), getWidth() / 2 - 128, getHeight() / 2, 256);
		} finally {
			graph.dispose();
		}
		bufferStrategy.show();
	}
	
	private void paintCountDown(Graphics2D graph, int currentSec) {
		graph.setColor(COLOR_BASE);
		graph.fillRoundRect(getWidth() / 2 - 550, 70, 1100, 140, 80, 80);
		graph.setColor(MY_BLACK_COLOR);
		graph.setFont(FONT_COOPER_BLACK_TIME);
		graph.drawString(String.valueOf(THE_GAME_START_IN_TXT + getCurrentSecond() + SECONDS_TXT),(getWidth() / 2) - 480, 165);
	}
	
	private void paintMap(Graphics2D graph, Image mapGame) {
		graph.drawImage(mapGame, 0, 0, getWidth(), getHeight(), this);
	}
	
	private void paintPlayer(Graphics2D graph, Image avatar, int posX, int posY, int size) {
		graph.drawImage(avatar, posX, posY, size, size, this);
	}
	
	public void modifyTime() {
		worker = new SwingWorker<Integer, Integer>() {
			@Override
			protected Integer doInBackground() throws Exception {
				while(currentSecond > 0) {
					currentSecond = currentSecond - 1;
					publish(getCurrentSecond());
					Thread.sleep(TimeUnit.SECONDS.toMillis(1));
				}
				isTerminate = 1;
				return isTerminate;
			}
			@Override
			protected void process(List<Integer> chunks) {
				super.process(chunks);
				setCurrentSecond(chunks.get(0));
			}

			@Override
			protected void done() {
				super.done();
			}
		};
		worker.execute();
	}

	public int getCurrentSecond() {
		return currentSecond;
	}

	public void setCurrentSecond(int currentSecond) {
		this.currentSecond = currentSecond;
	}

	public int getIsTerminate() {
		return isTerminate;
	}
}