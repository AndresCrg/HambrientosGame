package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MyDialogGameOver extends JDialog{

	private static final Font CAMBRIA_FONT = new Font("Cambria", Font.PLAIN, 22);
	private static final String YOU_HAVE_WON_TXT = " You have won";
	private static final String CONGRATULATION_TXT = "Congratulation! ";
	private static final long serialVersionUID = 1L;
	private static final int WIDTH_DIALOG = 400;
	private static final int HEIGHT_DIALOG = 400;
	private static final String PATH_ICON_WINNER = "/img/winner.png";
	private static final Color MY_GREEN_COLOR = Color.decode("#49c0b6");
	private static final String PATH_ICON_GAME_OVER = "/img/gameOver.png";
	private GridSystem gridSystem;
	
	public MyDialogGameOver() {
		setSize(WIDTH_DIALOG, HEIGHT_DIALOG);
		setUndecorated(true);
		getRootPane ().setOpaque (false);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - 200,
				(Toolkit.getDefaultToolkit().getScreenSize().height)/2 - 300);
		getContentPane().setBackground(MY_GREEN_COLOR);
		getContentPane().setLayout(new BorderLayout());
		gridSystem = new GridSystem(this);
	}
	
	public void publishWinner(String messege) {
		JLabel lbIconWinner = new JLabel();
		lbIconWinner.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(PATH_ICON_WINNER))
				.getImage().getScaledInstance(256, 256, Image.SCALE_DEFAULT)));
		lbIconWinner.setHorizontalAlignment(SwingConstants.CENTER);
		add(lbIconWinner,gridSystem.insertComponent(2, 1, 12, 1));
		
		JLabel lbMessage = new JLabel(CONGRATULATION_TXT + messege + YOU_HAVE_WON_TXT);
		lbMessage.setFont(CAMBRIA_FONT);
		lbMessage.setHorizontalAlignment(SwingConstants.CENTER);
		add(lbMessage,gridSystem.insertComponent(4, 1, 12, 1));
	}
	
	public void publishLoser(String messege) {
		JLabel lbIconWinner = new JLabel();
		lbIconWinner.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(PATH_ICON_GAME_OVER))
				.getImage().getScaledInstance(256, 256, Image.SCALE_DEFAULT)));
		lbIconWinner.setHorizontalAlignment(SwingConstants.CENTER);
		add(lbIconWinner,gridSystem.insertComponent(2, 1, 12, 1));
		
		JLabel lbMessage = new JLabel(messege);
		lbMessage.setFont(CAMBRIA_FONT);
		lbMessage.setHorizontalAlignment(SwingConstants.CENTER);
		add(lbMessage,gridSystem.insertComponent(4, 1, 12, 1));
	}
}