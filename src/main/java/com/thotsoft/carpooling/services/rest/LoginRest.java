package com.thotsoft.carpooling.services.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/login")
public interface LoginRest {

    @GET
    @Path("/{email}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    boolean login(@PathParam("email") String email, @PathParam("password") String password);

    @DELETE
    void logout();
}
