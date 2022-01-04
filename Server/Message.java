/**  EE422C Final Project submission by
 *     Sathvik Gujja
 *     srg3394
 *     76000
 *     Summer 2021
 */
import java.util.ArrayList;

class Message {
  /*
  String type;
  String input;
  int number;

  protected Message() {
    this.type = "";
    this.input = "";
    this.number = 0;
    System.out.println("server-side message created");
  }

  protected Message(String type, String input, int number) {
    this.type = type;
    this.input = input;
    this.number = number;
    System.out.println("server-side message created");
  }
  
  */
	
	//String test1 = "Test1 Value";
	//String test2 = "Test 2 Value";
    
	public String username;
	public String password;
	
	public Item item;
	public ArrayList<Item> items;
	public String type;
	public String itemName;
	public double bid;
	public int currTime;
	public boolean isSold;
	public String bidCheck;
	
	
	public Message(String type, String username, String password) {
		
		this.type = type;
		this.username = username;
		this.password = password;
	}
	
	public Message(String type, Item item, String username, String password) {
		this.type = type;
		this.item = item;
		this.itemName = item.getName();
		this.bid = item.getCurrentBid();
		this.currTime = item.getCurrTime();
		this.isSold = item.isSold();
		this.username = username;
		this.password = password;
	}
	
	public Message(String type, ArrayList<Item> items) {
		this.type = type;
		this.items = items;
	}
	
	public Message(String type, String bidCheck, Item item) {
		this.type = type;
		this.bidCheck = bidCheck;
		this.item = item;
	}
	
	public Message(String type, String buyNowCheck, Item item, String username) {
		this.type = type;
		this.bidCheck = buyNowCheck;
		this.item = item;
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public String getBidCheck() {
		return bidCheck;
	}

	public void setBidCheck(String bidCheck) {
		this.bidCheck = bidCheck;
	}

	@Override
	public String toString() {
		return "Message [username=" + username + ", password=" + password + ", item=" + item + ", items=" + items
				+ ", type=" + type + ", itemName=" + itemName + ", bid=" + bid + ", currTime=" + currTime + ", isSold="
				+ isSold + ", bidCheck=" + bidCheck + "]";
	}

	

	
	
	
	
  
}

