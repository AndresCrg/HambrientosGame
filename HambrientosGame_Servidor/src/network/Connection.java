package network;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import models.GameService;
import models.Player;
import persistence.FileManager;

public class Connection {
	
	private static final String GAME_OVER_SORRY_YOU_ARE_LOSE_MESSAGE = "Game Over, Sorry you are lose!";
	private static final String SENT_TXT = " sent";
	private static final String RECEIVED_TXT = " received";
	private static final String REQUEST_TXT = "Request -> ";
	private static final String NEW_REQUEST_AVALIBLE_MESSAGE = "New request avalible";
	private static final String NOTIFICATION_ID_ITEM_TO_DELETE = "NOTIFICATION_ID_ITEM_TO_DELETE";
	private static final String HEY_YOU_ARE_PLAYER_4_MESSAGE = "Hey! you are player # 4";
	private static final String SORRY_THE_ROOM_IS_FULL_MESSAGE = "Sorry, the room is full";
	private static final int SIZE_STREAM = 1024;
	private static final String ONE_MOMENT_THERE_ARE_THREE_HAMBRIENTOS_IN_THE_ROOM_MESSSAGE = "One moment, there are three hambrientos in the room!";
	private static final String PLAYER_SENT = "Player sent";
	private static final String PLAYER_KEY = "player";
	private static final String POS_Y_KEY = "positionY";
	private static final String POS_X_KEY = "positionX";
	private static final String NAME_FILE_KEY = "nameFile";
	private static final String USER_NAME_KEY = "userName";
	private static final String RUTA_IMAGES = "src/img/";
	private static final String PATH_IMAGE_MAP = "src/img/mapGame.png";
	private static final String PATH_ITEM = "src/img/fried-chicken.png";
	private static final String CHECK_REGISTER_MESSAGE = "Succesfull registration!";
	private static final String NOTIFICATION_ROOM_ALMOST_COMPLETE = "NOTIFICATION_ROOM_ALMOST_COMPLETE";
	private static final String REGISTER_PLAYER = "REGISTER_PLAYER";
	private static final String NOTIFICATION_PLAYER_CREATED = "PLAYER_CREATED";
	private static final String MAP_SENT = "MAP_GAME";
	private static final String MOVE_PLAYER = "MOVE_PLAYER";
	private static final String NOTIFICATION_PAINT_PLAYER = "PAINT_PLAYER";
	private static final String DATA_MOVE_SENT = "MOVE_DATA_PLAYER_SENT";
	private static final String DATA_MOVE_PLAYER_RECEIVED = "DATA_MOVE_PLAYER_RECEIVED";
	private static final String MOVE_OTHER_PLAYER = "MOVE_OTHER_PLAYER";
	private static final String NEW_ITEM = "REQUEST_NEW_ITEM";
	private static final String NOTIFICATION_NEW_ITEM = "NEW_ITEM_AVALIBLE";
	private static final String REQUEST_NEW_ITEM_RECEIVED = "REQUEST_NEW_ITEM_RECEIVED";
	private static final String REQUEST_NEW_ITEM_SENT = "REQUEST_NEW_ITEM_SENT";
	private static final String REQUEST_POINT_WIN = "REQUEST_POINT_WIN";
	private static final String REQUEST_POINT_WIN_RECEIVED = "REQUEST_POINT_WIN_RECEIVED";
	private static final String NOTIFICATION_SCORE_LIST = "NOTIFICATION_SCORE_LIST";
	private static final String NOTIFICATION_ROOM_FULL = "NOTIFICATION_ROOM_FULL";
	private static final String NOTIFICATION_PLAYER_NUMBER_FOUR = "NOTIFICATION_PLAYER_NUMBER_FOUR";
	private static final String NOTIFICATION_PLAYER_NUMBER_FIVE = "NOTIFICATION_PLAYER_NUMBER_FIVE";
	private static final String HEY_YOU_ARE_PLAYER_5_MESSAGE = "Hey! you are player # 4";
	private static final String NOTIFICATION_GAME_OVER = "NOTIFICATION_GAME_OVER";
	private Socket socketConnect;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private JSONParser jsonParser;
	private IPresenter iPresenter;
	private String idConnection;

