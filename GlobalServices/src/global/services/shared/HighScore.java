package global.services.shared;
import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.view.client.ProvidesKey;
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class HighScore implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final ProvidesKey<HighScore> KEY_PROVIDER = new ProvidesKey<HighScore>() {
	      public Object getKey(HighScore score) {
	        return score == null ? null : score.getId();
	      }
	    };
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent
    private String userId_;
    @Persistent
    private Long appId_;
    @Persistent
    private String subBoard_;
    @Persistent
    private String player_;
    @Persistent
    private int score_;
    @Persistent
    private String comment_;
    @Persistent
    private String location_;
    @Persistent
    private long during_;
    @Persistent
    private long date_;
    @Persistent
    private String avatar_;
    
    public HighScore(){}
    
    public HighScore(String userID, long appID) {
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
