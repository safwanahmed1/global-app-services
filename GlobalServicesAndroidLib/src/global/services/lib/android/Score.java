package global.services.lib.android;

public class Score {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    private Long id;
    private String userId_;
    private Long appId_;
    private String subBoard_;
    private String player_;
    private int score_;
    private String comment_;
    private String location_;
    private long during_;
    private long date_;
    private String avatar_;
    
    public Score(){}
    
    public Score(String userID, long appID) {
        //this.userID = userID;
    	this.userId_ = userID;
    	this.appId_ = appID;
    }
	public String getComment() {
		return comment_;
	}
	public void setComment(String comment) {
		this.comment_ = comment;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserID() {
		return userId_;
	}
	public void setUserID(String userId) {
		this.userId_ = userId;
	}
	
	public long getGameID() {
		return appId_;
	}
	public void setGameID(long gameID) {
		this.appId_ = gameID;
	}
	public String getSubBoard() {
		return subBoard_;
	}
	public void setSubBoard(String subBoard) {
		this.subBoard_ = subBoard;
	}
	public String getPlayer() {
		return player_;
	}
	public void setPlayer(String player) {
		this.player_ = player;
	}
	public int getHighScore() {
		return score_;
	}
	public void setHighScore(int highScore) {
		this.score_ = highScore;
	}
	public String getLocation() {
		return location_;
	}
	public void setLocation(String location) {
		this.location_ = location;
	}
	public long getDuring() {
		return during_;
	}
	public void setDuring(long during) {
		this.during_ = during;
	}
	public long getDate() {
		return date_;
	}
	public void setDate(long date) {
		this.date_ = date;
	}
	public String getAvatar() {
		return avatar_;
	}
	public void setAvatar(String avatar) {
		this.avatar_ = avatar;
	}
    
    
    

}
