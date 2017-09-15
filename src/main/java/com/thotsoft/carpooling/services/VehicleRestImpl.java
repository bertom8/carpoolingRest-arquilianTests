package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.model.Vehicle;
import com.thotsoft.carpooling.services.rest.VehicleRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Objects;

@Stateless
public class VehicleRestImpl implements VehicleRest {
    private static Logger logger = LoggerFactory.getLogger(VehicleRestImpl.class);

    @Context
    HttpServletRequest request;

    @PersistenceContext
    private EntityManager em;

    /**
     * @param vehicle Vehicle object to insert into DB
     * @return Licence number of Vehicle
     */
    @Override
    public String addVehicle(Vehicle vehicle) {
        Objects.requireNonNull(vehicle);
        em.persist(vehicle);
        logger.info("Vehicle added: {}", vehicle);
        return vehicle.getLicenceNumber();
    }

    /**
     * @param licenceNumber licence number of vehicle
     * @return Was this successfully
     */
    @Override
    public boolean removeVehicle(String licenceNumber) {
        User loggedUser = ((User) request.getSession().getAttribute("user"));
        if (loggedUser == null) {
            throw new IllegalArgumentException("No user logged in!");
        }

        Vehicle vehicle = getVehicle(licenceNumber);
        if (vehicle != null) {
            em.remove(vehicle);
            logger.info("User was removed with this id: {}", licenceNumber);
            return true;
        } else {
            throw new IllegalArgumentException("There was no such item in database: " + licenceNumber);
        }
    }

    /**
     * @param vehicle Vehicle object to remove from DB
     * @return Was this successfully
     */
    @Override
    public boolean removeVehicle(Vehicle vehicle) {
        User loggedUser = ((User) request.getSession().getAttribute("user"));
        if (loggedUser == null) {
            throw new IllegalArgumentException("No user logged in!");
        }

        if (vehicle != null) {
            em.remove(em.contains(vehicle) ? vehicle : em.merge(vehicle));
            logger.info("User was removed with this id: {}", vehicle.getLicenceNumber());
            return true;
        } else {
            throw new IllegalArgumentException("Vehicle was null");
        }
    }

    /**
     * @param licenceNumber licence number of vehicle
     * @return Vehicle object by licenceNumber
     */
    @Override
    public Vehicle getVehicle(String licenceNumber) {
        return em.find(Vehicle.class, licenceNumber);
    }

    /**
     * @param licenceNumber licence number of vehicle
     * @param vehicle       vehicle object by licenceNumber for update DB record to this
     */
    @Override
    public void updateVehicle(String licenceNumber, Vehicle vehicle) {
        Objects.requireNonNull(vehicle);
        Vehicle storedVehicle = getVehicle(licenceNumber);
        if (storedVehicle != null) {
            em.merge(vehicle);
            logger.info("Vehicle updated: {}", vehicle);
        }
    }

    /**
     * @return List of Vehicles of all
     */
    @Override
    public List<Vehicle> listVehicles() {
        return em.createQuery("from Vehicle", Vehicle.class).getResultList();
    }
}
