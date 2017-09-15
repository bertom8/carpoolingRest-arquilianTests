package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Message;
import com.thotsoft.carpooling.model.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class MessageRestTest {

    private static Message message = new Message();
    private static User from = new User();
    private static User to = new User();
    private static List<Message> messageList = new ArrayList<>();

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @BeforeClass
    public static void beforeClass() {
        from.setPassword("123456");
        from.setName("messageFrom");

        to.setPassword("123456");
        to.setName("messageTo");

        message.setDate(new Date());
        message.setText("testtext");
        message.setTitle("atitle");
        message.setUnread(true);
    }

    @Test
    public void addMessage(@ArquillianResteasyResource("") MessageRest messageRest,
                           @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        from.setEmail("addmessagefrom@email.com");
        userRest.addUser(from);
        UserRestTest.userList.add(from);

        to.setEmail("addmessageto@email.com");
        userRest.addUser(to);
        UserRestTest.userList.add(to);

        message.setSource(from);
        message.setTarget(to);
        assertNotEquals(0, messageRest.addMessage(message));
        messageList.add(message);
    }

    @Test
    public void removeMessage(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
    }

    @Test
    public void removeMessage1(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
    }

    @Test
    public void getMessage(@ArquillianResteasyResource("") MessageRest messageRest,
                           @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        from.setEmail("getmessagefrom@email.com");
        userRest.addUser(from);
        UserRestTest.userList.add(from);

        to.setEmail("getmassegeto@email.com");
        userRest.addUser(to);
        UserRestTest.userList.add(to);

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
        from.setEmail("listmessagesfrom1@email.com");
        userRest.addUser(from);
        UserRestTest.userList.add(from);

        to.setEmail("listmessagesto1@email.com");
        userRest.addUser(to);
        UserRestTest.userList.add(to);

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
        from.setEmail("listmessagesto2@email.com");
        userRest.addUser(from);
        UserRestTest.userList.add(from);

        to.setEmail("listmessagesto2@email.com");
        userRest.addUser(to);
        UserRestTest.userList.add(to);

        message.setSource(from);
        message.setTarget(to);
        message.setId(messageRest.addMessage(message));
        messageList.add(message);
        List<Message> messageToUser = new ArrayList<>();
        messageToUser.add(message);
        assertEquals(messageToUser, messageRest.listMessagesTo(to));
    }

}