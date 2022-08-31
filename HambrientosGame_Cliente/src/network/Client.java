package network;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import models.Item;
import models.Player;
import models.Request;
import persistence.FileManager;

public class Client {

	private static final int SIZE_STREAM = 1024;
	private static final int PORT = 3005;
	private static final String RUTA_IMAGES = "src/img/";
	private static final String RUTA_AVATARS = "src/avatarsAndItem/";
	private static final String HOST = "localhost";
	private static final String NOTIFICATION_PLAYER_CREATED = "PLAYER_CREATED";
	private static final String NOTIFICATION_MAP_GAME = "MAP_GAME";
	private static final String NOTIFICATION_PAINT_PLAYER = "PAINT_PLAYER";
	private static final String MAP_GAME_NAME_FILE = "mapGame.png";
	private static final String NOTIFICATION_MOVE_OTHER_PLAYER = "MOVE_OTHER_PLAYER";
	private static final String NOTIFICATION_NEW_ITEM = "NEW_ITEM_AVALIBLE";
	private static final String NOTIFICATION_ROOM_ALMOST_COMPLETE = "NOTIFICATION_ROOM_ALMOST_COMPLETE";
	private static final String REQUEST_NEW_ITEM = "REQUEST_NEW_ITEM";
	private static final String REQUEST_POINT_WIN = "REQUEST_POINT_WIN";
	private static final String NOTIFICATION_SCORE_LIST = "NOTIFICATION_SCORE_LIST";
	private static final String NOTIFICATION_ROOM_FULL = "NOTIFICATION_ROOM_FULL";
	protected static final String NOTIFICATION_PLAYER_NUMBER_FOUR = "NOTIFICATION_PLAYER_NUMBER_FOUR";
	protected static final String NOTIFICATION_PLAYER_NUMBER_FIVE = "NOTIFICATION_PLAYER_NUMBER_FIVE";
	protected static final String NOTIFICATION_ID_ITEM_TO_DELETE = "NOTIFICATION_ID_ITEM_TO_DELETE";
	protected static final String NOTIFICATION_GAME_OVER = "NOTIFICATION_GAME_OVER";
	private static final String REQUEST_MOVE_PLAYER = "MOVE_PLAYER";
	private Socket client;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private IPresenter iPresenter;

	public Client(IPresenter iPresenter) throws UnknownHostException, IOException {
		this.iPresenter = iPresenter;
		client = new Socket(HOST, PORT);
		inputStream = new DataInputStream(client.getInputStream());
		outputStream = new DataOutputStream(client.getOutputStream());
		reading();
	}

