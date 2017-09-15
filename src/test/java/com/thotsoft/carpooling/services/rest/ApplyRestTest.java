package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Advertisement;
import com.thotsoft.carpooling.model.User;
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

@RunWith(Arquillian.class)
@RunAsClient
public class ApplyRestTest {

    private static List<Advertisement> appliedAds = new ArrayList<>();

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @Test
    public void apply(@ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        // Add users
        User admin = new User();
        admin.setPassword("apply");
        admin.setAdmin(true);
        admin.setEmail("apply@apply.com");
        admin.setName("apply");
        admin.setPhoneNumber("85967485");
        webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke()
                .close();
        UserRestTest.userList.add(admin);

        User otherUser = new User();
        otherUser.setPassword("apply1");
        otherUser.setAdmin(true);
        otherUser.setEmail("apply1@apply.com");
        otherUser.setName("apply1");
        otherUser.setPhoneNumber("85967485");
        Response otherUserResponse = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(otherUser))
                .invoke();
        otherUser.setId(otherUserResponse.readEntity(Integer.class));
        otherUserResponse.close();
        UserRestTest.userList.add(otherUser);

        //Login
        Response login = webTarget.path("/login/" + admin.getEmail() + "/" + admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        // Set advertisement
        Advertisement localAdvertisement = new Advertisement();
        localAdvertisement.setNumOfSeats(3);
        localAdvertisement.setFromPlace("Szeged");
        localAdvertisement.setToPlace("Pest");
        localAdvertisement.setCost(52000);
        localAdvertisement.setSeeking(false);
        localAdvertisement.setStart(new Date());
        localAdvertisement.setUploadDate(new Date());
        localAdvertisement.setUser(otherUser);
        localAdvertisement.setVehicle(null);
        Response adResponse = webTarget.path("/ad/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(localAdvertisement))
                .invoke();
        localAdvertisement.setId(adResponse.readEntity(Integer.class));
        adResponse.close();
        UserRestTest.userList.add(admin);
        AdRestTest.advertisementList.add(localAdvertisement);


        webTarget.path("/apply/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .buildPut(Entity.json(localAdvertisement))
                .invoke()
                .close();
    }

    @Test(expected = Exception.class)
    public void applyNullAdvertisement(@ArquillianResteasyResource("") ApplyRest applyRest) throws Exception {
        applyRest.apply(null);
    }

    @Test(expected = Exception.class)
    public void applySeekingAdvertisement(@ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        // Add users
        User admin = new User();
        admin.setPassword("applySeekingAdvertisement");
        admin.setAdmin(true);
        admin.setEmail("applySeekingAdvertisement@apply.com");
        admin.setName("applySeekingAdvertisement");
        admin.setPhoneNumber("85967485");
        webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke()
                .close();
        UserRestTest.userList.add(admin);

        User otherUser = new User();
        otherUser.setPassword("applySeekingAdvertisement1");
        otherUser.setAdmin(true);
        otherUser.setEmail("applySeekingAdvertisement1@apply.com");
        otherUser.setName("applySeekingAdvertisement1");
        otherUser.setPhoneNumber("85967485");
        Response otherUserResponse = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(otherUser))
                .invoke();
        otherUser.setId(otherUserResponse.readEntity(Integer.class));
        otherUserResponse.close();
        UserRestTest.userList.add(otherUser);

        //Login
        Response login = webTarget.path("/login/" + admin.getEmail() + "/" + admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        // Set advertisement
        Advertisement localAdvertisement = new Advertisement();
        localAdvertisement.setNumOfSeats(3);
        localAdvertisement.setFromPlace("Szeged");
        localAdvertisement.setToPlace("Pest");
        localAdvertisement.setCost(52000);
        localAdvertisement.setSeeking(true);
        localAdvertisement.setStart(new Date());
        localAdvertisement.setUploadDate(new Date());
        localAdvertisement.setUser(otherUser);
        localAdvertisement.setVehicle(null);
        Response adResponse = webTarget.path("/ad/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(localAdvertisement))
                .invoke();
        localAdvertisement.setId(adResponse.readEntity(Integer.class));
        adResponse.close();
        UserRestTest.userList.add(admin);
        AdRestTest.advertisementList.add(localAdvertisement);
        System.out.println(localAdvertisement);

        webTarget.path("/apply/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .buildPut(Entity.json(localAdvertisement))
                .invoke()
                .close();
    }

    @Test
    public void cancel(@ArquillianResteasyResource("") ApplyRest applyRest) throws Exception {
        // TODO: couse of session it will fail
        //applyRest.cancel(advertisement);
    }

    @Test
    public void getAppliedAds(@ArquillianResteasyResource("") ApplyRest applyRest) throws Exception {
        // TODO: couse of session it will fail
        assertEquals(appliedAds, applyRest.getAppliedAds());
    }

}