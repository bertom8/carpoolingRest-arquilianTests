package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Message;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.LoginRestImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class MessageRestTest {
    private static List<Message> messageList = new ArrayList<>();

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @Test
    public void addMessage(@ArquillianResteasyResource("") MessageRest messageRest,
                           @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User from = new User();
        from.setPassword("123456");
        from.setName("addMessage-from");
        from.setEmail("addMessage-from@email.com");
        from.setId(userRest.addUser(from));
        from.setPassword(LoginRestImpl.hash(from.getPassword()));
        UserRestTest.userList.add(from);

        User to = new User();
        to.setPassword("123456");
        to.setName("addMessage-to");
        to.setEmail("addMessage-to@email.com");
        to.setId(userRest.addUser(to));
        to.setPassword(LoginRestImpl.hash(to.getPassword()));
        UserRestTest.userList.add(to);

        Message message = new Message();
        message.setDate(new Date());
        message.setText("addMessage");
        message.setTitle("addMessage");
        message.setUnread(true);
        message.setSource(from);
        message.setTarget(to);
        message.setId(messageRest.addMessage(message));
        messageList.add(message);
        assertNotEquals(0, message.getId());
    }

    @Test
    public void removeMessage(@ArquillianResteasyResource("") MessageRest messageRest,
                              @ArquillianResteasyResource("") UserRest userRest,
                              @ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        User from = new User();
        from.setPassword("123456");
        from.setName("removeMessage-from");
        from.setEmail("removeMessage-from@email.com");
        from.setId(userRest.addUser(from));
        from.setPassword(LoginRestImpl.hash(from.getPassword()));
        UserRestTest.userList.add(from);

        User to = new User();
        to.setPassword("123456");
        to.setName("removeMessage-to");
        to.setEmail("removeMessage-to@email.com");
        to.setId(userRest.addUser(to));
        to.setPassword(LoginRestImpl.hash(to.getPassword()));
        UserRestTest.userList.add(to);

        Message message = new Message();
        message.setDate(new Date());
        message.setText("removeMessage");
        message.setTitle("removeMessage");
        message.setUnread(true);
        message.setSource(from);
        message.setTarget(to);
        message.setId(messageRest.addMessage(message));
        messageList.add(message);

        //Login
        Response login = webTarget.path("/login/")
                .queryParam("email", from.getEmail())
                .queryParam("pass", from.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        Response response = webTarget.path("/message/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build("DELETE", Entity.json(message))
                .invoke();
        assertEquals(Response.Status.OK, response.getStatus());
        response.close();
    }

    @Test
    public void removeMessage1(@ArquillianResteasyResource("") MessageRest messageRest,
                               @ArquillianResteasyResource("") UserRest userRest,
                               @ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        User from = new User();
        from.setPassword("123456");
        from.setName("removeMessage1-from");
        from.setEmail("removeMessage1-from@email.com");
        from.setId(userRest.addUser(from));
        from.setPassword(LoginRestImpl.hash(from.getPassword()));
        UserRestTest.userList.add(from);

        User to = new User();
        to.setPassword("123456");
        to.setName("removeMessage1-to");
        to.setEmail("removeMessage1-to@email.com");
        to.setId(userRest.addUser(to));
        to.setPassword(LoginRestImpl.hash(to.getPassword()));
        UserRestTest.userList.add(to);

        Message message = new Message();
        message.setDate(new Date());
        message.setText("removeMessage1");
        message.setTitle("removeMessage1");
        message.setUnread(true);
        message.setSource(from);
        message.setTarget(to);
        message.setId(messageRest.addMessage(message));
        messageList.add(message);

        //Login
        Response login = webTarget.path("/login/")
                .queryParam("email", from.getEmail())
                .queryParam("pass", from.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        Response response = webTarget.path("/message/{id}")
                .resolveTemplate(Message.FIELD_ID, message.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build("DELETE", Entity.json(message))
                .invoke();
        assertEquals(Response.Status.OK, response.getStatus());
        response.close();
    }

    @Test
    public void getMessage(@ArquillianResteasyResource("") MessageRest messageRest,
                           @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User from = new User();
        from.setPassword("123456");
        from.setName("getMessage-from");
        from.setEmail("getMessage-from@email.com");
        from.setId(userRest.addUser(from));
        UserRestTest.userList.add(from);

        User to = new User();
        to.setPassword("123456");
        to.setName("getMessage-to");
        to.setEmail("getMessage-to@email.com");
        to.setId(userRest.addUser(to));
        UserRestTest.userList.add(to);

        Message message = new Message();
        message.setDate(new Date());
        message.setText("getMessage");
        message.setTitle("getMessage");
        message.setUnread(true);
        message.setSource(from);
        message.setTarget(to);
        message.setId(messageRest.addMessage(message));
        messageList.add(message);
        assertEquals(message, messageRest.getMessage(message.getId()));
    }

    @Test
    public void listMessages(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
        assertEquals(messageList, messageRest.listMessages());
    }

    @Test
    public void listMessagesFrom(@ArquillianResteasyResource("") MessageRest messageRest,
                                 @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User from = new User();
        from.setPassword("123456");
        from.setName("listMessagesFrom-from");
        from.setEmail("listMessagesFrom-from@email.com");
        from.setId(userRest.addUser(from));
        UserRestTest.userList.add(from);

        User to = new User();
        to.setPassword("123456");
        to.setName("listMessagesFrom-to");
        to.setEmail("listMessagesFrom-to@email.com");
        to.setId(userRest.addUser(to));
        UserRestTest.userList.add(to);

        Message message = new Message();
        message.setDate(new Date());
        message.setText("listMessagesFrom");
        message.setTitle("listMessagesFrom");
        message.setUnread(true);
        message.setSource(from);
        message.setTarget(to);
        message.setId(messageRest.addMessage(message));
        messageList.add(message);
        List<Message> messageFromUser = new ArrayList<>();
        messageFromUser.add(message);
        assertEquals(messageFromUser, messageRest.listMessagesFrom(from));
    }

    @Test
    public void listMessagesTo(@ArquillianResteasyResource("") MessageRest messageRest,
                               @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User from = new User();
        from.setPassword("123456");
        from.setName("listMessagesTo-from");
        from.setEmail("listMessagesTo-from@email.com");
        from.setId(userRest.addUser(from));
        UserRestTest.userList.add(from);

        User to = new User();
        to.setPassword("123456");
        to.setName("listMessagesTo-to");
        to.setEmail("listMessagesTo-to@email.com");
        to.setId(userRest.addUser(to));
        UserRestTest.userList.add(to);

        Message message = new Message();
        message.setDate(new Date());
        message.setText("listMessagesTo");
        message.setTitle("listMessagesTo");
        message.setUnread(true);
        message.setSource(from);
        message.setTarget(to);
        message.setId(messageRest.addMessage(message));
        messageList.add(message);
        List<Message> messageToUser = new ArrayList<>();
        messageToUser.add(message);
        assertEquals(messageToUser, messageRest.listMessagesTo(to));
    }

}