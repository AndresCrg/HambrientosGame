package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MyNotification extends JDialog{

	private static final int SIZE_ICON_CLOSE = 16;
	private static final Font CAMBRIA_FONT = new Font("Cambria", Font.PLAIN, 18);
	private static final int SIZE_ICON_INFO = 32;
	private static final Color MY_WHITE_COLOR = Color.decode("#ffffff");
	private static final String PATH_ICON_INFO = "/img/info.png";
	private static final int HEIGHT_DIALOG = 70;
	private static final int WIDTH_DIALOG = 600;
	private static final long serialVersionUID = 1L;
	private static final Color MY_BLUE_COLOR = Color.decode("#3369e7");
	private static final Color MY_BLACK_COLOR = Color.decode("#000000");
	private static final String PATH_ICON_CLOSE = "/img/closeNotification.png";
	
	public MyNotification(Container container, String message) {
		container.getFocusCycleRootAncestor();
		setSize(WIDTH_DIALOG, HEIGHT_DIALOG);
		setUndecorated(true);
		getRootPane ().setOpaque (false);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - 300,
				(Toolkit.getDefaultToolkit().getScreenSize().height)/2 - 200);
		getContentPane().setBackground(MY_WHITE_COLOR);
		getContentPane().setLayout(new BorderLayout());
		
		JLabel lbIconInfo = new JLabel();
		lbIconInfo.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(PATH_ICON_INFO))
				.getImage().getScaledInstance(SIZE_ICON_INFO, SIZE_ICON_INFO, Image.SCALE_DEFAULT)));
		lbIconInfo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		lbIconInfo.setOpaque(true);
		lbIconInfo.setBackground(MY_BLUE_COLOR);
		add(lbIconInfo, BorderLayout.LINE_START);
		
		JLabel lbMessage = new JLabel(message);
		lbMessage.setFont(CAMBRIA_FONT);
		lbMessage.setForeground(MY_BLACK_COLOR);
		lbMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lbMessage.setVerticalAlignment(SwingConstants.CENTER);
		add(lbMessage, BorderLayout.CENTER);
		
		JButton btnClose = new JButton();
		btnClose.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(PATH_ICON_CLOSE))
				.getImage().getScaledInstance(SIZE_ICON_CLOSE, SIZE_ICON_CLOSE, Image.SCALE_DEFAULT)));
		btnClose.setBackground(MY_WHITE_COLOR);
		btnClose.setOpaque(true);
		btnClose.setFocusable(false);
		btnClose.setPreferredSize(new Dimension(50, 50));
		btnClose.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		add(btnClose, BorderLayout.LINE_END);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}