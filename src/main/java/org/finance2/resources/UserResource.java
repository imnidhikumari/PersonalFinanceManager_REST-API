package org.finance2.resources;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.finance2.core.User;
import org.finance2.dao.UserDAO;
import org.finance2.dtoOrmodels.LoginRequest;
import org.finance2.utils.JwtUtil;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private  final UserDAO userDAO;
    private final Timer RegisterUserTimer;
    private final Timer loginTimer;

    @Inject
    public UserResource(UserDAO userDAO, MetricRegistry metrics){
        this.userDAO =userDAO;
        this.RegisterUserTimer =metrics.timer(MetricRegistry.name(CategoryResource.class,"ResisterUser"));
        this.loginTimer =metrics.timer(MetricRegistry.name(CategoryResource.class,"LoginTimer"));
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(LoginRequest loginRequest){
    final Timer.Context context = loginTimer.time();
    try{
        Optional<User> userOptional = userDAO.findUsersByEmail(loginRequest.getEmail());
        if(userOptional.isEmpty()){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid email or password").build();
        }
        User user = userOptional.get();

        if(!user.getPassword().equals(loginRequest.getPassword())){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid email or password").build();
        }
        String token = JwtUtil.generateToken(user.getEmail());

        return Response.ok(token).build();
    }catch (Exception e){
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("An error occurred during login").build();
    }
    }

}
