package network;

import models.Player;

public interface IPresenter {
	void addNewPlayer(Player player);
	void setDataMovePlayer(Player player);
	void addPointToThePlayer(String userName);
}