	private void reading() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(!client.isClosed()) {
						if (inputStream.available() > 0) {
							String request = inputStream.readUTF();
							System.out.println("Peticion -> " + request);
							switch (request) {
							case NOTIFICATION_PLAYER_CREATED:
								iPresenter.showConfirmMessage(inputStream.readUTF());
								break;
							case NOTIFICATION_MAP_GAME:
								iPresenter.showMap(receivedImg());
								break;
							case NOTIFICATION_PAINT_PLAYER:
								iPresenter.addNewPlayer(receivedInfoOtherPlayer(inputStream.readUTF()));
								break;
							case NOTIFICATION_MOVE_OTHER_PLAYER:
								iPresenter.setMove(setMovements(inputStream.readUTF()));
								break;
							case NOTIFICATION_NEW_ITEM:
								iPresenter.addArrayItem(getItem());
								break;
							case NOTIFICATION_ROOM_ALMOST_COMPLETE:
								iPresenter.showMessageAlmostComplete(inputStream.readUTF());;
								break;
							case NOTIFICATION_SCORE_LIST:
								iPresenter.saveScorePlayers(saveScore(inputStream.readUTF()));
								break;
							case NOTIFICATION_ROOM_FULL:
								iPresenter.showMessageRoomFull(inputStream.readUTF());
								break;
							case NOTIFICATION_PLAYER_NUMBER_FOUR:
								iPresenter.showMessageInfoLastPlayer(inputStream.readUTF());
								break;
							case NOTIFICATION_PLAYER_NUMBER_FIVE:
								iPresenter.showMessageInfoLastPlayer(inputStream.readUTF());
								break;
							case NOTIFICATION_ID_ITEM_TO_DELETE:
								iPresenter.idItemToDelete();
								break;
							case NOTIFICATION_GAME_OVER:
								iPresenter.showMessageGameOver(inputStream.readUTF());
								break;
							default:
								break;
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * Envía los datos del jugador local al server
	 * @param player 
	 * @param nameFile nombre del archivo de la imagen del avatar
	 * @param pathImage ruta del avatar
	 */
	public void sendNewPlayer(Player player, String nameFile, String pathImage) {
		try {
			outputStream.writeUTF(Request.REGISTER_PLAYER.toString());
			JSONObject jObject = FileManager.writeNewPlayerJSON(player, nameFile);
			outputStream.writeUTF(jObject.toJSONString());
			sendImage(pathImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Envía imagenes al server. Primero se envía el tamaño y luego el vector
	 * @param pathImage ruta de la imagen
	 * @throws IOException
	 */
	private void sendImage(String pathImage) throws IOException {
		File image = new File(pathImage);
		FileInputStream fileInputStream = new FileInputStream(image);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte [] imageInBytes = new byte [SIZE_STREAM];
		int data = 0;
		while((data = fileInputStream.read(imageInBytes)) != -1) {
			out.write(imageInBytes, 0, data);
		}
		out.close();
		fileInputStream.close();
		byte[] response = out.toByteArray();
		outputStream.writeInt(response.length);
		outputStream.write(response);
	}
	
	/**
	 * Recibe la imagen que viene del server. Primero lee el nombre del archivo, el tamaño y por último el vector
	 * @return La imagen creada a partir de los datos que vienen en el vector
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Image receivedImg() throws IOException, FileNotFoundException {
		FileOutputStream fOutputStream;
		String nameFile = inputStream.readUTF();
		int sizeImgMap = inputStream.readInt();
		byte [] mapInbytes = new byte[sizeImgMap];
		inputStream.read(mapInbytes);
		if (nameFile.equalsIgnoreCase(MAP_GAME_NAME_FILE)) {
			fOutputStream = new FileOutputStream(new File(RUTA_IMAGES + nameFile));
		}else {
			fOutputStream = new FileOutputStream(new File(RUTA_AVATARS + nameFile));
		}
		fOutputStream.write(mapInbytes);
		fOutputStream.close();
		return new ImageIcon(mapInbytes).getImage();
	}
	
	/**
	 * Envía las coordenadas del jugador en un archivo JSON compuesto por nombre de usuario, coordenada en X y en Y
	 * @param player
	 */
	public void sendMovements(Player player) {
		try {
			outputStream.writeUTF(REQUEST_MOVE_PLAYER);
			outputStream.writeUTF(FileManager.writeJSONMovements(player));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Recibe la informacion de un jugador que llegan por JSON para pintarlo al inicio de la partida
	 * @param fileName archivo JSON que me llega del server
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Player receivedInfoOtherPlayer(String fileName) throws FileNotFoundException, IOException {
		return FileManager.readDataPlayerJSON(fileName, receivedImg());
	}
	
	/**
	 * Lee las coordenadas de un jugador cuando este se mueve
	 * @param fileJSON archivo que viene con el nombre del jugador 
	 * @return
	 * @throws IOException
	 */
	private Player setMovements(String fileJSON) throws IOException {
		return FileManager.readDataMovePlayerJSON(fileJSON);
	}
	
	/**
	 * Lee primero la imagen y luego lee un archivo JSON con todos los items para luego cargarlos a memoria
	 * @return una lista de items
	 * @throws IOException
	 */
	private ArrayList<Item> getItem() throws IOException {
		ArrayList<Item> itemList = new ArrayList<Item>();
		try {
			Image imgItem= receivedImg();
			itemList = FileManager.readCoordenatesItemJSON(imgItem, inputStream.readUTF());
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return itemList;
	}

	public void sendRequestNewItem() {
		try {
			outputStream.writeUTF(REQUEST_NEW_ITEM);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPointWin(String userName) {
		try {
			outputStream.writeUTF(REQUEST_POINT_WIN);
			outputStream.writeUTF(userName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<Player> saveScore(String file) {
		return FileManager.readScoreListJSON(file);
	}
}