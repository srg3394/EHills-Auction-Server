/**  EE422C Final Project submission by
 *     Sathvik Gujja
 *     srg3394
 *     76000
 *     Summer 2021
 */

import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.net.URL;

import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import com.google.gson.Gson;

public class Client extends Application {

	ClientData client = null;
	Socket socket = null;
	
	ObjectInputStream dataIn = null;
	ObjectOutputStream dataOut = null;
	
	static ArrayList<Item> items = new ArrayList<Item>();
	static ArrayList<Item> soldItems = new ArrayList<Item>();
	
	int guestNumber = 0;
	
	public static BufferedReader fromServer;
	public static PrintWriter toServer;
	
	static Gson gson = new Gson();
	
	ClientReader reader;
	
	static boolean loggedIn = false;
	static Scene auctionScene;
	
	MediaPlayer startup = new MediaPlayer(new Media(new File("Startup.mp3").toURI().toString()));
	
	volatile static int row = 10;
	final AtomicInteger count = new AtomicInteger(10);
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		 startup.play(); 
		
		 socket=new Socket("localhost",1234);
		 System.out.println("Connecting to... " + socket);
		 
		 
		 fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		 toServer = new PrintWriter(socket.getOutputStream());
		 
		 client = new ClientData(fromServer, toServer, socket, this);
		 
		 reader = new ClientReader();
		 
		 reader.start();
		 
		 System.out.println("connected to server");
		 System.out.println();
		 
			GridPane grid = new GridPane();
			grid.setPadding(new Insets(10,10,10,10));
			grid.setHgap(10);
			grid.setVgap(8);
			
			//Username
			Label username = new Label("Username:");
			GridPane.setConstraints(username, 0, 0);
			
			//Username Input
			TextField usernameInput = new TextField();
			usernameInput.setMaxWidth(100);
			GridPane.setConstraints(usernameInput, 1, 0);
			
			//Password
			Label password = new Label("Password:");
			GridPane.setConstraints(password, 0, 1);
			
			//Password Input
			TextField passwordInput = new TextField();
			passwordInput.setPromptText("password");
			passwordInput.setMaxWidth(100);
			GridPane.setConstraints(passwordInput, 1, 1);
			
			//Login Button
			Button loginButton = new Button("Login to EHills!");
			GridPane.setConstraints(loginButton, 1, 2);
			
			//Guest Login Button
			Button guestLoginButton = new Button("Login as a guest");
			GridPane.setConstraints(guestLoginButton, 1, 3);
			
			//Exit Button
			Button exitButton = new Button("Exit");
			exitButton.setMinWidth(50);
			GridPane.setConstraints(exitButton, 2, 16);
			
			Image hillsImage = new Image("hills.png");
			ImageView hillsView = new ImageView(hillsImage);
			hillsView.setFitHeight(50);
			hillsView.setFitWidth(75);
			GridPane.setConstraints(hillsView, 1, 8);
			
			
			grid.setStyle("-fx-background-color: #D3D3D3;");
			grid.getChildren().addAll(username, usernameInput, password, passwordInput, loginButton, guestLoginButton, exitButton);      
			Scene loginScene = new Scene(grid, 300, 300);
			
			GridPane grid2 = new GridPane();
			
			Button auctionViewButton = new Button("View Auction Listings");
			GridPane.setConstraints(auctionViewButton, 1, 1);
			
			Button historyButton = new Button("View Item Bid History");
			GridPane.setConstraints(historyButton, 1, 2);
			
			Button exitButton2 = new Button("Exit Auction/Logout");
			GridPane.setConstraints(exitButton2, 1, 5);
			
			
			
			grid2.getChildren().addAll(auctionViewButton, exitButton2, historyButton);
			Scene homeScene = new Scene(grid2, 200, 100);
			
			
			primaryStage.setTitle("EHills Auction Server");
			primaryStage.setResizable(false);
						
			primaryStage.setScene(loginScene);
			primaryStage.show();
			
		    
			
