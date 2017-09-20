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
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class ApplyRestTest {

    private static List<Advertisement> appliedAds = new ArrayList<>();
    Map<String, NewCookie> cookiesMap;
    private List<String> cookies;
    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @Test
    public void apply(@ArquillianResteasyResource("") ResteasyWebTarget webTarget) throws Exception {
        // Add users
        User admin = new User();
        admin.setPassword("apply");
        admin.setAdmin(true);
        admin.setEmail("apply@apply.com");
        admin.setName("apply");
        admin.setPhoneNumber("85967485");
        Response adminRespone = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke();
        admin.setId(adminRespone.readEntity(Integer.class));
        adminRespone.close();
        UserRestTest.userList.add(admin);

        LoginRest proxy = new ResteasyClientBuilder().build().register(new ClientResponseFilter() {
            @Override
            public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
                cookiesMap = responseContext.getCookies();
                ApplyRestTest.this.cookies = responseContext.getHeaders().get("Set-Cookie");
                responseContext.getHeaders().values().forEach(System.out::println);
            }
        }).target(deploymentURL.toURI()).proxy(LoginRest.class);

        proxy.login(admin.getEmail(), admin.getPassword());

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
        otherUser.setPassword(LoginRestImpl.hash(otherUser.getPassword()));
        UserRestTest.userList.add(otherUser);

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

        AdRest adRest = webTarget.register(new ClientRequestFilter() {
            @Override
            public void filter(ClientRequestContext requestContext) throws IOException {
                List<Object> cookies = new ArrayList<>();
                cookies.addAll(cookiesMap.values());
                requestContext.getHeaders().put("Cookie", cookies);
            }
        }).proxy(AdRest.class);

        localAdvertisement.setId(adRest.addAdvertisement(localAdvertisement));
        UserRestTest.userList.add(admin);
        AdRestTest.advertisementList.add(localAdvertisement);

        ApplyRest applyRest = webTarget.register(new ClientRequestFilter() {
            @Override
            public void filter(ClientRequestContext requestContext) throws IOException {
                requestContext.getHeaders().put("Set-Cookie", new ArrayList<>(cookies));
            }
        }).proxy(ApplyRest.class);

        applyRest.apply(localAdvertisement);
        appliedAds.add(localAdvertisement);
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
        Response adminRespone = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke();
        admin.setId(adminRespone.readEntity(Integer.class));
        adminRespone.close();
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
        otherUser.setPassword(LoginRestImpl.hash(otherUser.getPassword()));
        UserRestTest.userList.add(otherUser);

        //Login
        Response login = webTarget.path("/login/")
                .queryParam("email", admin.getEmail())
                .queryParam("pass", admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
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
        AdRestTest.advertisementList.add(localAdvertisement);

        Response response = webTarget.path("/apply/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .buildPut(Entity.json(localAdvertisement))
                .invoke();
        assertNotEquals(Response.Status.NO_CONTENT, response.getStatus());
        response.close();
    }

    @Test
    public void cancel(@ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        // Add users
        User admin = new User();
        admin.setPassword("cancel");
        admin.setAdmin(true);
        admin.setEmail("cancel@vehicle.com");
        admin.setName("cancel");
        admin.setPhoneNumber("85967485");
        Response adminRespone = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke();
        admin.setId(adminRespone.readEntity(Integer.class));
        adminRespone.close();
        UserRestTest.userList.add(admin);

        User otherUser = new User();
        otherUser.setPassword("cancel1");
        otherUser.setAdmin(true);
        otherUser.setEmail("cancel1@apply.com");
        otherUser.setName("cancel1");
        otherUser.setPhoneNumber("85967485");
        Response otherUserResponse = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(otherUser))
                .invoke();
        otherUser.setId(otherUserResponse.readEntity(Integer.class));
        otherUserResponse.close();
        otherUser.setPassword(LoginRestImpl.hash(otherUser.getPassword()));
        UserRestTest.userList.add(otherUser);

        //Login
        Response login = webTarget.path("/login/")
                .queryParam("email", admin.getEmail())
                .queryParam("pass", admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
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
                .cookie(jSessionId)
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

        Response response = webTarget.path("/apply/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build("DELETE", Entity.json(localAdvertisement))
                .invoke();
        assertEquals(Response.Status.NO_CONTENT, response.getStatus());
        response.close();
    }

    @Test
    public void getAppliedAds(@ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        // Add users
        User admin = new User();
        admin.setPassword("getAppliedAds");
        admin.setAdmin(true);
        admin.setEmail("getAppliedAds@apply.com");
        admin.setName("getAppliedAds");
        admin.setPhoneNumber("85967485");
        Response adminRespone = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke();
        admin.setId(adminRespone.readEntity(Integer.class));
        adminRespone.close();
        UserRestTest.userList.add(admin);

        //Login
        Response login = webTarget.path("/login/")
                .queryParam("email", admin.getEmail())
                .queryParam("pass", admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        Response response = webTarget.path("/apply/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .get();
        assertEquals(appliedAds, response.readEntity(List.class));
        response.close();
    }

    @Test
    public void apply2(@ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        User admin = new User();
        admin.setPassword("apply2");
        admin.setAdmin(true);
        admin.setEmail("apply2@vehicle.com");
        admin.setName("apply2");
        admin.setPhoneNumber("85967485");
        Response adminRespone = webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke();
        admin.setId(adminRespone.readEntity(Integer.class));
        adminRespone.close();
        UserRestTest.userList.add(admin);

        LoginRest proxy = new ResteasyClientBuilder().build().register(new ClientResponseFilter() {
            @Override
            public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
                responseContext.getHeaders().values().forEach(System.out::println);
            }
        }).target(deploymentURL.toURI()).proxy(LoginRest.class);

        proxy.login(admin.getEmail(), admin.getPassword());
    }

}