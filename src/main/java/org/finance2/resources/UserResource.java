package org.finance2.resources;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.finance2.core.User;
import org.finance2.dao.UserDAO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private  final UserDAO userDAO;
    private final Timer RegisterUserTimer;

    @Inject
    public UserResource(UserDAO userDAO, MetricRegistry metrics){
        this.userDAO =userDAO;
        this.RegisterUserTimer =metrics.timer(MetricRegistry.name(CategoryResource.class,"ResisterUser"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response RegisterUser(User user){
        final Timer.Context context = RegisterUserTimer.time();
        try{
            userDAO.registerUser(user);
            return Response.status(Response.Status.CREATED).entity(user).build();
        }finally {
            context.stop();
        }
    }

}
