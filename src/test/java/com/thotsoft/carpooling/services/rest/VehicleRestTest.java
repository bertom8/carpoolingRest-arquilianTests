package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.model.Vehicle;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@RunAsClient
public class VehicleRestTest {
    private static List<Vehicle> vehicleList = new ArrayList<>();

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @Test
    public void addVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        Vehicle localVehicle = new Vehicle();
        localVehicle.setLicenceNumber("addVehicle-1");
        localVehicle.setType("M3");
        localVehicle.setManufacturer("BMW");
        localVehicle.setDesign("Sedan");
        localVehicle.setColor("black");
        localVehicle.setRoad(true);
        localVehicle.setAnimal(false);
        localVehicle.setClime(true);
        localVehicle.setComfort(true);
        localVehicle.setSmoking(false);
        assertEquals(localVehicle.getLicenceNumber(), vehicleRest.addVehicle(localVehicle));
        vehicleList.add(localVehicle);
    }

    @Test(expected = Exception.class)
    public void addNullVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) {
        vehicleRest.addVehicle(null);
    }

    @Test(expected = Exception.class)
    public void addVehicleWithNoDefiniteFields(@ArquillianResteasyResource("") VehicleRest vehicleRest) {
        vehicleRest.addVehicle(new Vehicle());
    }

    @Test
    public void removeVehicle(@ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        User admin = new User();
        admin.setPassword("removeVehicle");
        admin.setAdmin(true);
        admin.setEmail("removeVehicle@vehicle.com");
        admin.setName("removeVehicle");
        admin.setPhoneNumber("85967485");
        webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke()
                .close();
        UserRestTest.userList.add(admin);

        //Login with admin
        Response login = webTarget.path("/login/")
                .queryParam("email", admin.getEmail())
                .queryParam("pass", admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        Vehicle localVehicle = new Vehicle();
        localVehicle.setLicenceNumber("removeVehicle-1");
        localVehicle.setType("M3");
        localVehicle.setManufacturer("BMW");
        localVehicle.setDesign("Sedan");
        localVehicle.setColor("black");
        localVehicle.setRoad(true);
        localVehicle.setAnimal(false);
        localVehicle.setClime(true);
        localVehicle.setComfort(true);
        localVehicle.setSmoking(false);

        webTarget.path("/vehicle/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(localVehicle))
                .invoke().close();
        vehicleList.add(localVehicle);

        Response response = webTarget.path("/vehicle")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .build(HttpMethod.DELETE, Entity.json(localVehicle))
                .invoke();
        assertTrue(response.readEntity(Boolean.class));
    }

    @Test(expected = Exception.class)
    public void removeNullVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) {
        vehicleRest.removeVehicle((Vehicle) null);
    }

    @Test(expected = Exception.class)
    public void removeNullStringVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) {
        vehicleRest.removeVehicle((String) null);
    }

    @Test(expected = Exception.class)
    public void removeEmptyStringVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) {
        vehicleRest.removeVehicle("");
    }

    @Test
    public void removeVehicle1(@ArquillianResteasyResource("") WebTarget webTarget) throws Exception {
        User admin = new User();
        admin.setPassword("removeVehicle1");
        admin.setAdmin(true);
        admin.setEmail("removeVehicle1@vehicle.com");
        admin.setName("removeVehicle1");
        admin.setPhoneNumber("85967485");
        webTarget.path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(admin))
                .invoke()
                .close();
        UserRestTest.userList.add(admin);

        //Login with admin
        Response login = webTarget.path("/login/")
                .queryParam("email", admin.getEmail())
                .queryParam("pass", admin.getPassword())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .build("POST")
                .invoke();
        Cookie jSessionId = login.getCookies().get("JSESSIONID");
        login.close();

        Vehicle localVehicle = new Vehicle();
        localVehicle.setLicenceNumber("removeVehicle1-1");
        localVehicle.setType("M3");
        localVehicle.setManufacturer("BMW");
        localVehicle.setDesign("Sedan");
        localVehicle.setColor("black");
        localVehicle.setRoad(true);
        localVehicle.setAnimal(false);
        localVehicle.setClime(true);
        localVehicle.setComfort(true);
        localVehicle.setSmoking(false);

        webTarget.path("/vehicle/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPut(Entity.json(localVehicle))
                .invoke().close();
        vehicleList.add(localVehicle);

        Response response = webTarget.path("/vehicle/{licenceNumber}")
                .resolveTemplate("licenceNumber", localVehicle.getLicenceNumber())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .cookie(jSessionId)
                .delete();
        assertTrue(response.readEntity(Boolean.class));
    }

    @Test
    public void getVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        Vehicle localVehicle = new Vehicle();
        localVehicle.setLicenceNumber("getVehicle-1");
        localVehicle.setType("M3");
        localVehicle.setManufacturer("BMW");
        localVehicle.setDesign("Sedan");
        localVehicle.setColor("black");
        localVehicle.setRoad(true);
        localVehicle.setAnimal(false);
        localVehicle.setClime(true);
        localVehicle.setComfort(true);
        localVehicle.setSmoking(false);
        vehicleRest.addVehicle(localVehicle);
        vehicleList.add(localVehicle);
        assertEquals(localVehicle, vehicleRest.getVehicle(localVehicle.getLicenceNumber()));
    }

    @Test(expected = Exception.class)
    public void getNullVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) {
        vehicleRest.getVehicle(null);
    }

    @Test
    public void updateVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        Vehicle localVehicle = new Vehicle();
        localVehicle.setLicenceNumber("updateVehicle-1");
        localVehicle.setType("M3");
        localVehicle.setManufacturer("BMW");
        localVehicle.setDesign("Sedan");
        localVehicle.setColor("black");
        localVehicle.setRoad(true);
        localVehicle.setAnimal(false);
        localVehicle.setClime(true);
        localVehicle.setComfort(true);
        localVehicle.setSmoking(false);
        vehicleRest.addVehicle(localVehicle);
        vehicleList.add(localVehicle);

        localVehicle.setManufacturer("OwnManufacturer");
        vehicleRest.updateVehicle(localVehicle.getLicenceNumber(), localVehicle);
        assertEquals(localVehicle, vehicleRest.getVehicle(localVehicle.getLicenceNumber()));
    }

    @Test(expected = Exception.class)
    public void updateNullVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) {
        vehicleRest.updateVehicle("updateNullVehicle-1", null);
    }

    @Test
    public void listVehicles(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        assertEquals(vehicleList, vehicleRest.listVehicles());
    }

}