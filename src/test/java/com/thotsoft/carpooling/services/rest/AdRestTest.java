package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Advertisement;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public void addAdvertisement(@ArquillianResteasyResource("") AdRest adRest) throws Exception {
    }

    @Test
    public void removeAdvertisement(@ArquillianResteasyResource("") AdRest adRest) throws Exception {
    }

    @Test
    public void removeAdvertisement1(@ArquillianResteasyResource("") AdRest adRest) throws Exception {
    }

    @Test
    public void getAdvertisement(@ArquillianResteasyResource("") AdRest adRest) throws Exception {
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