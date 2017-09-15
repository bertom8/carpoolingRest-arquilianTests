package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Advertisement;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class ApplyRestTest {

    private static List<Advertisement> appliedAds = new ArrayList<>();
    private static Advertisement advertisement = new Advertisement();

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @BeforeClass
    public static void beforeClass() {
        advertisement.setNumOfSeats(3);
        advertisement.setFromPlace("Szeged");
        advertisement.setToPlace("Pest");
        advertisement.setCost(52000);
        advertisement.setSeeking(false);
        advertisement.setStart(new Date());
        advertisement.setUploadDate(new Date());
        advertisement.setUser(null);
        advertisement.setVehicle(null);
    }

    @Test
    public void apply(@ArquillianResteasyResource("") ApplyRest applyRest) throws Exception {
        // TODO: couse of session it will fail
        applyRest.apply(advertisement);
    }

    @Test(expected = Exception.class)
    public void applyNullAdvertisement(@ArquillianResteasyResource("") ApplyRest applyRest) throws Exception {
        applyRest.apply(null);
    }

    @Test(expected = Exception.class)
    public void applySeekingAdvertisement(@ArquillianResteasyResource("") ApplyRest applyRest) throws Exception {
        // TODO: couse of session it will fail
        Advertisement ad = new Advertisement();
        ad.setNumOfSeats(3);
        ad.setFromPlace("Szeged");
        ad.setToPlace("Pest");
        ad.setCost(52000);
        ad.setSeeking(true);
        ad.setStart(new Date());
        ad.setUploadDate(new Date());
        ad.setUser(null);
        ad.setVehicle(null);
        applyRest.apply(ad);
    }

    @Test
    public void cancel(@ArquillianResteasyResource("") ApplyRest applyRest) throws Exception {
        // TODO: couse of session it will fail
        applyRest.cancel(advertisement);
    }

    @Test
    public void getAppliedAds(@ArquillianResteasyResource("") ApplyRest applyRest) throws Exception {
        // TODO: couse of session it will fail
        assertEquals(appliedAds, applyRest.getAppliedAds());
    }

}