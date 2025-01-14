package org.finance2.mapper;

import org.finance2.core.TransactionTable;
import org.finance2.dtoOrmodels.response.TransactionResponse;

public class TransactionMapper {
    public static TransactionResponse mapToResponse(TransactionTable transactionTable) {
        TransactionResponse response = new TransactionResponse();
        response.setUserId(transactionTable.getUser().getId());
        response.setAmount(transactionTable.getAmount());
        response.setType(transactionTable.getType());
        response.setDescription(transactionTable.getDescription());
        return response;
    }
}
