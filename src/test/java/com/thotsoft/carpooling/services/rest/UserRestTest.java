package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Address;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.LoginRestImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public void removeUser(@ArquillianResteasyResource("") WebTarget webTarget) {
        User localAdmin = new User();
        localAdmin.setPassword("123456");
        localAdmin.setAdmin(false);
        localAdmin.setEmail("admin1@jee.hu");
        localAdmin.setName("Phil");
        localAdmin.setPhoneNumber("85967485");

        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("localuser1@jee.hu");
        localUser.setName("Phil");
        localUser.setPhoneNumber("85967485");

        Cookie jSessionId = AddUsersAndLogin(webTarget, localAdmin, localUser);

        //Delete user
        Response response = webTarget.path("/user")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build(HttpMethod.DELETE, Entity.json(localUser))
                .invoke();
        assertTrue(response.readEntity(Boolean.class));
        response.close();
    }

    @Test
    public void removeUser1(@ArquillianResteasyResource("") WebTarget webTarget,
                            @ArquillianResteasyResource("") UserRest userRest) {
        User localAdmin = new User();
        localAdmin.setPassword("123456");
        localAdmin.setAdmin(false);
        localAdmin.setEmail("admin2@jee.hu");
        localAdmin.setName("Phil");
        localAdmin.setPhoneNumber("85967485");

        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("localuser2@jee.hu");
        localUser.setName("Phil");
        localUser.setPhoneNumber("85967485");

        Cookie jSessionId = AddUsersAndLogin(webTarget, localAdmin, localUser);

        System.out.println(userRest.getUser(localUser.getId()));

        //Delete user
        Response response = webTarget.path("/user/{id}")
                .resolveTemplate("id", localUser.getId())
                .request()
                .cookie(jSessionId)
                .delete();
        assertTrue(response.readEntity(Boolean.class));
        response.close();
    }

    @Test
    public void removeNullUser(@ArquillianResteasyResource("") WebTarget webTarget) {
        User notAdmin = new User();
        notAdmin.setPassword("123456");
        notAdmin.setAdmin(false);
        notAdmin.setEmail("notadmin@jee.hu");
        notAdmin.setName("Phil");
        notAdmin.setPhoneNumber("85967485");

        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("localuser1@jee.hu");
        localUser.setName("Phil");
        localUser.setPhoneNumber("85967485");

        Cookie jSessionId = AddUsersAndLogin(webTarget, notAdmin, localUser);

        //Delete user
        Response response = webTarget.path("/user")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build(HttpMethod.DELETE, Entity.json(null))
                .invoke();
        assertTrue(response.readEntity(Boolean.class));
        response.close();
    }

    @Test
    public void removeUserIfNotAdmin(@ArquillianResteasyResource("") WebTarget webTarget) {
        User notAdmin = new User();
        notAdmin.setPassword("123456");
        notAdmin.setAdmin(false);
        notAdmin.setEmail("notadmin2@jee.hu");
        notAdmin.setName("Phil");
        notAdmin.setPhoneNumber("85967485");

        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("localuser2@jee.hu");
        localUser.setName("Phil");
        localUser.setPhoneNumber("85967485");

        Cookie jSessionId = AddUsersAndLogin(webTarget, notAdmin, localUser);

        //Delete user
        Response response = webTarget.path("/user")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build(HttpMethod.DELETE, Entity.json(localUser))
                .invoke();
        assertTrue(response.readEntity(Boolean.class));
        response.close();
    }

    @Test(expected = Exception.class)
    public void removeUserWithZeroId(@ArquillianResteasyResource("") WebTarget webTarget) {
        // Add admin User
        webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke()
                .close();
        userList.add(admin);

        //Login with admin
        Response login = webTarget.path("/login/" + admin.getEmail() + "/" + admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("GET")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        //Delete user
        Response response = webTarget.path("/user/" + 0)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build(HttpMethod.DELETE)
                .invoke();
        assertTrue(response.readEntity(Boolean.class));
        response.close();
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
    @InSequence(1)
    public void listUsers(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        List<User> storedUsers = userRest.listUsers();
        System.out.println(storedUsers);
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

    private Cookie AddUsersAndLogin(@ArquillianResteasyResource("") WebTarget webTarget, User admin, User localUser) {
        // Add admin User
        webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke()
                .close();
        userList.add(admin);

        //Add user User
        Response userAdd = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(localUser))
                .invoke();
        localUser.setId(userAdd.readEntity(Integer.class));
        userAdd.close();
        userList.add(localUser);

        //Login with admin
        Response login = webTarget.path("/login/" + admin.getEmail() + "/" + admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("GET")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();
        return jSessionId;
    }
}