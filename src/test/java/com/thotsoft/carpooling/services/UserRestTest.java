package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Address;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.UserRest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.inject.Inject;
import java.io.File;

import static org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies.createDependency;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserRestTest {
    public static final Logger logger = LoggerFactory.getLogger(UserRestTest.class);

    @Deployment
    public static WebArchive createDeployment() {

        JavaArchive contentModelJar = ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "com.thotsoft.carpooling")
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return deployment().addAsLibrary(contentModelJar);
    }

    private static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, "carpooling.war")
                .addAsLibraries(mavenDependencies());
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

    @EJB(mappedName = "java:global/carpooling/UserRestImpl!com.thotsoft.carpooling.services.rest.UserRest")
    private UserRest userRest;

    @Test
    public void addUser() throws Exception {

        User user = new User();
        user.setAddress(new Address());
        user.setPassword("123456");
        user.setAdmin(false);
        user.setEmail("asd@ert.hu");
        user.setName("Phil");
        user.setPhoneNumber("85967485");
        userRest.addUser(user);
        assertTrue(true);
    }

    @Test
    public void removeUser() throws Exception {
    }

    @Test
    public void removeUser1() throws Exception {
    }

    @Test
    public void getUser() throws Exception {
    }

    @Test
    public void updateUser() throws Exception {
    }

    @Test
    public void listUsers() throws Exception {
    }

    @Test
    public void isAlreadyRegistered() throws Exception {
    }

    @Test
    public void countUsers() throws Exception {
    }

}