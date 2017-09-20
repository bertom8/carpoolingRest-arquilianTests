package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Advertisement;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.LoginRestImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.util.JsonSerialization;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class AdRestTest {

    static List<Advertisement> advertisementList = new ArrayList<>();

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @Test
    public void addAdvertisement(@ArquillianResteasyResource("") WebTarget webTarget) throws Exception {

        InputStream configStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("keycloak-authz.json");

        AuthzClient client = AuthzClient.create(JsonSerialization.readValue(configStream, Configuration.class));

        AccessTokenResponse accessTokenResponse = client.obtainAccessToken("admin", "admin");

        String token = accessTokenResponse.getToken();

        AdRest adRest = new ResteasyClientBuilder().build().register(new ClientRequestFilter() {
            @Override
            public void filter(ClientRequestContext requestContext) throws IOException {
                requestContext.getHeaders().add("Authorization", "Bearer " + token);
            }
        }).target(deploymentURL.toURI()).proxy(AdRest.class);

        User otherUser = new User();
        otherUser.setPassword("addAdvertisement");
        otherUser.setAdmin(true);
        otherUser.setEmail("admin@ad.com");
        otherUser.setName("admin");
        otherUser.setPhoneNumber("85967485");
        Response otherUserResponse = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(otherUser))
                .invoke();
        otherUser.setId(otherUserResponse.readEntity(Integer.class));
        otherUserResponse.close();
        UserRestTest.userList.add(otherUser);

        Advertisement advertisement = new Advertisement();
        advertisement.setNumOfSeats(3);
        advertisement.setFromPlace("Szeged");
        advertisement.setToPlace("Pest");
        advertisement.setCost(52000);
        advertisement.setSeeking(false);
        advertisement.setStart(new Date());
        advertisement.setUploadDate(new Date());
        advertisement.setUser(otherUser);
        advertisement.setVehicle(null);

        advertisement.setId(adRest.addAdvertisement(advertisement));
        assertNotEquals(0, advertisement.getId());
    }

    @Test
    public void removeAdvertisement(@ArquillianResteasyResource("") AdRest adRest) throws Exception {
        // webtarget
    }

    @Test
    public void removeAdvertisement1(@ArquillianResteasyResource("") AdRest adRest) throws Exception {
        // webtarget
    }

    @Test
    public void getAdvertisement(@ArquillianResteasyResource("") AdRest adRest,
                                 @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User otherUser = new User();
        otherUser.setPassword("getAdvertisement");
        otherUser.setAdmin(true);
        otherUser.setEmail("getAdvertisement@apply.com");
        otherUser.setName("getAdvertisement");
        otherUser.setPhoneNumber("85967485");
        otherUser.setId(userRest.addUser(otherUser));
        otherUser.setPassword(LoginRestImpl.hash(otherUser.getPassword()));
        UserRestTest.userList.add(otherUser);

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
        localAdvertisement.setId(adRest.addAdvertisement(localAdvertisement));
        advertisementList.add(localAdvertisement);
        assertEquals(localAdvertisement, adRest.getAdvertisement(localAdvertisement.getId()));
    }

    @Test
    public void updateAdvertisement(@ArquillianResteasyResource("") AdRest adRest) throws Exception {

    }

    @Test
    public void listAds(@ArquillianResteasyResource("") AdRest adRest) throws Exception {
    }

    @Test
    public void countAds(@ArquillianResteasyResource("") AdRest adRest) throws Exception {
    }

}