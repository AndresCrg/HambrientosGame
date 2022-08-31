package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import presenter.Actions;
import presenter.Presenter;

public class DialogSelectAvatar extends JDialog{

private static final int SIZE_AVATAR = 120;
	//	private static final Color MY_YELLOW_COLOR = Color.decode("#ffc20e");
	private static final String PATH_AVATAR_RAMON = "src/avatarsAndItem/Ramon.png";
	private static final String PATH_AVATAR_PEPE = "src/avatarsAndItem/Pepe.png";
	private static final String PATH_AVATAR_CHICHICO = "src/avatarsAndItem/Chichico.png";
	private static final String PATH_AVATAR_ANASTASIO = "src/avatarsAndItem/Anastasio.png";
	private static final String PATH_AVATAR_PATACON = "src/avatarsAndItem/Patacon.png";
	private static final String AVATARS_TITLE = "        AVATARS";
	private static final String RAMON_TXT = "Ramon";
	private static final String PEPE_TXT = "Pepe";
	private static final String CHICHICO_TXT = "Chichico";
	private static final String ANASTASIO_TXT = "Anastasio";
	private static final String PATACON_TXT = "Patacon";
	private static final long serialVersionUID = 1L;
	private static final int WIDTH_DIALOG = 850;
	private static final int HEIGHT_DIALOG = 300;
	private static final Color MY_SALMON_COLOR = Color.decode("#ff0000");
	private static final Color MY_GREEN_COLOR = Color.decode("#00c16e");
	private static final Color MY_BLACK_COLOR = Color.decode("#000000");
	private static final Color MY_WHITE_COLOR = Color.decode("#fefefe");
	private static final Font FONT_COOPER_BLACK_SUBTITLE = new Font("COOPER BLACK", Font.PLAIN, 21);
	private static final Font FONT_COOPER_BLACK_TITLE = new Font("COOPER BLACK", Font.PLAIN, 50);
	private GridSystem gridSystem;
	private JButton btnAvatarPatacon;
	private JButton btnAvatarAnastasio;
	private JButton btnAvatarChichico;
	private JButton btnAvatarPepe;
	private JButton btnAvatarRamon;

	public DialogSelectAvatar(Presenter presenter) {
		setSize(WIDTH_DIALOG, HEIGHT_DIALOG);
		setUndecorated(true);
		getRootPane ().setOpaque (false);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - 400,
				(Toolkit.getDefaultToolkit().getScreenSize().height)/2 - 240);
		getContentPane().setBackground(MY_SALMON_COLOR);

		gridSystem = new GridSystem(this);
		JLabel lbTitle = new JLabel(AVATARS_TITLE);
		lbTitle.setForeground(MY_BLACK_COLOR);
		lbTitle.setFont(FONT_COOPER_BLACK_TITLE);
		add(lbTitle, gridSystem.insertComponent(1, 3, 12, 1));

		btnAvatarPatacon = configBtn(PATACON_TXT, PATH_AVATAR_PATACON, Actions.PATACON, presenter);
		add(btnAvatarPatacon, gridSystem.insertComponent(3, 1, 1, 1));

		btnAvatarAnastasio = configBtn(ANASTASIO_TXT, PATH_AVATAR_ANASTASIO, Actions.ANASTASIO, presenter);
		add(btnAvatarAnastasio, gridSystem.insertComponent(3, 3, 1, 1));

		btnAvatarChichico = configBtn(CHICHICO_TXT, PATH_AVATAR_CHICHICO, Actions.CHICHICO, presenter);
		add(btnAvatarChichico, gridSystem.insertComponent(3, 5, 1, 1));

		btnAvatarPepe = configBtn(PEPE_TXT, PATH_AVATAR_PEPE, Actions.PEPE, presenter);
		add(btnAvatarPepe, gridSystem.insertComponent(3, 7, 1, 1));

		btnAvatarRamon = configBtn(RAMON_TXT, PATH_AVATAR_RAMON, Actions.RAMON, presenter);
		add(btnAvatarRamon, gridSystem.insertComponent(3, 9, 1, 1));
	}

	private JButton configBtn(String nameAvatar, String pathAvatar, Actions getNameAvatar, Presenter presenter) {
		JButton btnAux = new JButton(scaleIcon(pathAvatar));
		btnAux.setBackground(MY_WHITE_COLOR);
		btnAux.setOpaque(true);
		btnAux.setFocusable(false);
		btnAux.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btnAux.setFont(FONT_COOPER_BLACK_SUBTITLE);
		btnAux.setText(nameAvatar);
		btnAux.setName(nameAvatar);
		btnAux.setHorizontalTextPosition(SwingConstants.CENTER);
		btnAux.setVerticalTextPosition(SwingConstants.TOP);
		btnAux.setMargin(new Insets(0,10,0,10));
		btnAux.setActionCommand(getNameAvatar.toString());
		btnAux.addActionListener(presenter);
		btnAux.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				btnAux.setBackground(MY_GREEN_COLOR);
				btnAux.setForeground(Color.WHITE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnAux.setBackground(MY_WHITE_COLOR);
				btnAux.setForeground(Color.BLACK);
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

	private ImageIcon scaleIcon(String pathIcon) {
		ImageIcon imageIcon = new ImageIcon(new ImageIcon(pathIcon).getImage().getScaledInstance(SIZE_AVATAR, SIZE_AVATAR, Image.SCALE_DEFAULT));
		return imageIcon;
	}
}