package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import presenter.Actions;
import presenter.Presenter;

public class NewUserDialog extends JDialog{

	private static final int SIZE_ICON_APP = 128;
	private static final String PATH_LOGO_IN_BLACK = "/img/logoInBlack.png";
	private static final String HAMBRIENTOS_TXT = "HAMBRIENTOS!";
	private static final String NEW_PLAYER_TXT = " New Player";
	private static final String EXIT_GAME_TXT = "EXIT";
	private static final String SELECT_AVATAR_TXT = "SELECT AVATAR";
	private static final Color MY_SALMON_COLOR = Color.decode("#ff0000");
	private static final Color MY_GREEN_COLOR = Color.decode("#00c16e");
	private static final Color COLOR_BASE = Color.decode("#fefefe");
	private static final Color MY_BLACK_COLOR = Color.decode("#000000");
	private static final Font FONT_COOPER_BLACK_SUBTITLE = new Font("COOPER BLACK", Font.PLAIN, 21);
	private static final Font FONT_COOPER_BLACK_TITLE = new Font("COOPER BLACK", Font.PLAIN, 50);
	private static final long serialVersionUID = 1L;
	private static final int WIDTH_DIALOG = 500;
	private static final int HEIGHT_DIALOG = 600;
	private static final String ENTER_YOUR_NICK_NAME_TXT = "Input your nick name";
	private static final Font FONT_CANDARA_DATA = new Font("Candara", Font.PLAIN, 21);
	private static final String START_GAME_TXT = "START GAME";
	private GridSystem gridSystem;
	private JTextArea inputUserName;
	private JButton btnSelectAvatar;
	private JButton btnStartGame;
	private JButton btnExitGame;
	
	
	public NewUserDialog(Presenter presenter) {
		setSize(WIDTH_DIALOG, HEIGHT_DIALOG);
		setUndecorated(true);
		getRootPane ().setOpaque (false);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - 250,
				(Toolkit.getDefaultToolkit().getScreenSize().height)/2 - 310);
		getContentPane().setBackground(COLOR_BASE);
		gridSystem = new GridSystem(this);
		
		JLabel lbTitleGame = new JLabel(HAMBRIENTOS_TXT, SwingConstants.CENTER);
		lbTitleGame.setFont(FONT_COOPER_BLACK_TITLE);
		lbTitleGame.setForeground(MY_BLACK_COLOR);
		add(lbTitleGame,gridSystem.insertComponent(1, 1, 12, 1));
		
		JLabel lbIconApp = new JLabel();
		lbIconApp.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(PATH_LOGO_IN_BLACK))
							.getImage().getScaledInstance(SIZE_ICON_APP, SIZE_ICON_APP, Image.SCALE_DEFAULT)));
		lbIconApp.setFont(FONT_COOPER_BLACK_SUBTITLE);
		lbIconApp.setAlignmentX(CENTER_ALIGNMENT);
		lbIconApp.setHorizontalAlignment(SwingConstants.CENTER);
		add(lbIconApp,gridSystem.insertComponent(2, 1, 12, 1));
		
		JLabel lbTitle = new JLabel(NEW_PLAYER_TXT, SwingConstants.CENTER);
		lbTitle.setFont(FONT_COOPER_BLACK_TITLE);
		lbTitle.setForeground(MY_BLACK_COLOR);
		lbTitle.setAlignmentX(CENTER_ALIGNMENT);
		add(lbTitle,gridSystem.insertComponent(3, 1, 12, 1));
		
		inputUserName = new JTextArea(0,10);
		inputUserName.setBorder(BorderFactory.createTitledBorder(
								BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
								ENTER_YOUR_NICK_NAME_TXT,TitledBorder.LEFT, TitledBorder.ABOVE_TOP,
								FONT_COOPER_BLACK_SUBTITLE));
		inputUserName.setFont(FONT_CANDARA_DATA);
		add(inputUserName,gridSystem.insertComponent(5, 1, 12, 1));
		
		btnSelectAvatar = configBtn(SELECT_AVATAR_TXT, Actions.SHOW_DIALOG_AVATARS, presenter);
		add(btnSelectAvatar,gridSystem.insertComponent(7, 1, 12, 1));
		
		btnStartGame = configBtn(START_GAME_TXT, Actions.CREATE_PLAYER, presenter);
		add(btnStartGame,gridSystem.insertComponent(9, 1, 12, 1));
		
		btnExitGame = configBtn(EXIT_GAME_TXT, Actions.EXIT, presenter);
		add(btnExitGame, gridSystem.insertComponent(11, 1, 12, 1));
	}
	
	private JButton configBtn(String message, Actions action, Presenter presenter) {
		JButton btnAux = new JButton(message);
		btnAux.setFont(FONT_COOPER_BLACK_SUBTITLE);
		btnAux.setBorder(BorderFactory.createLineBorder(MY_BLACK_COLOR));
		btnAux.setBackground(MY_SALMON_COLOR);
		btnAux.setForeground(MY_BLACK_COLOR);
		btnAux.setActionCommand(action.toString());
		btnAux.addActionListener(presenter);
		btnAux.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				btnAux.setBackground(MY_SALMON_COLOR);
				btnAux.setForeground(MY_BLACK_COLOR);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				btnAux.setBackground(MY_GREEN_COLOR);
				btnAux.setForeground(Color.WHITE);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		return btnAux;
	}
	
	public String getUserName() {
		return inputUserName.getText();
	}
}