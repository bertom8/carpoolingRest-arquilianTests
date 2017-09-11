package com.thotsoft.carpooling.war.services;

import com.thotsoft.carpooling.api.VehicleService;
import com.thotsoft.carpooling.api.model.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Stateless
@Remote(VehicleService.class)
public class VehicleServiceImpl implements VehicleService {
    private static Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addVehicle(Vehicle vehicle) {
        Objects.requireNonNull(vehicle);
        em.persist(vehicle);
        em.flush();
        logger.info("Vehicle added: {}", vehicle);
    }

    @Override
    public boolean removeVehicle(String licenceNumber) {
        Vehicle vehicle = getVehicle(licenceNumber);
        if (vehicle != null) {
            em.remove(vehicle);
            logger.info("User was removed with this id: {}", licenceNumber);
            return true;
        } else {
            throw new IllegalArgumentException("There was no such item in database: " + licenceNumber);
        }
    }

    @Override
    public boolean removeVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            em.remove(vehicle);
            logger.info("User was removed with this id: {}", vehicle.getLicenceNumber());
            return true;
        } else {
            throw new IllegalArgumentException("Vehicle was null");
        }
    }

    @Override
    public Vehicle getVehicle(String licenceNumber) {
        return em.find(Vehicle.class, licenceNumber);
    }

    @Override
    public void updateVehicle(String licenceNumber, Vehicle vehicle) {
        Objects.requireNonNull(vehicle);
        Vehicle storedVehicle = getVehicle(licenceNumber);
        if (storedVehicle != null) {
            em.merge(vehicle);
            logger.info("Vehicle updated: {}", vehicle);
        }
    }

    @Override
    public List<Vehicle> listVehicles() {
        return em.createQuery("from Vehicle", Vehicle.class).getResultList();
    }
}
