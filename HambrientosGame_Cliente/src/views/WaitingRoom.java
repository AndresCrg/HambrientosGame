package views;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import models.Player;

public class WaitingRoom extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final String TITLE = "HAMBRIENTOS!";
	private static final String PATH_ICON_APP = "/img/logoInBlack.png";
	private WaitRoomPnl waitRoomPnl;
	
	
	public WaitingRoom(Player player) {
		setTitle(TITLE);
		setUndecorated(true);
		setIconImage(new ImageIcon(getClass().getResource(PATH_ICON_APP)).getImage());
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initComponets(player);
		setVisible(true);
	}

	private void initComponets(Player player) {
		waitRoomPnl = new WaitRoomPnl(player);
		add(waitRoomPnl, BorderLayout.CENTER);
	}
	
	public void start() {
		waitRoomPnl.startGame();
		Timer timer  = new Timer(16, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				waitRoomPnl.updateGame();
				Toolkit.getDefaultToolkit().sync();
			}
		});
		timer.start();
	}

	public void executeWorker() {
		waitRoomPnl.modifyTime();
	}
	
	public int getIsterminate() {
		return waitRoomPnl.getIsTerminate();
	}
}