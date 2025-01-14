package org.finance2.core;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name="transactionTable")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    @NotNull(message = "User must not be null")
    User user;

    @Column(name="amount", nullable = false)
    @NotNull
    Double amount;

    @Column(name="type", nullable = false)
    @NotBlank
    String type;

    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    @NotNull(message = "User must not be null")
    Category category;

    @Column(name="description", nullable = false)
    @NotBlank
    String description;

    @Column(name="transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date transactionDate;
}