	public Connection(Socket socketConnect, IPresenter iPresenter) throws IOException {
		this.socketConnect = socketConnect;
		this.iPresenter = iPresenter;
		inputStream = new DataInputStream(socketConnect.getInputStream());
		outputStream = new DataOutputStream(socketConnect.getOutputStream());
		jsonParser = new JSONParser();
	}

	public void manageRequest() {
		try {
			if (socketConnect.getInputStream().available() > 0) {
				Logger.getGlobal().log(Level.INFO, NEW_REQUEST_AVALIBLE_MESSAGE);
				String request = inputStream.readUTF();
				System.out.println("Nueva peticion en el server ---> " + request);
				switch (request) {
				case REGISTER_PLAYER:
					Logger.getGlobal().log(Level.INFO, REQUEST_TXT + REGISTER_PLAYER + RECEIVED_TXT);
					iPresenter.addNewPlayer(receivedRegisterPlayer());
					sendConfirmMessage();
					Logger.getGlobal().log(Level.INFO, NOTIFICATION_PLAYER_CREATED + SENT_TXT);
					sendMapGame();
					Logger.getGlobal().log(Level.INFO, REQUEST_TXT + MAP_SENT + SENT_TXT);
					break;
				case MOVE_PLAYER:
					Logger.getGlobal().info(DATA_MOVE_PLAYER_RECEIVED);
					iPresenter.setDataMovePlayer(savePositionPlayers(inputStream.readUTF()));
					break;
				case NEW_ITEM:
					Logger.getGlobal().info(REQUEST_NEW_ITEM_RECEIVED);
					sendResponseNewItem();
					Logger.getGlobal().info(REQUEST_NEW_ITEM_SENT);
					break;
				case REQUEST_POINT_WIN:
					Logger.getGlobal().info(REQUEST_POINT_WIN_RECEIVED);
					iPresenter.addPointToThePlayer(inputStream.readUTF());
					break;
				default:
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Player savePositionPlayers(String filePositionsJSON) {
		return FileManager.readPositionOnePlayer(filePositionsJSON);
	}

	private void sendResponseNewItem() {
		try {
			outputStream.writeUTF(NOTIFICATION_NEW_ITEM);
			sendImg(PATH_ITEM);
			outputStream.writeUTF(FileManager.readCoordenateItemFromPersistence());
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private Player receivedRegisterPlayer() throws IOException {
		Player player = null;
		try {
			JSONObject objFile = (JSONObject) jsonParser.parse(inputStream.readUTF());
			JSONObject objPlayer = (JSONObject) objFile.get(PLAYER_KEY);
			String userName = (String) objPlayer.get(USER_NAME_KEY);
			String nameFile = (String) objPlayer.get(NAME_FILE_KEY);
			Long positionX = (Long) objPlayer.get(POS_X_KEY);
			Long positionY = (Long) objPlayer.get(POS_Y_KEY);
			receivedImage(nameFile);
			player = GameService.createPlayer(userName, RUTA_IMAGES + nameFile, Integer.valueOf(positionX.intValue()),
					Integer.valueOf(positionY.intValue()));
			idConnection = userName;
		} catch (ParseException e) {
			Logger.getGlobal().log(Level.SEVERE, e.toString());
		}
		return player;
	}

	private Image receivedImage(String pathAvatar) throws IOException {
		int sizeImg = inputStream.readInt();
		byte [] imageInbytes = new byte[sizeImg];
		inputStream.read(imageInbytes);
		FileOutputStream fOutputStream = new FileOutputStream(new File(RUTA_IMAGES + pathAvatar));
		fOutputStream.write(imageInbytes);
		fOutputStream.close();
		return new ImageIcon(imageInbytes).getImage();
	}

	private void sendConfirmMessage() throws IOException {
		outputStream.writeUTF(NOTIFICATION_PLAYER_CREATED);
		outputStream.writeUTF(CHECK_REGISTER_MESSAGE);
	}

	private void sendMapGame() throws IOException {
		outputStream.writeUTF(MAP_SENT);
		sendImg(PATH_IMAGE_MAP);
	}

	@SuppressWarnings("unchecked")
	private void createJSON(Player player) throws IOException {
		JSONObject jObject = new JSONObject();
		JSONObject objPlayer = new JSONObject();
		objPlayer.put(USER_NAME_KEY, player.getUserName());
		objPlayer.put(POS_X_KEY, player.getLocationX());
		objPlayer.put(POS_Y_KEY, player.getLocationY());
		jObject.put(PLAYER_KEY, objPlayer);
		outputStream.writeUTF(jObject.toJSONString());
	}

	private void sendImg(String pathFile) throws IOException {
		File fMap = new File(pathFile);
		FileInputStream fileStream = new FileInputStream(fMap);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int data = 0;
		byte [] aux = new byte [SIZE_STREAM];
		while((data = fileStream.read(aux)) != -1) {
			out.write(aux, 0, data);
		}
		out.close();
		fileStream.close();
		byte [] dataInBytes = out.toByteArray();
		outputStream.writeUTF(fMap.getName());
		outputStream.writeInt(dataInBytes.length);
		outputStream.write(dataInBytes);
	}

	public void sendPlayer(Player player) {
		try {
			outputStream.writeUTF(NOTIFICATION_PAINT_PLAYER);
			createJSON(player);
			sendImg(player.getPathFileImg());
			Logger.getGlobal().info(PLAYER_SENT);
		} catch (IOException e) {
			Logger.getGlobal().severe(e.toString());;
		}
	}

	public void sendDataMovements(Player player) {
		try {
			outputStream.writeUTF(MOVE_OTHER_PLAYER);
			outputStream.writeUTF(FileManager.writeMovePlayer(player));
			Logger.getGlobal().info(DATA_MOVE_SENT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendNotificationRoomAlmostComplete() {
		try {
			outputStream.writeUTF(NOTIFICATION_ROOM_ALMOST_COMPLETE);
			outputStream.writeUTF(ONE_MOMENT_THERE_ARE_THREE_HAMBRIENTOS_IN_THE_ROOM_MESSSAGE);
			Logger.getGlobal().info(NOTIFICATION_ROOM_ALMOST_COMPLETE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendResponseScoreAllPlayers() {
		try {
			outputStream.writeUTF(NOTIFICATION_SCORE_LIST);
			outputStream.writeUTF(FileManager.readScoreList());
			Logger.getGlobal().info(NOTIFICATION_SCORE_LIST);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessageRoomFull() {
		try {
			outputStream.writeUTF(NOTIFICATION_ROOM_FULL);
			outputStream.writeUTF(SORRY_THE_ROOM_IS_FULL_MESSAGE);
			Logger.getGlobal().info(NOTIFICATION_ROOM_FULL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessagePlayerNumberFour() {
		try {
			outputStream.writeUTF(NOTIFICATION_PLAYER_NUMBER_FOUR);
			outputStream.writeUTF(HEY_YOU_ARE_PLAYER_4_MESSAGE);
			Logger.getGlobal().info(NOTIFICATION_PLAYER_NUMBER_FOUR);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void sendMessagePlayerNumberFive() {
		try {
			outputStream.writeUTF(NOTIFICATION_PLAYER_NUMBER_FIVE);
			outputStream.writeUTF(HEY_YOU_ARE_PLAYER_5_MESSAGE);
			Logger.getGlobal().info(NOTIFICATION_PLAYER_NUMBER_FIVE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendNotificationItemRemove() {
		try {
			outputStream.writeUTF(NOTIFICATION_ID_ITEM_TO_DELETE);
			Logger.getGlobal().info(NOTIFICATION_ID_ITEM_TO_DELETE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPlayerWonGame(String winnerPlayer) {
		try {
			outputStream.writeUTF(NOTIFICATION_GAME_OVER);
			outputStream.writeUTF(winnerPlayer);
			Logger.getGlobal().info(NOTIFICATION_GAME_OVER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageGameOverToOtherPlayers() {
		try {
			outputStream.writeUTF(NOTIFICATION_GAME_OVER);
			outputStream.writeUTF(GAME_OVER_SORRY_YOU_ARE_LOSE_MESSAGE);
			Logger.getGlobal().info(NOTIFICATION_GAME_OVER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getId() {
		return idConnection;
	}
}