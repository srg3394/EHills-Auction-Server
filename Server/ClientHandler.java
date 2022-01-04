/**  EE422C Final Project submission by
 *     Sathvik Gujja
 *     srg3394
 *     76000
 *     Summer 2021
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Observable;

import com.google.gson.Gson;

class ClientHandler implements Runnable, Observer {

  private Server server;
  private Socket clientSocket;
  private BufferedReader fromClient;
  private PrintWriter toClient;
  
  

  protected ClientHandler(Server server, Socket clientSocket) {
    this.server = server;
    this.clientSocket = clientSocket;
    try {
      fromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
      toClient = new PrintWriter(this.clientSocket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void sendToClient(Message message) {
	
		Gson gson = new Gson();
	  
	  	System.out.println("Sending to client: " + message);
	  	System.out.println();
	  	toClient.println(gson.toJson(message));
	  	toClient.flush();
	 
  }

  @Override
  public void run() {
    String input;
    Gson gson = new Gson();
    try {
      while ((input = fromClient.readLine()) != null) {
        Message message = gson.fromJson(input, Message.class);
        System.out.println("From client: " + message);
        Message sendingTo;
        sendingTo = server.processRequest(message);
        
        try {
        	
        	if(message.getType().equals("Successful_Login") || message.getType().equals("Failed_Login")) {
            	sendToClient(sendingTo);
        	}       	
        	else if(message.getType().equals("Valid_Bid")) {
        		server.distributeMessage(sendingTo);
        		
        	}
        	else if(message.getType().equals("Bid")) {
        		server.distributeMessage(sendingTo);
        	}
        	else if(message.getType().equals("Valid_Buy")) {
        		server.distributeMessage(sendingTo);
        	}
        	
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    this.sendToClient((Message) arg);
  }
}