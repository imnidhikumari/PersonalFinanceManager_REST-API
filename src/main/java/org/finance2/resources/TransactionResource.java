package org.finance2.resources;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.finance2.core.TransactionTable;
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
    private final Timer getTransactionByUserIdTimer;


    @Inject
    public TransactionResource(TransactionDAO transactionDAO, MetricRegistry metrics){
        this.transactionDAO = transactionDAO;

        this.addTransactionTimer=metrics.timer(MetricRegistry.name(CategoryResource.class,"addTransaction"));
        this.getAllTransactionTimer =metrics.timer(MetricRegistry.name(CategoryResource.class,"getAllTransaction"));
        this.getTransactionByUserIdTimer =metrics.timer(MetricRegistry.name(CategoryResource.class,"getTransactionByUserId"));

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
    @Path("/user/{userId}")
    public Response getTransactionsByUserId(@PathParam("userId") Long userId) {
        final Timer.Context context = getTransactionByUserIdTimer.time();
        try {
            List<TransactionTable> transactions = transactionDAO.getTransactionByUserId(userId);
            if (transactions.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No transactions found for user with id: " + userId)
                        .build();
            }
            return Response.ok(transactions).build();
        } finally {
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
