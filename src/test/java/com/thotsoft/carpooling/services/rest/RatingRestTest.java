package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Address;
import com.thotsoft.carpooling.model.Rating;
import com.thotsoft.carpooling.model.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class RatingRestTest {

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @Test
    public void rate(@ArquillianResteasyResource("") RatingRest ratingRest,
                     @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User user = new User();
        user.setAddress(new Address());
        user.setPassword("123456");
        user.setAdmin(false);
        user.setEmail("ijmhu@ert.hu");
        user.setName("Phil");
        user.setPhoneNumber("85967485");
        user.setId(userRest.addUser(user));
        UserRestTest.userList.add(user);

        ratingRest.rate(user, Rating.Rate.EXCELLENT);
        assertEquals(0, ratingRest.getRating(user), 0);
    }

    @Test(expected = Exception.class)
    public void rateANullUser(@ArquillianResteasyResource("") RatingRest ratingRest) {
        ratingRest.rate(null, Rating.Rate.NOT_BAD);
    }

    @Test
    public void getRating(@ArquillianResteasyResource("") RatingRest ratingRest,
                          @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User user = new User();
        user.setAddress(new Address());
        user.setPassword("123456");
        user.setAdmin(false);
        user.setEmail("omiuze@ert.hu");
        user.setName("Phil");
        user.setPhoneNumber("85967485");
        user.setId(userRest.addUser(user));
        UserRestTest.userList.add(user);

        assertEquals(0, ratingRest.getRating(user), 0);
    }

    @Test(expected = Exception.class)
    public void getRatingOfNullUser(@ArquillianResteasyResource("") RatingRest ratingRest) {
        ratingRest.getRating(null);
    }

}