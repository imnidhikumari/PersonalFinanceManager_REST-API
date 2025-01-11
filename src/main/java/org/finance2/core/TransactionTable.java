package org.finance2.core;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name="transaction")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    @NotBlank
    User user;

    @Column(name="amount", nullable = false)
    @NotBlank
    Double amount;

    @Column(name="type", nullable = false)
    @NotBlank
    String type;

    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    @NotBlank
    Category category;

    @Column(name="description", nullable = false)
    @NotBlank
    String description;

    @Column(name="transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date transactionDate;
}
