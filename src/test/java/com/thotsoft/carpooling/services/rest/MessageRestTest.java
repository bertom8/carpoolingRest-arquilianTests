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
public class MessageRestTest {

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @Test
    public void addMessage(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
    }

    @Test
    public void removeMessage(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
    }

    @Test
    public void removeMessage1(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
    }

    @Test
    public void getMessage(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
    }

    @Test
    public void listMessages(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
    }

    @Test
    public void listMessagesFrom(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
    }

    @Test
    public void listMessagesTo(@ArquillianResteasyResource("") MessageRest messageRest) throws Exception {
    }

}