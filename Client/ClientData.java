/**  EE422C Final Project submission by
 *     Sathvik Gujja
 *     srg3394
 *     76000
 *     Summer 2021
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.*;

import com.google.gson.Gson;

public class ClientData{
	//private final ObjectInputStream dataIn; 
	//private final ObjectOutputStream dataOut;
	private final Socket socket;
	
	private String custUsername;
	private String custPassword;
	private String guestUsername;
	private String guestPassword;
	
	public boolean loggedIn;

	
	public static BufferedReader fromServer;
	public static PrintWriter toServer;
	
	private final Client clientFX;
	
	
	
	
	public ClientData(BufferedReader fromServer, PrintWriter toServer, Socket socket, Client clientFX ) {
		this.fromServer = fromServer;
		this.toServer = toServer;
		this.socket = socket;
		this.clientFX = clientFX;
		
		//Thread incThread = new Thread(new ClientReader(dataIn, this));
		//incThread.start();
	}

	public String getCustUsername() {
		return custUsername;
	}

	public void setCustUsername(String username) {
		this.custUsername = username;
	}

	public String getCustPassword() {
		return custPassword;
	}

	public void setCustPassword(String password) {
		this.custPassword = password;
	}

	public String getGuestUsername() {
		return guestUsername;
	}

	public void setGuestUsername(String guestUsername) {
		this.guestUsername = guestUsername;
	}

	public String getGuestPassword() {
		return guestPassword;
	}

	public void setGuestPassword(String guestPassword) {
		this.guestPassword = guestPassword;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	
}

