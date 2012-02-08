package global.services.xmpp;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Question {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent(defaultFetchGroup = "true")
	private Text question;

	@Persistent
	private String asker;

	@Persistent
	private Date asked;

	@Persistent(defaultFetchGroup = "true")
	private Text answer;

	@Persistent
	private String answerer;

	@Persistent
	private Date answered;

	@Persistent
	private Boolean suspended;

	public Key getKey() {
		return key;
	}

	// ...
}