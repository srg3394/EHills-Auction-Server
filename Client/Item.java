/**  EE422C Final Project submission by
 *     Sathvik Gujja
 *     srg3394
 *     76000
 *     Summer 2021
 */
import java.io.Serializable;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class Item implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private double minimumBid;
	private double currentBid;
	private String topBidder;
	private boolean isSold;
	private double buyNowPrice;
	private Integer maxTime;
	private Integer currTime;
	
	private TreeMap<String, String> bidHistory;
	
	public Item(String name, String description, double minimumBid, double buyNowPrice, int time) {
		
		this.name = name;
		this.description = description;
		this.minimumBid = minimumBid;
		this.currentBid = minimumBid;
		this.topBidder = "None";
		this.isSold = false;
		this.buyNowPrice = buyNowPrice;
		this.maxTime = time;
		this.currTime = time;
		this.bidHistory = new TreeMap<String, String>();
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", description=" + description + ", minimumBid=" + minimumBid + ", currentBid="
				+ currentBid + ", topBidder=" + topBidder + ", isSold=" + isSold + ", buyNowPrice=" + buyNowPrice +  ", maxTime=" + maxTime + ", currTime=" + currTime + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getMinimumBid() {
		return minimumBid;
	}

	public void setMinimumBid(double minimumBid) {
		this.minimumBid = minimumBid;
	}

	public double getCurrentBid() {
		return currentBid;
	}

	public void setCurrentBid(double currentBid) {
		this.currentBid = currentBid;
	}

	public String getTopBidder() {
		return topBidder;
	}

	public void setTopBidder(String topBidder) {
		this.topBidder = topBidder;
	}

	public boolean isSold() {
		return isSold;
	}

	public void setSold(boolean isSold) {
		this.isSold = isSold;
	}

	public double getBuyNowPrice() {
		return buyNowPrice;
	}

	public void setBuyNowPrice(double buyNowPrice) {
		this.buyNowPrice = buyNowPrice;
	}
	
	public Integer getMaxTime() {
		return maxTime;
	}
	
	public void setMaxTime(Integer maxTime) {
		this.maxTime = maxTime;
	}

	public Integer getCurrTime() {
		return currTime;
	}

	public void setCurrTime(Integer currTime) {
		this.currTime = currTime;
	}
	
	
	public TreeMap<String, String> getBidHistory() {
		return bidHistory;
	}

	public void setBidHistory(TreeMap<String, String> bidHistory) {
		this.bidHistory = bidHistory;
	}

	public void startTimer() {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask(){
			
			@Override
			public void run() {
				if(currTime > 0) {
					currTime--;
				}
				
			}
		}, 1000l, 1000l);	
		
	}
	
	public String historyToString() {		
		return bidHistory.toString();
	}
	
	
	
	
}
