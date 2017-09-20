package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Address;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.CreateDeployment;
import com.thotsoft.carpooling.services.rest.UserRest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.net.URL;

import static org.junit.Assert.assertNotEquals;

@RunWith(Arquillian.class)
public class UserRestImplTest {
    @EJB(mappedName = "java:global/carpooling/UserRestImpl!com.thotsoft.carpooling.services.rest.UserRest")
    private UserRest userRest;

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setEmail("addUser@email.com");
        user.setName("addUser");
        user.setPassword("addUser");
        user.setAddress(new Address());
        user.setPhoneNumber("47896");
        user.setAdmin(true);
        user.setId(userRest.addUser(user));
        assertNotEquals(0, user.getId());
    }

    @Test
    public void removeUser() throws Exception {
    }

    @Test
    public void removeUser1() throws Exception {
    }

    @Test
    public void getUser() throws Exception {
    }

    @Test
    public void updateUser() throws Exception {
    }

    @Test
    public void listUsers() throws Exception {
    }

    @Test
    public void isAlreadyRegistered() throws Exception {
    }

    @Test
    public void countUsers() throws Exception {
    }

}