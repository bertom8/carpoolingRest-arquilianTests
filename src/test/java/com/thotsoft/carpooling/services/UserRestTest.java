package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Address;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.LoginRest;
import com.thotsoft.carpooling.services.rest.UserRest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ValidationException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies.createDependency;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@RunAsClient
public class UserRestTest {
    public static final Logger logger = LoggerFactory.getLogger(UserRestTest.class);
    private static User user = new User();
    private static User admin = new User();

//    @EJB(mappedName = "java:global/carpooling/UserRestImpl!com.thotsoft.carpooling.services.rest.UserRest")
//    private UserRest userRest;
//
//    @EJB(mappedName = "java:global/carpooling/LoginRestImpl!com.thotsoft.carpooling.services.rest.LoginRest")
//    private LoginRest loginRest;

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        JavaArchive contentModelJar = ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "com.thotsoft.carpooling")
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return deployment().addAsLibrary(contentModelJar);
    }

    private static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, "carpooling.war")
                .addAsLibraries(mavenDependencies())
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF", "jboss-web.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF", "web.xml"));
    }

    private static File[] mavenDependencies() {
        return Maven.configureResolver().withMavenCentralRepo(false)
                .loadPomFromFile("pom.xml").importCompileAndRuntimeDependencies()
                .addDependencies(
                        createDependency("org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-impl-maven",
                                ScopeType.TEST, false)
                )
                .resolve().withTransitivity().asFile();
    }

    @BeforeClass
    public static void beforeClass() {
        user.setAddress(new Address());
        user.setPassword("123456");
        user.setAdmin(false);
        user.setEmail("asd@ert.hu");
        user.setName("Phil");
        user.setPhoneNumber("85967485");

        admin.setAdmin(true);
        admin.setEmail("valami@valami.com");
        admin.setPhoneNumber("8965748");
        admin.setName("Admin");
        admin.setPassword("admin");
        admin.setAddress(new Address());
    }

    @Test
    @InSequence(1)
    public void addUser(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
//        user.setAddress(new Address());
//        user.setPassword("123456");
//        user.setAdmin(false);
//        user.setEmail("asd@ert.hu");
//        user.setName("Phil");
//        user.setPhoneNumber("85967485");
        userRest.addUser(user);
        assertTrue(true);
    }

    @Test(expected = ValidationException.class)
    @InSequence(6)
    public void addUserWithInvalidEmail(@ArquillianResteasyResource("") UserRest userRest) {
        User user1 = new User();
        user1.setEmail("asdasd");
        user1.setAddress(new Address());
        user1.setPassword("123456");
        user1.setAdmin(false);
        user1.setName("Wrong email");
        user1.setPhoneNumber("964485");
        userRest.addUser(user1);
    }

    @Test
    @InSequence(8)
    public void removeUser(@ArquillianResteasyResource("") UserRest userRest,
                           @ArquillianResteasyResource("") LoginRest loginRest) throws Exception {
        admin.setAdmin(true);
        admin.setEmail("valami@valami.com");
        admin.setPhoneNumber("8965748");
        admin.setName("Admin");
        admin.setPassword("admin");
        admin.setAddress(new Address());
        userRest.addUser(admin);
        loginRest.login(admin.getEmail(), "123456");
        assertTrue(userRest.removeUser(user));
    }

    @Test
    @InSequence(9)
    public void removeUser1(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        fail();
    }

    @Test
    @InSequence(2)
    public void getUser(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        assertEquals(userRest.getUser(user.getId()), user);
    }

    @Test
    @InSequence(7)
    public void updateUser(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        fail();
    }

    @Test
    @InSequence(4)
    public void listUsers(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        List<User> storedUsers = userRest.listUsers();
        List<User> users = new ArrayList<>();
        users.add(user);
        assertEquals(users, storedUsers);
    }

    @Test
    @InSequence(3)
    public void isAlreadyRegistered(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        assertTrue(userRest.isAlreadyRegistered(user.getEmail()));
    }

    @Test
    @InSequence(5)
    public void countUsers(@ArquillianResteasyResource("") UserRest userRest) throws Exception {
        assertEquals(userRest.countUsers(), 1);
    }
}