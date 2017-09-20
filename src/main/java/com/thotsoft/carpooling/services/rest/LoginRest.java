package com.thotsoft.carpooling.services.rest;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/login")
public interface LoginRest {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    boolean login(@QueryParam("email") String email, @QueryParam("pass") String password);

    @DELETE
    void logout() throws ServletException;
}
