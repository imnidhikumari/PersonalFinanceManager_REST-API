package org.finance2.resources;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.finance2.core.User;
import org.finance2.dao.CategoryDAO;
import org.finance2.core.Category;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/Categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private  final CategoryDAO categoryDAO;
    private final Timer addCategoryTimer;
    private final Timer fetchAllCategoryForUserTimer;

    @Inject
    public CategoryResource(CategoryDAO categoryDAO, MetricRegistry metrics){
        this.categoryDAO=categoryDAO;

        this.addCategoryTimer=metrics.timer(MetricRegistry.name(CategoryResource.class,"addCategory"));
        this.fetchAllCategoryForUserTimer =metrics.timer(MetricRegistry.name(CategoryResource.class,"fetchAllCategoryForUser"));

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCategory(Category category){
        final Timer.Context context = addCategoryTimer.time();
        try{
            categoryDAO.addCategory(category);
            return Response.status(Response.Status.CREATED).entity(category).build();
        }finally {
            context.stop();
        }
    }

    @GET
    @Path("/userid/{id}")
    public Response fetchAllCategoryForUser(@PathParam("id") Long userId){
        final Timer.Context context = fetchAllCategoryForUserTimer.time();
        try{
            List<String> categories = categoryDAO.fetchAllCategoryForUser(userId);
            return Response.ok(categories).build();
        }finally {
            context.stop();
        }
    }


}
