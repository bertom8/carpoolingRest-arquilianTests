package com.thotsoft.carpooling.services.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/login")
public interface LoginRest {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    boolean login(@FormParam("email") String email, @FormParam("password") String password);

    @DELETE
    void logout();
}
