package views;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SelectFolderFileChooser extends JDialog{

	private static final String CANCEL_SELECTION = "canceló la selection";
	private static final String EXTENSION_BMP = "bmp";
	private static final String EXTENSION_PNG = "png";
	private static final String EXTENSION_JPG = "jpg";
	private static final String EXTENSION_JPEG = "jpeg";
	private static final String DESCRIPTION_FILE_CHOOSER = "Only images";
	private static final int HEIGHT_FILE_CHOOSER = 400;
	private static final int WIDTH_FILE_CHOOSER = 900;
	private static final String SELECT_AVATAR_TXT = "Select Avatar";
	private static final long serialVersionUID = 1L;
	private JFileChooser fChooser;
	
	public SelectFolderFileChooser() {
		setSize(WIDTH_FILE_CHOOSER, HEIGHT_FILE_CHOOSER);
		setLocationRelativeTo(null);
		setModal(true);
		
		fChooser = new JFileChooser();
		fChooser.setDialogTitle(SELECT_AVATAR_TXT);
		fChooser.setFileFilter(new FileNameExtensionFilter(DESCRIPTION_FILE_CHOOSER, EXTENSION_JPEG, EXTENSION_JPG,
								EXTENSION_PNG, EXTENSION_BMP));
		fChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}
	
	public String getPathFile() throws Exception{
		int selection = fChooser.showOpenDialog(this);
		if (selection == JFileChooser.APPROVE_OPTION) {
			return fChooser.getSelectedFile().getPath();
		}
		throw new Exception(CANCEL_SELECTION);
	}
	
	public String getFileName() throws Exception{
		int selection = fChooser.showSaveDialog(this);
		if (selection == JFileChooser.APPROVE_OPTION) {
			return fChooser.getSelectedFile().getName();
		}
		throw new Exception(CANCEL_SELECTION);
	}
}