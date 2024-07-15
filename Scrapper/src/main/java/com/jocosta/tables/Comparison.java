package com.jocosta.tables;


import jakarta.persistence.*;
import java.math.BigDecimal;


/**
 * Represents the "comparison" table in the database.
 * it is used to map java fields to database fields
 * */
@Entity
public class Comparison {


    /**
     * Unique id for comparison table
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    /**
     * details_id column for comparison table
     * */
    @ManyToOne
    @JoinColumn(name="details_id")
    private Details details;

    /**
     * price column for comparison table
     * */
    @Column(name="price")
    private BigDecimal price;

    /**
     *
     * Default constructor for the Comparison Class
     * */
    public Comparison() {
    }


    public void setId(int id) {
        this.id = id;
    }



    /**
     *
     * Sets the details for the product in comparison table
     *
     * @param details set the id of the product
     * */
    public void setDetails(Details details) {
        this.details = details;
    }


    /**
     *
     * Sets the price for the product in comparison table
     *
     * @param price set the id of the product
     * */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
