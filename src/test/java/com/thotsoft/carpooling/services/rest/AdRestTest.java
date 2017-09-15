package com.thotsoft.carpooling.services.rest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

@RunWith(Arquillian.class)
@RunAsClient
public class AdRestTest {

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