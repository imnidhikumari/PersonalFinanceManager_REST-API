package org.finance2.resources;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.finance2.core.TransactionTable;
import org.finance2.core.User;
import org.finance2.dao.CategoryDAO;
import org.finance2.core.Category;
import org.finance2.dao.TransactionDAO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private  final TransactionDAO transactionDAO;
    private final Timer addTransactionTimer;
    private final Timer getAllTransactionTimer;
    private final Timer getTransactionByIdTimer;


    @Inject
    public TransactionResource(TransactionDAO transactionDAO, MetricRegistry metrics){
        this.transactionDAO = transactionDAO;

        this.addTransactionTimer=metrics.timer(MetricRegistry.name(CategoryResource.class,"addTransaction"));
        this.getAllTransactionTimer =metrics.timer(MetricRegistry.name(CategoryResource.class,"getAllTransaction"));
        this.getTransactionByIdTimer =metrics.timer(MetricRegistry.name(CategoryResource.class,"getTransactionById"));

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTransaction(TransactionTable transactionTable){
        final Timer.Context context = addTransactionTimer.time();
        try{
            transactionDAO.addTransaction(transactionTable);
            return Response.status(Response.Status.CREATED).entity(transactionTable).build();
        }finally {
            context.stop();
        }
    }

    @GET
    @Path("id/{id}")
    public Response getTransactionById(@PathParam("id") Long id){
        final Timer.Context context = getTransactionByIdTimer.time();
        try{
            return transactionDAO.getTransactionById(id)
                    .map(transactionTable -> Response.ok(transactionTable).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        }finally {
            context.stop();
        }
    }

    @GET
    public Response getAllTransaction(){
        final Timer.Context context = getAllTransactionTimer.time();
        try{
            List<TransactionTable> transactionTables = transactionDAO.getAllTransaction();
            return Response.ok(transactionTables).build();
        }finally {
            context.stop();
        }
    }


}
