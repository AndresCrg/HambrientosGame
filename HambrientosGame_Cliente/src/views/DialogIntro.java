package views;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class DialogIntro extends JDialog{

	private static final String PATH_INTRO_GAME = "src/intro/IntroFinal.mp4";
	private static final long serialVersionUID = 1L;
	private static final int INTRO_GAME_SIZE = 800;
	private final JFXPanel jfxPanel = new JFXPanel();
	private JPanel panelVideo;
	private MediaPlayer oracleVid;
	private MediaView mediaView;
	
	public DialogIntro() {
		setSize(INTRO_GAME_SIZE, INTRO_GAME_SIZE);
		setUndecorated(true);
		setLayout(new BorderLayout());
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - 400,
				(Toolkit.getDefaultToolkit().getScreenSize().height)/2 - 450);
		panelVideo = new JPanel(new BorderLayout());
		panelVideo.add(jfxPanel, BorderLayout.CENTER);
		add(panelVideo, BorderLayout.CENTER);
	}
	
	public void createScene(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				File file = new File((PATH_INTRO_GAME));
				oracleVid = new MediaPlayer(new Media(file.toURI().toString()));
				mediaView = new MediaView(oracleVid);
				mediaView.setFitHeight(800);
				mediaView.setFitWidth(800);
				jfxPanel.setScene(new Scene(new Group(mediaView))); 
				oracleVid.play();
			}
		});
	}
}