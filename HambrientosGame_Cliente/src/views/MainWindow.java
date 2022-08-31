package views;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import models.Item;
import models.Player;
import presenter.Presenter;

public class MainWindow extends JFrame{

	private static final String PATH_ICON_APP = "/img/logoInBlack.png";
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "HAMBRIENTOS!";
	private PanelGame panelGame;
	
	public MainWindow(Presenter presenter, Image imgMap, ArrayList<Player> players, ArrayList<Item> items) {
		setTitle(TITLE);
		setIconImage(new ImageIcon(getClass().getResource(PATH_ICON_APP)).getImage());
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(presenter);
		initComponents(imgMap, players, items);
		setVisible(true);
	}
	
	private void initComponents(Image imgMap, ArrayList<Player> players, ArrayList<Item> items) {
		panelGame = new PanelGame(imgMap, players, items);
		this.add(panelGame, BorderLayout.CENTER);
	}
	
	public void startGame() {
		panelGame.startGame();
	}
	
	public void updateGame() {
		panelGame.updateGame();
	}
}