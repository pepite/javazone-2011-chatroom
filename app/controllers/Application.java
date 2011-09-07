package controllers;

import play.*;

import static play.libs.F.Matcher.ClassOf;
import static play.mvc.Http.WebSocketEvent.*;
import play.libs.F.Either;
import play.libs.F.EventStream;
import play.libs.F.Promise;
import play.mvc.*;
import play.utils.HTML;

import java.util.*;

import models.*;
import models.Room.Message;

public class Application extends Controller {
	
    public static void index() {
        render();
    }

    public static void room(String user) {
    	render(user);
    }
    
    
    public static class ChatRoom extends WebSocketController {
    	
    	public static void join(String user) {
    		Room room = Room.room;
    		
    		EventStream<Message> messages = room.join();
    		
    		while (inbound.isOpen()) {
    			
    			Either<Http.WebSocketEvent, Message> e 
    			= await(Promise.waitEither(inbound.nextEvent(), messages.nextEvent()));
    			
    			for (String text : TextFrame.match(e._1)) {
    				room.say(user, text);
    			}
    			
    			for (Message message : ClassOf(Message.class).match(e._2)) {
    				outbound.send(HTML.htmlEscape(message.user + ":  " +message.text));
    			}
    		}
    		
    	}
    }
}