			loginButton.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {	            	            		            	
	            	
	            	Message m = new Message("Login", usernameInput.getText(), passwordInput.getText());
	            	sendToServer(m);
	            	
	            	try
	            	{
	            	    Thread.sleep(2000);
	            	}
	            	catch(InterruptedException ex)
	            	{
	            	    Thread.currentThread().interrupt();
	            	}
	            	
	            	if(loggedIn) {
	            		client.setCustUsername(usernameInput.getText());
		            	client.setCustPassword(passwordInput.getText());
		            	
		            	primaryStage.setScene(homeScene);
		            	primaryStage.setTitle("Welcome to EHills Auction Server");
		            	
		            	initializeItems(items);
	            	}
	            	
	            	
	            	
	            }
	        });
			
			guestLoginButton.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {	            	
	            	
	            	try {
	            		client.setCustUsername(usernameInput.getText());
	            	}
	            	catch(Exception e) {
	            		
	            	}
	            	client.setCustPassword("NA");
	            	
	            	primaryStage.setScene(homeScene);
	            	primaryStage.setTitle("Welcome! Your username is " + client.getCustUsername());
	            	
	            	
	            	
	            	initializeItems(items);
	            }
	        });
			
			exitButton.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {
	            	System.exit(0);	
	            }
	        });
			
			
			auctionViewButton.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {           
	            	
	            	
	            	 TabPane tabPane = new TabPane();

	            	 for(Item currItem : items) {
	            		 
	            		 currItem.startTimer();
	            		 Tab currTab = new Tab(currItem.getName());
	            		 initItemTab(currTab, currItem, homeScene, primaryStage, client);
	            		 
	            		 currTab.setClosable(false);
	            		 
	            		 tabPane.getTabs().add(currTab);
	            	 }
	            	 
	            	 

	                 VBox vBox = new VBox(tabPane);
	                 Scene auctionListingsScene = new Scene(vBox, 700, 600);	                 
	                 auctionScene = auctionListingsScene;
	                 primaryStage.setScene(auctionListingsScene);
	                 primaryStage.setTitle("Auction Listings");
	                 
	                 
	              
	            }
	        });
			
			historyButton.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {
	            	TabPane tabPane = new TabPane();
	            	
	            	for(Item currItem : items) {
	            		Tab currTab = new Tab(currItem.getName());
	            		GridPane histGrid = new GridPane();
	            		histGrid.setPadding(new Insets(10,10,10,10));
	            		histGrid.setHgap(10);
	            		histGrid.setVgap(8);
	            		
	            		Button returnFromHistory = new Button("Return to Auction");
	            		GridPane.setConstraints(returnFromHistory, 0, 20);
	            		int row = 0;
	            		for (String key: currItem.getBidHistory().keySet()) {
	                        Label bidHistory = new Label(currItem.getBidHistory().get(key) + " has bid $" + key + "!");	                        
	                        GridPane.setConstraints(bidHistory, 0, row);
	                        row++;
	                        histGrid.getChildren().add(bidHistory);
	                    }
	            		
	            		
	            		//Label bidHistory = new Label(currItem.historyToString());
	            		
	            		
	            		histGrid.getChildren().add(returnFromHistory);
	            		currTab.setClosable(false); 
	            		tabPane.getTabs().add(currTab);
	            		currTab.setContent(histGrid);
	            		
	            		returnFromHistory.setOnAction(new EventHandler<ActionEvent>() {
	   	       			 
		                    @Override
		                    public void handle(ActionEvent event) {
		                    	primaryStage.setScene(auctionScene);	
		                    }
		                });
	            	}
	            	
	            	
	            	
	            	VBox vBox = new VBox(tabPane);
	                Scene viewHistoryScene = new Scene(vBox, 500, 500);
	                
	                 

	                 primaryStage.setScene(viewHistoryScene);
	                 primaryStage.setTitle("View Bid Histories");
	            }
	        });
			
			
			exitButton2.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {
	            	System.exit(0);	
	            }
	        });
			
		
	}
	
	
	
	public static void initializeItems(ArrayList<Item> items) {
		// name, description, minimumBid, buyNowPrice
		/*Item basketball = new Item("Basketball", "A Standard 29.5 inch NBA regulation basketball.", 5, 50, 20);
		Item lamp = new Item("Lamp", "A vibrant lamp used to illuminate interiors of various rooms.", 5, 50, 20);
		Item violin = new Item("Violin", "A beautiful sounding violin to enrich the ears of plenty.", 10, 100, 20);
		Item sneakers = new Item("Sneakers", "A nice pair of sneakers for athletic purposes", 2, 20, 20);
		Item gameboy = new Item("Gameboy", "An antique video game system used to play retro games.", 15, 100, 20);
		
		items.add(basketball);
		items.add(lamp);
		items.add(violin);
		items.add(sneakers);
		items.add(gameboy);
		*/
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
			
			
			
			
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void initItemTab(Tab currTab, Item currItem, Scene homeScene, Stage primaryStage, ClientData client) {
		
		GridPane itemGrid = new GridPane();
		itemGrid.setPadding(new Insets(10,10,10,10));
		itemGrid.setHgap(10);
		itemGrid.setVgap(8);
		
        Label itemName = new Label("Name: " + currItem.getName());
        GridPane.setConstraints(itemName, 0, 1);
        
        Label itemDesc = new Label("Description: " + currItem.getDescription());
        itemDesc.setWrapText(true);
        GridPane.setConstraints(itemDesc, 0, 2);
        
        Label itemMinBid = new Label("Minimum Bid: ");
        GridPane.setConstraints(itemMinBid, 0, 3);
        TextField minBidField = new TextField(String.valueOf(currItem.getMinimumBid()));
        minBidField.setEditable(false);
        GridPane.setConstraints(minBidField, 1, 3);
        
        Label itemCurrBid = new Label("Current Bid: ");
        GridPane.setConstraints(itemCurrBid, 0, 4);
        TextField currBidField = new TextField(String.valueOf(currItem.getCurrentBid()));
        //TextField currBidField = new TextField("-----");
        currBidField.setEditable(false);
        GridPane.setConstraints(currBidField, 1, 4);
        
        Label itemTopBidder = new Label("Top Bidder: ");
        GridPane.setConstraints(itemTopBidder, 0, 5);
        TextField topBidderField = new TextField(String.valueOf(currItem.getTopBidder()));
        topBidderField.setEditable(false);
        GridPane.setConstraints(topBidderField, 1, 5);
        
        Button bidButton = new Button("Bid");
        GridPane.setConstraints(bidButton, 0, 7);
        TextField bidField = new TextField();
        GridPane.setConstraints(bidField, 1, 7);
        
      //  Button buyNowButton = new Button("Buy Now");
      //  GridPane.setConstraints(buyNowButton, 0, 8);
      //  TextField buyNowField = new TextField(String.valueOf(currItem.getBuyNowPrice()));
      //  GridPane.setConstraints(buyNowField, 1, 8);
        
        Label boughtLabel = new Label("");
		GridPane.setConstraints(boughtLabel, 0, 11);
        
        Label timeLabel = new Label("Time Remaining: ");
        GridPane.setConstraints(timeLabel, 0, 9);
        TextField timeLabelField = new TextField();
        timeLabelField.setText(String.valueOf(currItem.getMaxTime()));
        timeLabelField.setEditable(false);
        
        new AnimationTimer() {

			@Override
			public void handle(long now) {
				if(currItem.isSold() == false) {
					timeLabelField.setText(String.valueOf(currItem.getCurrTime()));
					if(currItem.getCurrTime() == 0) {
						
						if(currItem.getTopBidder().equals("None")) {
							primaryStage.setTitle("Nobody has bought " + currItem.getName() + "." );
							boughtLabel.setText(currItem.getName() + " has been sold to nobody.");
							boughtLabel.setStyle("-fx-font-weight: bold");
						}
						else {
							primaryStage.setTitle(currItem.getTopBidder() + " has bought " + currItem.getName() + " for $" + String.valueOf(currItem.getCurrentBid()) + "!");
							boughtLabel.setText(currItem.getName() + " has been sold to " + currItem.getTopBidder() + "!");
							boughtLabel.setStyle("-fx-font-weight: bold");
						}

						
						
						bidButton.setDisable(true);
						currBidField.setText(String.valueOf(currItem.getCurrentBid()));							
						currItem.setSold(true);
						currTab.setContent(itemGrid);
					}
				}	
			}   	       	
        }.start();
        
        GridPane.setConstraints(timeLabelField, 1, 9);
        
		
		Button returnFromAuctionButton = new Button("Return");
		GridPane.setConstraints(returnFromAuctionButton, 0, 12);
		
		Label bidCheckLabel = new Label("");
		GridPane.setConstraints(bidCheckLabel, 0, 10);
		
		

		
		itemGrid.getChildren().addAll(returnFromAuctionButton, itemName, itemDesc, itemMinBid, minBidField, itemCurrBid, itemTopBidder, currBidField, topBidderField, bidButton, bidField, timeLabel, timeLabelField, bidCheckLabel, boughtLabel);
		currTab.setContent(itemGrid);
		
		bidButton.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
            	String bidCheck = processBid(currItem, bidField.getText(), client);
            	bidCheckLabel.setText(bidCheck);
            	
            	Message m = new Message("Bid_Update", bidCheck, currItem);
            	sendToServer(m);           	           	
            	
            	Thread bidUpdate = new Thread(()-> {
            		String input;
                    try {
                      while ((input = fromServer.readLine()) != null) {
                    	  synchronized(fromServer) {               		  
                              Gson gson = new Gson();
                              Message message = gson.fromJson(input, Message.class);
                              
                              System.out.println("Receiving message from server: " + message);
                              if(message.getType().equals("Valid_Bid")) {
                            	   
                            	  Platform.runLater(new Runnable() {

   								
    								public void run() {
    									updateItem(message); 
    									
    									System.out.println(message.item.getCurrentBid());
    									topBidderField.setText(message.item.getTopBidder());
    									currBidField.setText(String.valueOf(message.item.getCurrentBid()));
    									timeLabelField.setText(String.valueOf(message.item.getMaxTime()));   								
    									  																		
    									//updateItemGUI(currTab, currItem, homeScene, primaryStage, client, itemGrid, topBidderField, currBidField);
    									//updateItemGUI(message, topBidderField, currBidField, primaryStage, currTab, itemGrid);
    									
    								}
                            	    
                            	  });
                            	  
                              }
                              
                    	  }
                      }
                	
                    }
                    catch(Exception e) {
                    	System.out.println("Update error!");
                    }
            	});          	           	
            	
            	bidUpdate.start();
            	
            	
            	
            }     
            
        });
		
	/*	buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
            	String buyNowCheck = processBuyNow(currItem, client, primaryStage);
            	System.out.println("LMAO");
            	            	
            	Message m = new Message("Buy_Now", buyNowCheck, currItem, client.getCustUsername());
            	sendToServer(m);
                
            	try
            	{
            	    Thread.sleep(2000);
            	}
            	catch(InterruptedException ex)
            	{
            	    Thread.currentThread().interrupt();
            	}
            	
            	if(currItem.isSold()){
            		bidButton.setDisable(true);
            		currItem.setCurrentBid(currItem.getBuyNowPrice());
            	}
            	
            	
            }							  																		   							  									
    				
        }); */
		
		returnFromAuctionButton.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
            	primaryStage.setScene(homeScene);	
            }
        });
		
	}
	
	public synchronized static String processBid(Item currItem, String bidText, ClientData client) {
		Double bid;
		try {
			bid = Double.parseDouble(bidText);
		}
		catch(Exception e ) {
			return "Failure: Invalid Bid";
		}
		if(currItem.isSold()){
			return "Failure: The item is already sold!";
		}
		
		if(currItem.getCurrentBid() > bid) {
			return "Failure: The current bid is greater than your bid";
		}
		
		if(bid >= currItem.getBuyNowPrice()) {
			return "Failure: Bid must not be greater than [Buy Now] price";
		}
		
		try {
			
			currItem.setCurrentBid(bid);
			currItem.setTopBidder(client.getCustUsername());
			currItem.setCurrTime(currItem.getMaxTime());
			currItem.getBidHistory().put(String.valueOf(currItem.getCurrentBid()), currItem.getTopBidder());
			
			for(Item item : items) {
				if(currItem.getName().equals(item.getName())) {
					item = currItem;
				}
			}
			
			Message m = new Message("Bid", currItem, client.getCustUsername(), client.getCustPassword());
			sendToServer(m);
			return "Successful Bid Placement";
		}
		catch(NumberFormatException e) {
			return "Bid must be a valid number";
		}
		
	}
	
	public synchronized static String processBuyNow(Item currItem, ClientData client, Stage primaryStage) {
		if(currItem.isSold()) {
			return "The item has already been sold";
		}
		
		if(currItem.getTopBidder().equals("None")) {
			currItem.setTopBidder(client.getCustUsername());
		}

		primaryStage.setTitle(currItem.getTopBidder() + " has bought " + currItem.getName() + " for $" + String.valueOf(currItem.getBuyNowPrice()) + "!");
		
		currItem.setSold(true);
		currItem.setCurrentBid(currItem.getBuyNowPrice());
		currItem.setTopBidder(client.getCustUsername());
		currItem.getBidHistory().put(String.valueOf(currItem.getBuyNowPrice()), currItem.getTopBidder());
		
		Message m = new Message("Buy", currItem, client.getCustUsername(), client.getCustPassword());
		sendToServer(m);
		
		return "Success! You have bought the " + currItem.getName();
	}
	
	public synchronized static void processMessage(Message message) {
		if(message.getType().equals("Successful_Login")) {
			loggedIn = true;
		}
		else if(message.getType().equals("Failed_Login")){
			
		}
		else if(message.getType().equals("Bid")) {
			updateItem(message);
		}
		else if(message.getType().equals("Valid_Buy")) {
			
			updateBuy(message);
		}		   
		
	}
	
	public synchronized static void updateBuy(Message message) {
    	for(Item currItem : items) {
    		if(currItem.getName().equals(message.item.getName())) {
    			
    			currItem.setSold(true);
    			message.item.setSold(true);
    			
    		}
    	}
    }
	
	public static synchronized void updateItem(Message message) {
		for(Item currItem : items) 
			if(currItem.getName().equals(message.itemName)){
				currItem.setTopBidder(message.username);
				currItem.setCurrentBid(message.bid);
				currItem.setCurrTime(message.currTime);
				currItem.setSold(message.isSold);
			}
		
	}
	
	public static synchronized void updateItemGUI(Tab currTab, Item currItem, Scene homeScene, Stage primaryStage, ClientData client, GridPane itemGrid, TextField topBidderField, TextField currBidField) {
		
			topBidderField.setText(currItem.getTopBidder());
			currBidField.setText(String.valueOf(currItem.getCurrentBid()));
			primaryStage.setTitle(currItem.getTopBidder() + " bids $" + String.valueOf(currItem.getCurrentBid()) + " on " + currItem.getName() + ".");
			currTab.setContent(itemGrid);
		
	}
	
	public static void updateItemGUI(Message message, TextField topBidderField, TextField currBidField, Stage primaryStage, Tab currTab, GridPane itemGrid) {
		for(Item currItem : items) 
			if(currItem.getName().equals(message.itemName)){
				topBidderField.setText(currItem.getTopBidder());
				currBidField.setText(String.valueOf(currItem.getCurrentBid()));
				primaryStage.setTitle(currItem.getTopBidder() + " bids $" + String.valueOf(currItem.getCurrentBid()) + " on " + currItem.getName() + ".");
				currTab.setContent(itemGrid);
				
			}
	}
	
	public static void sendToServer(Message message) {
	    System.out.println("Sending to server: " + message);
	    System.out.println();
	    toServer.println(gson.toJson(message));
	    Client.toServer.flush();
	}
	
	class ClientReader extends Thread{
		
		public ClientReader() {
			
		}
		
		@Override
		public void run() {
			String incString;
			
			try {
				while(true) {
					if((incString = fromServer.readLine()) != null) {
						Message message = gson.fromJson(incString, Message.class);
						if(!message.type.equals("Valid_Bid")) {
							System.out.println("Receiving message from server: " + message);
							System.out.println();
							processMessage(message);
						}	
					}
				}
			
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
