package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.User;

import javax.ejb.Local;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
public interface UserRest {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void addUser(User user);

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeUser(@PathParam("id") int id);

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeUser(User user);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    User getUser(@PathParam("id") int id);

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateUser(@PathParam("id") int id, User user);

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<User> listUsers() throws NamingException;

    @GET
    @Path("/exists/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    boolean isAlreadyRegistered(@PathParam("email") String email);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    int countUsers();

    User getUserByEmail(String email);
}
