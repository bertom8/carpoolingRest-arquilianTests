package com.thotsoft.carpooling.services.rest;

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

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@RunAsClient
public class LoginRestTest {

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @Test
    public void login(@ArquillianResteasyResource("") LoginRest loginRest,
                      @ArquillianResteasyResource("") UserRest userRest) throws Exception {
        User user = new User();
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test@email.com");
        userRest.addUser(user);
        UserRestTest.userList.add(user);
        assertTrue(loginRest.login(user.getEmail(), user.getPassword()));
    }

    @Test(expected = Exception.class)
    public void loginNotExistedEmail(@ArquillianResteasyResource("") LoginRest loginRest) {
        loginRest.login("nemletezikezaz@email.cim", "123456");
    }

    @Test(expected = Exception.class)
    public void loginNullPassword(@ArquillianResteasyResource("") LoginRest loginRest,
                                  @ArquillianResteasyResource("") UserRest userRest) {
        User user = new User();
        user.setName("testnullpasswordlogin");
        user.setPassword("test");
        user.setEmail("null@email.com");
        userRest.addUser(user);
        UserRestTest.userList.add(user);
        loginRest.login(user.getEmail(), null);
    }

    @Test
    public void logout(@ArquillianResteasyResource("") LoginRest loginRest) throws Exception {
        loginRest.logout();
    }

}