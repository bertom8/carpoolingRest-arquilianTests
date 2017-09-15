package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Address;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.LoginRestImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@RunAsClient
public class UserRestTest {
    static List<User> userList = new ArrayList<>();
    private static User user = new User();
    private static User admin = new User();

//    @EJB(mappedName = "java:global/carpooling/UserRestImpl!com.thotsoft.carpooling.services.rest.UserRest")
//    private UserRest userRest;
//
//    @EJB(mappedName = "java:global/carpooling/LoginRestImpl!com.thotsoft.carpooling.services.rest.LoginRest")
//    private LoginRest loginRest;
    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @BeforeClass
    public static void beforeClass() {
        user.setAddress(new Address());
        user.setPassword("123456");
        user.setAdmin(false);
        user.setEmail("asd@ert.hu");
        user.setName("Phil");
        user.setPhoneNumber("85967485");

        admin.setAdmin(true);
        admin.setEmail("valami@valami.com");
        admin.setPhoneNumber("8965748");
        admin.setName("Admin");
        admin.setPassword("admin");
        admin.setAddress(new Address());
    }

    @Test
    public void addUser(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        user.setId(userRest.addUser(user));
        assertNotEquals(0, user.getId());
        user.setPassword(LoginRestImpl.hash(user.getPassword()));
        userList.add(user);
    }

    @Test(expected = Exception.class)
    public void addUserWithInvalidEmail(@ArquillianResteasyResource("") UserRest userRest) {
        User user1 = new User();
        user1.setEmail("asdasd");
        user1.setAddress(new Address());
        user1.setPassword("123456");
        user1.setAdmin(false);
        user1.setName("Wrong email");
        user1.setPhoneNumber("964485");
        userRest.addUser(user1);
    }

    @Test(expected = Exception.class)
    public void addUserWithInvalidEmail2(@ArquillianResteasyResource("") UserRest userRest) {
        User user1 = new User();
        user1.setEmail("invalid@invalid");
        user1.setAddress(new Address());
        user1.setPassword("123456");
        user1.setAdmin(false);
        user1.setName("Wrong email");
        user1.setPhoneNumber("964485");
        userRest.addUser(user1);
    }

    @Test(expected = Exception.class)
    public void addUserWithInvalidEmail3(@ArquillianResteasyResource("") UserRest userRest) {
        User user1 = new User();
        user1.setEmail("invalid.invalid");
        user1.setAddress(new Address());
        user1.setPassword("123456");
        user1.setAdmin(false);
        user1.setName("Wrong email");
        user1.setPhoneNumber("964485");
        userRest.addUser(user1);
    }

    @Test(expected = Exception.class)
    public void addNullUser(@ArquillianResteasyResource("") UserRest userRest) {
        userRest.addUser(null);
    }

    @Test
    public void addUserWithNullAddress(@ArquillianResteasyResource("") UserRest userRest) {
        User user1 = new User();
        user1.setEmail("email@email.com");
        user1.setAddress(null);
        user1.setPassword("123456");
        user1.setAdmin(false);
        user1.setName("Null address");
        user1.setPhoneNumber("964485");
        assertNotEquals(0, userRest.addUser(user1));
    }

    @Test
    public void removeUser(@ArquillianResteasyResource("") WebTarget webTarget) throws MalformedURLException {

//        System.out.println(Entity.entity(admin, MediaType.APPLICATION_JSON_TYPE).getEntity().toString());
//        webTarget.request().buildPut(Entity.entity(admin, MediaType.APPLICATION_JSON_TYPE)).invoke().close();
        userList.add(admin);
        Response response = webTarget.path("/user")
                .property("user", admin)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .property("user", admin)
                .build(HttpMethod.DELETE, Entity.json(user))
                .invoke();
//        Response response = webTarget.request().property("user", admin).accept(MediaType.APPLICATION_JSON)
//                .header("Content-type", MediaType.APPLICATION_JSON)
//                .build("DELETE", Entity.entity(user, MediaType.APPLICATION_JSON_TYPE))
//                .invoke();
//        logger.error(response.getEntity().toString());
        System.out.println(response.getStatus());
        assertTrue(response.readEntity(Boolean.class));
//                userRest.addUser(admin);
//        loginRest.login(admin.getEmail(), "123456");
//        assertTrue(userRest.removeUser(user));
    }

    @Test
    public void removeNullUser(@ArquillianResteasyResource("") UserRest userRest,
                               @ArquillianResteasyResource("") LoginRest loginRest) {
        fail("Not implemented yet");
    }

    @Test
    public void removeUserIfNotAdmin(@ArquillianResteasyResource("") UserRest userRest,
                                     @ArquillianResteasyResource("") LoginRest loginRest) {
        fail("Not implemented yet");
    }

    @Test
    public void removeUserWithZeroId(@ArquillianResteasyResource("") UserRest userRest) {
        fail("Not implemented yet");
    }

    @Test
    public void getUser(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        assertEquals(userRest.getUser(user.getId()), user);
    }

    @Test
    public void updateUser(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        user.setName("updatedName");
        userRest.updateUser(user.getId(), user);
        assertEquals(user, userRest.getUser(user.getId()));
    }

    @Test
    public void listUsers(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        List<User> storedUsers = userRest.listUsers();
        assertEquals(userList, storedUsers);
    }

    @Test
    public void isAlreadyRegistered(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        assertTrue(userRest.isAlreadyRegistered(user.getEmail()));
    }

    @Test(expected = NullPointerException.class)
    public void isAlreadyRegisteredWithNullParameter(@ArquillianResteasyResource("") UserRest userRest) {
        userRest.isAlreadyRegistered(null);
    }

    @Test
    public void countUsers(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        assertEquals(userRest.countUsers(), userList.size());
    }
}