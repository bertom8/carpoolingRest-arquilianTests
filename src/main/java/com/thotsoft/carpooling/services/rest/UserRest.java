package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.User;

import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
public interface UserRest {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    int addUser(User user);

    @DELETE
    @Path("/{" + User.FIELD_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeUser(@PathParam(User.FIELD_ID) int id);

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeUser(User user);

    @GET
    @Path("/{" + User.FIELD_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    User getUser(@PathParam(User.FIELD_ID) int id);

    @POST
    @Path("/{" + User.FIELD_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateUser(@PathParam(User.FIELD_ID) int id, User user);

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

    @GET
    @Path("/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    User getUserByEmail(@PathParam("email") String email);
}
