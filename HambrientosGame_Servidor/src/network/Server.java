package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	
	private static final int SLEEP = 600;
	private static final int PORT = 3005;
	private static final String NEW_CONNECTION = " New connection!";
	private static final String CHARGING_CARACTERS = "...";
	private static final String SERVER_ON = "Server on ";
	private ServerSocket server;
	private ArrayList<Connection> connections;
	private IPresenter iPresenter;
	
	public Server(IPresenter iPresenter) throws IOException {
		this.iPresenter = iPresenter;
		connections = new ArrayList<Connection>();
		server = new ServerSocket(PORT);
		Logger.getGlobal().log(Level.INFO, SERVER_ON + PORT + CHARGING_CARACTERS);
	}
	
	public void acceptConnections() throws IOException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(!server.isClosed()) {
					try {
						while(connections.size() <= 4) {
							connections.add(new Connection(server.accept(), iPresenter));
							Logger.getGlobal().log(Level.INFO, LocalTime.now() + NEW_CONNECTION);
						}
					} catch (IOException e) {
						Logger.getGlobal().log(Level.SEVERE, e.toString());
					}
				}
			}
		}).start();
	}
	
	public void manageProxy() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					for (Connection connection : connections) {
						connection.manageRequest();
					}
					try {
						Thread.sleep(SLEEP);
					} catch (InterruptedException e) {
						Logger.getGlobal().log(Level.SEVERE, e.toString());
					}
				}
			}
		}).start();
	}

	public ArrayList<Connection> getConnections() {
		return connections;
	}
}