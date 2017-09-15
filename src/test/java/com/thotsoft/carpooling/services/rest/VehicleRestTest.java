package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Vehicle;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@RunAsClient
public class VehicleRestTest {
    private static Vehicle vehicle = new Vehicle();
    private static Vehicle vehicle2 = new Vehicle();
    private static List<Vehicle> vehicleList = new ArrayList<>();

    @ArquillianResource
    private URL deploymentURL;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return CreateDeployment.createDeployment();
    }

    @BeforeClass
    public static void beforeClass() {
        vehicle.setLicenceNumber("asd-789");
        vehicle.setType("M3");
        vehicle.setManufacturer("BMW");
        vehicle.setDesign("Sedan");
        vehicle.setColor("black");
        vehicle.setRoad(true);
        vehicle.setAnimal(false);
        vehicle.setClime(true);
        vehicle.setComfort(true);
        vehicle.setSmoking(false);

        vehicle2.setLicenceNumber("qwe-123");
        vehicle2.setType("Swift");
        vehicle2.setManufacturer("Suzuki");
        vehicle2.setDesign("Sedan");
        vehicle2.setColor("green");
        vehicle2.setRoad(true);
        vehicle2.setAnimal(true);
        vehicle2.setClime(false);
        vehicle2.setComfort(false);
        vehicle2.setSmoking(false);
    }

    @Test
    public void addVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        assertEquals(vehicle.getLicenceNumber(), vehicleRest.addVehicle(vehicle));
        vehicleList.add(vehicle);
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
    public void removeVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        Vehicle testedVehicle = vehicleRest.getVehicle(vehicle.getLicenceNumber());
        assertTrue(vehicleRest.removeVehicle(testedVehicle));
        vehicleList.remove(vehicle);
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
    public void removeVehicle1(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        vehicleRest.addVehicle(vehicle2);
        vehicleList.add(vehicle2);
        assertTrue(vehicleRest.removeVehicle(vehicle2.getLicenceNumber()));
        vehicleList.remove(vehicle2);
    }

    @Test
    public void getVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        Vehicle vehicle3 = vehicle2;
        vehicle3.setLicenceNumber("uhj-759");
        vehicleRest.addVehicle(vehicle3);
        vehicleList.add(vehicle3);
        assertEquals(vehicle3, vehicleRest.getVehicle(vehicle3.getLicenceNumber()));
    }

    @Test(expected = Exception.class)
    public void getNullVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) {
        vehicleRest.getVehicle(null);
    }

    @Test
    public void updateVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        Vehicle vehicle4 = vehicle2;
        vehicle4.setManufacturer("OwnManufacturer");
        vehicleRest.updateVehicle(vehicle4.getLicenceNumber(), vehicle4);
        assertEquals(vehicle4, vehicleRest.getVehicle(vehicle2.getLicenceNumber()));
    }

    @Test(expected = Exception.class)
    public void updateNullVehicle(@ArquillianResteasyResource("") VehicleRest vehicleRest) {
        vehicleRest.updateVehicle(vehicle.getLicenceNumber(), null);
    }

    @Test
    public void listVehicles(@ArquillianResteasyResource("") VehicleRest vehicleRest) throws Exception {
        assertEquals(vehicleList, vehicleRest.listVehicles());
    }

}