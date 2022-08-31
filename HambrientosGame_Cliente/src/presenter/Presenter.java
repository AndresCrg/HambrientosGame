package presenter;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Timer;
import models.Player;
import models.GameService;
import models.Item;
import network.Client;
import network.IPresenter;
import views.DialogIntro;
import views.DialogSelectAvatar;
import views.MainWindow;
import views.MyDialogGameOver;
import views.MyNotification;
import views.NewUserDialog;
import views.WaitingRoom;

public class Presenter implements ActionListener, IPresenter, KeyListener{
	private static final String FINAL_PATH_AVATAR = ".png";
	private static final String INIT_PATH_AVATAR = "src/avatarsAndItem/";
	private static final String MESSAGE_UPLOAD_SUCCESSFULLY = "Upload successfully!";
	private static final String PATH_MUSIC_BACKGROUND = "LeGrandChase.wav";
	private Player localPlayer;
	private GameService service;
	private Client client;
	private MainWindow mainWindow;
	private NewUserDialog newUserDialog;
	private DialogSelectAvatar dialogSelectAvatar;
	private DialogIntro dialogIntro;
	private Timer gameLoop;
	private String pathAvatar;
	private String nameFileImage;
	private Image imgMapAux;
	private WaitingRoom waitingRoom;
	private MyNotification myNotification;
	private MyDialogGameOver myDialogGameOver;

	public Presenter() {
		try {
			client = new Client(this);
			service = new GameService();
			newUserDialog = new NewUserDialog(this);
			dialogSelectAvatar = new DialogSelectAvatar(this);
			dialogIntro = new DialogIntro();
			myDialogGameOver = new MyDialogGameOver();
			introGame();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void introGame() {
		dialogIntro.setVisible(true);
		dialogIntro.createScene();
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		dialogIntro.setVisible(false);
		newUserDialog.setVisible(true);
	}

	public void playSound(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL musicURL = getClass().getResource(PATH_MUSIC_BACKGROUND);
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(musicURL);
					Clip clip = AudioSystem.getClip();
					clip.open(inputStream);
					clip.start();
				} catch (LineUnavailableException |UnsupportedAudioFileException | IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Actions.valueOf(e.getActionCommand())) {
		case CREATE_PLAYER:
			createPlayer();
			break;
		case SHOW_DIALOG_AVATARS:
			showAvatarsToSelect();
			break;
		case ANASTASIO:
			selectAvatar(((JButton)e.getSource()).getName());
			break;
		case CHICHICO:
			selectAvatar(((JButton)e.getSource()).getName());
			break;
		case PATACON:
			selectAvatar(((JButton)e.getSource()).getName());
			break;
		case PEPE:
			selectAvatar(((JButton)e.getSource()).getName());
			break;
		case RAMON:
			selectAvatar(((JButton)e.getSource()).getName());
			break;
		case EXIT:
			System.exit(0);
			break;
		default:
			break;
		}
	}

	private void createPlayer() {
		localPlayer = new Player(newUserDialog.getUserName(), new ImageIcon(pathAvatar).getImage());
		System.out.println(localPlayer.getUserName() + " / " + localPlayer.getRecPlayer().getLocation());
		service.addPlayer(localPlayer);
		client.sendNewPlayer(localPlayer, nameFileImage, pathAvatar);
		newUserDialog.setVisible(false);
		waitingRoom = new WaitingRoom(localPlayer);
		waitingRoom.start();
	}

	private void showAvatarsToSelect() {
		dialogSelectAvatar.setVisible(true);
	}

	private void selectAvatar(String name) {
		pathAvatar = INIT_PATH_AVATAR + name + FINAL_PATH_AVATAR;
		nameFileImage = name + FINAL_PATH_AVATAR;
		System.out.println(pathAvatar);
		myNotification = new MyNotification(dialogSelectAvatar,MESSAGE_UPLOAD_SUCCESSFULLY);
		myNotification.setVisible(true);
		dialogSelectAvatar.setVisible(false);
	}

	@Override
	public void showConfirmMessage(String message) {
		myNotification = new MyNotification(waitingRoom, message);
		myNotification.setVisible(true);
	}

	@Override
	public void addNewPlayer(Player player) {
		service.addPlayer(player);
	}

	@Override
	public void showMap(Image imgMap) {
		imgMapAux = imgMap;
		client.sendRequestNewItem();
	}

	public void startGameLoop() {
		mainWindow = new MainWindow(this, imgMapAux, service.getPlayers(), service.getItems());
		mainWindow.startGame();
		gameLoop = new Timer(16, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.updateGame();
				Toolkit.getDefaultToolkit().sync();
			}
		});
		gameLoop.start();
	}

	@Override
	public void setMove(Player toSetPlayer) {
		for (Player player : service.getPlayers()) {
			if (player.getUserName().equals(toSetPlayer.getUserName())) {
				player.setMovements(toSetPlayer.getLocation().x, toSetPlayer.getLocation().y);
			}
		}
	}

	@Override
	public void showMessageAlmostComplete(String message) {
		boolean isreading = true;
		waitingRoom.executeWorker();
		myNotification = new MyNotification(waitingRoom, message);
		myNotification.setVisible(true);
		while(isreading) {
			if (waitingRoom.getIsterminate() == 1) {
				waitingRoom.setVisible(false);
				startGameLoop();
				isreading = false;
			}
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addArrayItem(ArrayList<Item> itemList) {
		service.setItems(itemList);
		System.out.println("Tamaño de la lista de items: " + service.getItems().size());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_D) {
			service.moveRight();
			client.sendMovements(localPlayer);
		}else if (e.getKeyCode() == KeyEvent.VK_A) {
			service.moveLeft();
			client.sendMovements(localPlayer);
		}else if (e.getKeyCode() == KeyEvent.VK_W) {
			service.moveUp();
			client.sendMovements(localPlayer);
		}else if (e.getKeyCode() == KeyEvent.VK_S) {
			service.moveDown();
			client.sendMovements(localPlayer);
		}
	}

	@Override
	public void saveScorePlayers(ArrayList<Player> players) {
		for (Player playerLocal : service.getPlayers()) {
			for (Player playerFromServer : players) {
				if (playerLocal.getUserName().equalsIgnoreCase(playerFromServer.getUserName())) {
					playerLocal.setScore(playerFromServer.getScore());
				}
			}
		}
	}

	@Override
	public void showMessageRoomFull(String message) {
		myNotification = new MyNotification(waitingRoom, message);
		myNotification.setVisible(true);
	}

	@Override
	public void showMessageInfoLastPlayer(String message) {
		myNotification = new MyNotification(waitingRoom, message);
		myNotification.setVisible(true);
		waitingRoom.setVisible(false);
		startGameLoop();
	}

	@Override
	public void idItemToDelete() {
		Iterator<Item> iterator = service.getItems().iterator();
		Item item = iterator.next();
		System.out.println("item actual -> " + item);
		System.out.println("id que llega del server que se debe eliminar --> " + item.getId());
		iterator.remove();
	}

	@Override
	public void showMessageGameOver(String message) {
		if (message.equals(localPlayer.getUserName())) {
			myDialogGameOver.publishWinner(message);
			myDialogGameOver.setVisible(true);
		}else {
			myDialogGameOver.publishLoser(message);
			myDialogGameOver.setVisible(true);
		}
	}
}