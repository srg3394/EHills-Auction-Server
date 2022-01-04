/**  EE422C Final Project submission by
 *     Sathvik Gujja
 *     srg3394
 *     76000
 *     Summer 2021
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;

import com.google.gson.Gson;

class Server extends Observable {
	
	static ArrayList<Item> items = new ArrayList<Item>();
	static Item item;
	
	static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	
	Object lock = new Object();
	
	static HashMap<String, String> logins = new HashMap<String, String>();

  public static void main(String[] args) {  	
	    importItems();	    
	  	importLogins();
	  	new Server().runServer();
	  	
  }

  private void runServer() {
    try {
      setUpNetworking();      
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  private void setUpNetworking() throws Exception {
    @SuppressWarnings("resource")
    ServerSocket serverSock = new ServerSocket(1234);
    while (true) {
      Socket clientSocket = serverSock.accept();
      System.out.println("Connecting to... " + clientSocket);
      
      System.out.println("Connected to Client");
      System.out.println();
      
      ClientHandler handler = new ClientHandler(this, clientSocket);
      this.addObserver(handler);
      synchronized(lock) {
    	  clients.add(handler);
      }	  
      Thread t = new Thread(handler);
      t.start();
    }
  }
  
  private static void importItems() {
	  try {
			Scanner input = new Scanner(new File("Items.txt"));
			input.useDelimiter("-");
			
			while(input.hasNext()) {
				String name = input.next();
				String description = input.next();			
				
				int minimumBid = input.nextInt();
				int buyNowPrice = input.nextInt();
							
				int time = input.nextInt();
				input.nextLine();
					
				Double d1 = Double.valueOf(minimumBid);
				Double d2 = Double.valueOf(buyNowPrice);
				
				Item currItem = new Item(name, description, d1, d2, time);
				items.add(currItem);
				
			}
			
			for(Item item : items) {
				System.out.println(item);
			}
			
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
  }
  
  private static void importLogins() {
	try {
		Scanner input = new Scanner(new File("Logins.txt"));
		input.useDelimiter("-");
		
		while(input.hasNext())
		{
			String username = input.next();
			String password = input.next();
			
			input.nextLine();
			
			logins.put(username, password);
		}
		
		System.out.println(logins.keySet());
		System.out.println(logins.values());
		
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	  
  }
  
  void distributeMessage(Message message) {
	    ArrayList<ClientHandler> clientsCopy;
	    synchronized (lock) {
	        clientsCopy = new ArrayList<>(clients);
	    }
	    for (ClientHandler client : clientsCopy) {
	        client.sendToClient(message);
	    }
	}

  protected Message processRequest(Message message) {
  //  String output = "Error";
   // Gson gson = new Gson();
  //  Message message = gson.fromJson(input, Message.class);
    
    	//System.out.println(message.username);
    	//Message m = new Message();
    	//m.password = "Server sends data";
    	//gson.toJson(m);
    	
    //	this.setChanged();
    //	this.notifyObservers(output);
    
	try {
		if(message.getType().equals("Login")) {
			if(logins.containsKey(message.getUsername()) && (logins.get(message.getUsername()).equals(message.getPassword()))) {
				message.setType("Successful_Login");
				System.out.println("Valid Login Credentials");
				return message;
			}
			else {
				message.setType("Failed_Login");
				System.out.println("Invalid Login Credentials");
				return message;
			}
		}
		else if(message.getType().equals("Bid_Update")) {
			if(message.getBidCheck().equals("Successful Bid Placement")) {
				message.setType("Valid_Bid");
				
				for(Item currItem : items) 
					if(currItem.getName().equals(message.itemName)){
						currItem.setTopBidder(message.username);
						currItem.setCurrentBid(message.bid);
						currItem.setCurrTime(message.currTime);
						currItem.setSold(message.isSold);
						
						message.item = currItem;
						
						
						System.out.println(items);
					}
				
				
			}
			else {
				message.setType("Invalid_Bid");
			}
			return message;
		}
		else if(message.getType().equals("Buy_Now")) {
			if(message.getBidCheck().contains("Success!")) { // bidCheck is buyCheck
				message.setType("Valid_Buy");
			}
			
			return message;
		}
		else if(message.getType().equals("Bid")) {
			return message;
		}
		
	}
    catch (Exception e) {
      e.printStackTrace();
    }
	
	return message;
  }
  

}