package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Address;
import com.thotsoft.carpooling.model.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserRestTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserRestImpl.class)
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "jboss-web.xml");
    }

    @EJB
    private UserRestImpl userRest;

    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setAddress(new Address());
        user.setPassword("123456");
        user.setAdmin(false);
        user.setEmail("asd@ert.hu");
        user.setName("Phil");
        user.setPhoneNumber("85967485");
        userRest.addUser(user);
        assertTrue(true);
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