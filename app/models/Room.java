package models;

import play.*;
import play.db.jpa.*;
import play.libs.F.ArchivedEventStream;
import play.libs.F.EventStream;

import javax.persistence.*;
import java.util.*;

public class Room extends Model {
	
	final ArchivedEventStream<Message> messages = new ArchivedEventStream<Room.Message>(50);
	
	public EventStream<Message> join() {
		return messages.eventStream();
	}
	
	public void say(String user, String text) {
		messages.publish(new Message(user, text));
	}
    
	public class Message {
		
		public String user;
		public String text;
		
		public Message(String user, String text) {
			this.user = user;
			this.text = text;
		}
	}
	
	public static Room room = new Room(); 
}
