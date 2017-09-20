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

    @Test
    public void addUser(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User user1 = new User();
        user1.setEmail("addUser@jee.hu");
        user1.setAddress(new Address());
        user1.setPassword("123456");
        user1.setAdmin(false);
        user1.setName("addUser");
        user1.setPhoneNumber("964485");
        user1.setId(userRest.addUser(user1));
        assertNotEquals(0, user1.getId());
        user1.setPassword(LoginRestImpl.hash(user1.getPassword()));
        userList.add(user1);
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
        user1.setId(userRest.addUser(user1));
        assertNotEquals(0, user1.getId());
        user1.setPassword(LoginRestImpl.hash(user1.getPassword()));
        userList.add(user1);
    }

    @Test
    public void removeUser(@ArquillianResteasyResource("") WebTarget webTarget,
                           @ArquillianResteasyResource("") UserRest userRest) {
        User localAdmin = new User();
        localAdmin.setPassword("123456");
        localAdmin.setAdmin(true);
        localAdmin.setEmail("removeUser@jee.hu");
        localAdmin.setName("removeUser");
        localAdmin.setPhoneNumber("85967485");
        localAdmin.setId(userRest.addUser(localAdmin));
        userList.add(localAdmin);

        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("removeUser-2@jee.hu");
        localUser.setName("removeUser-2");
        localUser.setPhoneNumber("85967485");
        localUser.setId(userRest.addUser(localUser));
        localUser.setPassword(LoginRestImpl.hash(localUser.getPassword()));
        userList.add(localUser);

        //Login with admin
        Response login = webTarget.path("/login/")
                .queryParam("email", localAdmin.getEmail())
                .queryParam("pass", localAdmin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

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
        localAdmin.setAdmin(true);
        localAdmin.setEmail("removeUser1@jee.hu");
        localAdmin.setName("removeUser1");
        localAdmin.setPhoneNumber("85967485");
        localAdmin.setId(userRest.addUser(localAdmin));
        userList.add(localAdmin);

        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("removeUser1-2@jee.hu");
        localUser.setName("removeUser1-2");
        localUser.setPhoneNumber("85967485");
        localUser.setId(userRest.addUser(localUser));
        localUser.setPassword(LoginRestImpl.hash(localUser.getPassword()));
        userList.add(localUser);

        //Login with admin
        Response login = webTarget.path("/login/")
                .queryParam("email", localAdmin.getEmail())
                .queryParam("pass", localAdmin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        //Delete user
        Response response = webTarget.path("/user/{id}")
                .resolveTemplate("id", localUser.getId())
                .request()
                .cookie(jSessionId)
                .delete();
        assertTrue(response.readEntity(Boolean.class));
        response.close();
    }

    @Test(expected = Exception.class)
    public void removeNullUser(@ArquillianResteasyResource("") WebTarget webTarget,
                               @ArquillianResteasyResource("") UserRest userRest) {
        User localAdmin = new User();
        localAdmin.setPassword("123456");
        localAdmin.setAdmin(true);
        localAdmin.setEmail("removeNullUser@jee.hu");
        localAdmin.setName("removeNullUser");
        localAdmin.setPhoneNumber("85967485");
        localAdmin.setId(userRest.addUser(localAdmin));
        userList.add(localAdmin);

        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("removeNullUser-2@jee.hu");
        localUser.setName("removeNullUser-2");
        localUser.setPhoneNumber("85967485");
        localUser.setId(userRest.addUser(localUser));
        localUser.setPassword(LoginRestImpl.hash(localUser.getPassword()));
        userList.add(localUser);

        //Login with admin
        Response login = webTarget.path("/login/")
                .queryParam("email", localAdmin.getEmail())
                .queryParam("pass", localAdmin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        //Delete user
        Response response = webTarget.path("/user")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build(HttpMethod.DELETE, Entity.json(null))
                .invoke();
        assertTrue(response.readEntity(Boolean.class));
        response.close();
    }

    @Test(expected = Exception.class)
    public void removeUserIfNotAdmin(@ArquillianResteasyResource("") WebTarget webTarget,
                                     @ArquillianResteasyResource("") UserRest userRest) {
        User notAdmin = new User();
        notAdmin.setPassword("123456");
        notAdmin.setAdmin(false);
        notAdmin.setEmail("removeUserIfNotAdmin@jee.hu");
        notAdmin.setName("removeUserIfNotAdmin");
        notAdmin.setPhoneNumber("85967485");
        notAdmin.setId(userRest.addUser(notAdmin));
        userList.add(notAdmin);

        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("removeUserIfNotAdmin-2@jee.hu");
        localUser.setName("removeUserIfNotAdmin-2");
        localUser.setPhoneNumber("85967485");
        localUser.setId(userRest.addUser(localUser));
        localUser.setPassword(LoginRestImpl.hash(localUser.getPassword()));
        userList.add(localUser);

        //Login with admin
        Response login = webTarget.path("/login/")
                .queryParam("email", notAdmin.getEmail())
                .queryParam("pass", notAdmin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

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

        User localAdmin = new User();
        localAdmin.setPassword("123456");
        localAdmin.setAdmin(true);
        localAdmin.setEmail("removeUserWithZeroId@jee.hu");
        localAdmin.setName("removeUserWithZeroId");
        localAdmin.setPhoneNumber("85967485");
        // Add admin User
        Response adminResponse = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(localAdmin))
                .invoke();
        localAdmin.setId(adminResponse.readEntity(Integer.class));
        adminResponse.close();
        userList.add(localAdmin);

        //Login with admin
        Response login = webTarget.path("/login/")
                .queryParam("email", localAdmin.getEmail())
                .queryParam("pass", localAdmin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        //Delete user
        Response response = webTarget.path("/user/{id}")
                .resolveTemplate("id", 0)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build(HttpMethod.DELETE)
                .invoke();
        assertTrue(response.readEntity(Boolean.class));
        response.close();
    }

    @Test
    public void getUser(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("getUser@jee.hu");
        localUser.setName("getUser");
        localUser.setPhoneNumber("85967485");
        localUser.setId(userRest.addUser(localUser));
        localUser.setPassword(LoginRestImpl.hash(localUser.getPassword()));
        userList.add(localUser);
        assertEquals(userRest.getUser(localUser.getId()), localUser);
    }

    @Test
    public void updateUser(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("updateUser@jee.hu");
        localUser.setName("updateUser");
        localUser.setPhoneNumber("85967485");
        localUser.setId(userRest.addUser(localUser));
        localUser.setPassword(LoginRestImpl.hash(localUser.getPassword()));
        userList.add(localUser);
        localUser.setName("updateUser-2");
        userRest.updateUser(localUser.getId(), localUser);
        assertEquals(localUser, userRest.getUser(localUser.getId()));
    }

    @Test
    public void listUsers(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        List<User> storedUsers = userRest.listUsers();
        assertEquals(userList, storedUsers);
    }

    @Test
    public void isAlreadyRegistered(@ArquillianResteasyResource("") UserRest userRest,
                                    @ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        User localUser = new User();
        localUser.setPassword("123456");
        localUser.setAdmin(false);
        localUser.setEmail("isAlreadyRegistered@jee.hu");
        localUser.setName("isAlreadyRegistered");
        localUser.setPhoneNumber("85967485");
        localUser.setId(userRest.addUser(localUser));
        userList.add(localUser);

        //Login with localUser
        Response login = webTarget.path("/login/")
                .queryParam("email", localUser.getEmail())
                .queryParam("pass", localUser.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        Response response = webTarget.path("/user/{" + User.FIELD_EMAIL + "}")
                .resolveTemplate(User.FIELD_EMAIL, localUser.getEmail())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertTrue(response.readEntity(Boolean.class));
        response.close();
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
        admin.setPassword(LoginRestImpl.hash(admin.getPassword()));
        userList.add(admin);

        //Add user User
        Response userAdd = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(localUser))
                .invoke();
        localUser.setId(userAdd.readEntity(Integer.class));
        userAdd.close();
        localUser.setPassword(LoginRestImpl.hash(localUser.getPassword()));
        userList.add(localUser);

        //Login with admin
        Response login = webTarget.path("/login/")
                .queryParam("email", admin.getEmail())
                .queryParam("pass", admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();
        return jSessionId;
    }
}