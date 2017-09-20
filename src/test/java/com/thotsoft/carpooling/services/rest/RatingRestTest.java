package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Address;
import com.thotsoft.carpooling.model.Rating;
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
                     @ArquillianResteasyResource("") UserRest userRest,
                     @ArquillianResteasyResource("") LoginRest loginRest,
                     @ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        User user = new User();
        user.setAddress(new Address());
        user.setPassword("123456");
        user.setAdmin(false);
        user.setEmail("rate@ert.hu");
        user.setName("rate");
        user.setPhoneNumber("85967485");
        user.setId(userRest.addUser(user));
        user.setPassword(LoginRestImpl.hash(user.getPassword()));
        UserRestTest.userList.add(user);

        Response login = webTarget.path("/login/")
                .queryParam("email", user.getEmail())
                .queryParam("pass", user.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        Response response = webTarget.path("/rating/{rate}")
                .resolveTemplate("rate", Rating.Rate.EXCELLENT)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .put(Entity.json(user));

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
        user.setEmail("getRating@ert.hu");
        user.setName("getRating");
        user.setPhoneNumber("85967485");
        user.setId(userRest.addUser(user));
        user.setPassword(LoginRestImpl.hash(user.getPassword()));
        UserRestTest.userList.add(user);

        assertEquals(0, ratingRest.getRating(user), 0);
    }

    @Test(expected = Exception.class)
    public void getRatingOfNullUser(@ArquillianResteasyResource("") RatingRest ratingRest) {
        ratingRest.getRating(null);
    }

}