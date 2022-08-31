package network;

import java.awt.Image;
import java.util.ArrayList;

import models.Item;
import models.Player;

public interface IPresenter {
	void showConfirmMessage(String message);
	void showMap(Image imgMap);
	void addNewPlayer(Player player);
	void setMove(Player player);
	void showMessageAlmostComplete(String message);
	void addArrayItem(ArrayList<Item> itemList);
	void saveScorePlayers(ArrayList<Player> players);
	void showMessageRoomFull(String message);
	void showMessageInfoLastPlayer(String message);
	void idItemToDelete();
	void showMessageGameOver(String message);
}
