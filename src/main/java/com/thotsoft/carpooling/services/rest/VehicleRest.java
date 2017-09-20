package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Vehicle;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/vehicle")
public interface VehicleRest {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    String addVehicle(Vehicle vehicle);

    @DELETE
    @Path("/{" + Vehicle.FIELD_LICENCE_NUMBER + "}")
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeVehicle(@PathParam(Vehicle.FIELD_LICENCE_NUMBER) String licenceNumber);

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeVehicle(Vehicle vehicle);

    @GET
    @Path("/{" + Vehicle.FIELD_LICENCE_NUMBER + "}")
    @Produces(MediaType.APPLICATION_JSON)
    Vehicle getVehicle(@PathParam(Vehicle.FIELD_LICENCE_NUMBER) String licenceNumber);

    @POST
    @Path("/{" + Vehicle.FIELD_LICENCE_NUMBER + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateVehicle(@PathParam(Vehicle.FIELD_LICENCE_NUMBER) String licenceNumber, Vehicle vehicle);

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<Vehicle> listVehicles();
}
