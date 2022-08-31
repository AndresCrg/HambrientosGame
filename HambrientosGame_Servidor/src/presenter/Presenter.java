package presenter;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.GameService;
import models.Item;
import models.Player;
import network.Connection;
import network.IPresenter;
import network.Server;
import persistence.FileManager;

public class Presenter implements IPresenter{

	private static final String PLAYER_ADD_TO_LIST_LOGICAL_CORRECTLY_TXT = "Player add to list logical correctly!";
	private static final String POINT_DONE_REGISTERED = "POINT_DONE_REGISTERED";
	private Server server;
	private GameService gameService;
	private boolean reading;
	private boolean isChecking;

	public Presenter() throws IOException {
		server = new Server(this);
		gameService = new GameService();
		reading = true;
		isChecking = true;
		intiServer();
		sendPlayer();
	}

	private void intiServer() {
		try {
			server.acceptConnections();
			server.manageProxy();
		} catch (IOException e) {
			Logger.getGlobal().log(Level.SEVERE, e.toString());
		}
	}
	
	@Override
	public void addNewPlayer(Player player) {
		gameService.addPlayer(player);
		Logger.getGlobal().log(Level.INFO, PLAYER_ADD_TO_LIST_LOGICAL_CORRECTLY_TXT);
		FileManager.writePlayerListJSON(gameService.getPlayers());
	}

	public void sendPlayer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(reading) {
					if (gameService.getPlayers().size() == 3) {
						sendMessageWhenThrereAreThree();
						reading = false;
					}
					try {
						Thread.sleep(TimeUnit.SECONDS.toMillis(3));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				sendLastPlayers();
			}
		}).start();
	}
	
	public void sendMessageWhenThrereAreThree() {
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(3));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Connection connection : server.getConnections()) {
			connection.sendNotificationRoomAlmostComplete();
		}
	}

	public void sendLastPlayers() {
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(22));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (gameService.getPlayers().size() == 4) {
			server.getConnections().get(3).sendMessagePlayerNumberFour();
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(5));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if (gameService.getPlayers().size() == 5) {
			server.getConnections().get(3).sendMessagePlayerNumberFour();
			server.getConnections().get(4).sendMessagePlayerNumberFive();
			try {
				Thread.sleep(TimeUnit.SECONDS.toMillis(5));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sendDataAllPlayers();
	}

	public void sendDataAllPlayers() {
		for (Connection connection : server.getConnections()) {
			for (Player player : gameService.getPlayers()) {
				if (!(connection.getId().equals(player.getUserName()))) {
					connection.sendPlayer(player);
				}
			}
		}
		checkColision();
		checkingWhoWon();
	}

	@Override
	public void setDataMovePlayer(Player player) {
		for (Player localPlayer : gameService.getPlayers()) {
			if (localPlayer.getUserName().equals(player.getUserName())) {
				localPlayer.setLocationX(player.getLocationX());
				localPlayer.setLocationY(player.getLocationY());
				localPlayer.setMoveRecPlayer(player.getLocationX(), player.getLocationY());
			}
		}
		sendDataMoveToAllPlayers(player);
	}
	
	public void sendDataMoveToAllPlayers(Player player) {
		for (Connection connection : server.getConnections()) {
			for (Player localPlayer : gameService.getPlayers()) {
				if (!(connection.getId().equals(localPlayer.getUserName()))) {
					connection.sendDataMovements(player);
				}
			}
		}
	}
	
	public void checkColision() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Iterator<Item> iterator = gameService.getItemList().iterator();
				Item itemAux = iterator.next();
				while(isChecking) {
					for (Player player : gameService.getPlayers()) {
						if (itemAux.getRecItem().intersects(player.getRecPlayer())) {
							Logger.getGlobal().info(POINT_DONE_REGISTERED);
							addPointToThePlayer(player.getUserName());
							sendNotificationItemToDelete();
							iterator.remove();
							itemAux = iterator.next();
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void sendNotificationItemToDelete() {
		for (Connection connection : server.getConnections()) {
			connection.sendNotificationItemRemove();
		}
	}

	@Override
	public void addPointToThePlayer(String userName) {
		for (Player player : gameService.getPlayers()) {
			if (player.getUserName().equalsIgnoreCase(userName)) {
				player.setScore(player.getScore() + 1);
				try {
					FileManager.writeScoretList(gameService.getPlayers());
				} catch (IOException e) {
					Logger.getGlobal().severe("File do not write correctly!");
				}
			}
		}
		sendResponseScoreAllPlayers();
	}

	private void sendResponseScoreAllPlayers() {
		for (Connection connection : server.getConnections()) {
			connection.sendResponseScoreAllPlayers();
		}
	}

	public void checkingWhoWon() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(isChecking) {
					for (Player player : gameService.getPlayers()) {
						if (player.getScore() == 3) {
							sendMessageToWinner(player);
						}
					}
					try {
						Thread.sleep(TimeUnit.SECONDS.toMillis(2));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void sendMessageToWinner(Player player) {
		for (Connection connection : server.getConnections()) {
			if (connection.getId().equals(player.getUserName())) {
				connection.sendPlayerWonGame(player.getUserName());
			}else {
				connection.sendMessageGameOverToOtherPlayers();
			}
			isChecking = false;
		}
	}

	public static void main(String[] args) {
		try {
			new Presenter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}