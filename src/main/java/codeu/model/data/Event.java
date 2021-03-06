
package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.time.ZoneId; 

import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;

/** 
 *  This class abstracts any object so it can be a User, Conversation, or Message. 
 *  Made so a list can  easily hold and sort all types of objects at once.
 */
public class Event{

	/* Denotes the type of event this is. Conversation, Message, or User. */
	public enum EventType{
    CONVERSATION, MESSAGE, USER;
  }
  private final EventType eventType;

	/* All the fields needed from the conversation class. */
	private final String titleOfConversation;
	private final UUID authorIdForConversation;
	private final Instant conversationCreationTime;
  private final UUID conversationId;

	/* All the fields needed from the message class. */
	private final Instant messageCreationTime;
  private final UUID authorIdForMessage;
  private final String conversationTitleOfMessage;
  private final String messageContent;
  private final UUID messageId;

  /* All the fields needed from the user class. */
  private final Instant userCreationTime;
  private final String nameOfUser;
  private final UUID userId;

  /* Needed to get the user given the ID from a conversation object. */
  private final UserStore userStore;
  /* Need to get the conversation given the ID from a message object. */
  private final ConversationStore conversationStore;
  
  /**
   * Constructs a new event using a Conversation object.
   *
   * @param conversation an object of type Conversation
   */
	public Event(Conversation conversation) {
    eventType = EventType.CONVERSATION;

    userStore = UserStore.getInstance();
    conversationStore = ConversationStore.getInstance();

    titleOfConversation = conversation.getTitle();
    authorIdForConversation = conversation.getOwnerId();
    conversationCreationTime = conversation.getCreationTime();
    conversationId = conversation.getId();
    messageCreationTime = null;
    authorIdForMessage = null;
    conversationTitleOfMessage = null;
    messageContent = null;
    messageId = null;

    userCreationTime = null;
    nameOfUser = null;
    userId = null;

	}

  /**
   * Constructs a new event using a Message object.
   *
   * @param message an object of type Message
   */
	public Event(Message message) {
    eventType = EventType.MESSAGE;

    userStore = UserStore.getInstance();
    conversationStore = ConversationStore.getInstance();

    titleOfConversation = null;
    authorIdForConversation = null;
    conversationCreationTime = null;
    conversationId = null;

    messageCreationTime = message.getCreationTime();
    authorIdForMessage = message.getAuthorId();

    Boolean error = false;
    try {
      conversationStore.getConversationWithId(message
        .getConversationId()).getTitle();
    }
    catch (NullPointerException e) {
      System.err.println("_______________________________________________"  +
                         "\n CAUGHT EXCEPTION_____________________________" + 
                         " This message does not have a conversation"    + 
                         " it is associated with. If you are unit test," + 
                         " consider using the other constructor that takes" + 
                         " a message and a placeholder for the conversationTitleOfMessage," + 
                         " otherwise consider checking the admin page or"   + 
                         " appegine for to see if the conversation exists." + 
                         " _______________________________________________" +
                         "\n_______________________________________________");
      error = true;
    }
    if(error) {
      conversationTitleOfMessage = null;
    }
    else {
      conversationTitleOfMessage = conversationStore.getConversationWithId(
        message.getConversationId()).getTitle();
    }

    messageContent = message.getContent();
    messageId = message.getId();

    userCreationTime = null;
    nameOfUser = null;
    userId = null;
	}

  /**
   * Constructor used for testing. Supply a mock for message.
   *
   * @param message a mock message used for testing.
   * @param testTitle  a mock string used for testing (the conversation for the message doesn't 
   *                exist because it is a test. Would other wise give a nullptr exception).
   */
  public Event(Message message, String testTitle) {
    eventType = EventType.MESSAGE;

    userStore = UserStore.getInstance();
    conversationStore = ConversationStore.getInstance();

    titleOfConversation = null;
    authorIdForConversation = null;
    conversationCreationTime = null;
    conversationId = null;

    messageCreationTime = message.getCreationTime();
    authorIdForMessage = message.getAuthorId();
    conversationTitleOfMessage = testTitle;
    messageContent = message.getContent();
    messageId = message.getId();

    userCreationTime = null;
    nameOfUser = null;
    userId = null;
  }

  /**
   * Constructs an new event using a User object.
   *
   * @param user an object of type User
   */
  public Event(User user) {
    eventType = EventType.USER;

    userStore = UserStore.getInstance();
    conversationStore = ConversationStore.getInstance();

    titleOfConversation = null;
    authorIdForConversation = null;
    conversationCreationTime = null;
    conversationId = null;

    messageCreationTime = null;
    authorIdForMessage = null;
    conversationTitleOfMessage = null;
    messageContent = null;
    messageId = null;
    userCreationTime = user.getCreationTime();
    nameOfUser = user.getName();
    userId = user.getId();
	}

	/** Outputs a string based on the type given in the constructor. */
	public String toString() {

	  if(eventType == EventType.CONVERSATION) {
        return toString(conversationCreationTime) + " PST: "+ 
          userStore.getUser(authorIdForConversation).getName() + 
          " created a new conversation: ";
	  }
	  if(eventType == EventType.MESSAGE) {
        return toString(messageCreationTime) + " PST: " + 
          userStore.getUser(authorIdForMessage).getName() + 
          " sent a message in " + conversationTitleOfMessage + 
          ": " + "\"" + messageContent + "\"";
	  }
	  if(eventType == EventType.USER) {
	  	return toString(userCreationTime) + " PST: " + nameOfUser + " joined!";
	  }
	  else{
	  	System.err.println("This object has no event type. This is impossible. I don't know how you even caused this error");
	  	return null;
	  }
	}

  /** Formats time of type Instant into a string. */
	public String toString(Instant unformattedTime) {
	  DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
      .withLocale(Locale.US)
      .withZone(ZoneId.systemDefault());

    String formattedTime = formatter.format(unformattedTime);

    return formattedTime;
	}

  public String getTitleOfConversation() {
    return titleOfConversation;
  }

	/** Returns the type of event that the object is. */
	public EventType getEventType() { 
    return eventType;
	}

  /** 
   *  Returns seconds from the time Java was created 
   *  to the time the event was created as a Long. 
   */
	public long getCreationTime() {
		if(eventType == EventType.USER) {
			return userCreationTime.getEpochSecond();
		}
		if(eventType == EventType.MESSAGE) {
			return messageCreationTime.getEpochSecond();
		}
		if(eventType == EventType.CONVERSATION) {
			return conversationCreationTime.getEpochSecond();
		}
		else {
			System.err.println("This object has no event type");
			return 0;
		}
	}

  public UUID getId() {
    if(eventType == EventType.USER) {
      return userId;
    }
    if(eventType == EventType.MESSAGE) {
      return messageId;
    }
    if(eventType == EventType.CONVERSATION) {
      return conversationId;
    }
    else {
      System.err.println("This object has no event type");
      return null;
    }
  }
}
