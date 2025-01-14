package org.finance2.resources;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.finance2.core.TransactionTable;
import org.finance2.dao.TransactionDAO;
import org.finance2.dtoOrmodels.response.BalanceOverviewResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/balance/user/{userId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BalanceResource {

    private  final TransactionDAO transactionDAO;
    private final Timer getBalanceTimer;


    @Inject
    public BalanceResource(TransactionDAO transactionDAO, MetricRegistry metrics){
        this.transactionDAO = transactionDAO;

        this.getBalanceTimer =metrics.timer(MetricRegistry.name(CategoryResource.class,"addTransaction"));
    }

    @GET
    public Response getBalance(@PathParam("userId")Long userId) {
        double totalBalance = 0.0;
        final Timer.Context context = getBalanceTimer.time();
        try{
            List<TransactionTable> transactionTables = transactionDAO.getTransactionByUserId(userId);
            for (TransactionTable transaction : transactionTables) {
                if ("INCOME".equals(transaction.getType())) {
                    totalBalance += transaction.getAmount();
                } else if ("EXPENSE".equals(transaction.getType())) {
                    totalBalance -= transaction.getAmount();
                }
            }
            BalanceOverviewResponse balanceOverviewResponse = new BalanceOverviewResponse();
            balanceOverviewResponse.setAvailableBalance(totalBalance);
            return Response.ok(balanceOverviewResponse).build();
        }finally{
            context.stop();
        }
    }
}
