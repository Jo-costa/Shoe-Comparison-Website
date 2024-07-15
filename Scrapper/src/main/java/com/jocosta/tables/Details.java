package com.jocosta.tables;

import jakarta.persistence.*;

/**
 * Represents the "details" table in the database.
 * it is used to map java fields to database fields
 * */

@Entity
@Table(name="details")
public class Details {


    /**
     * Unique id for details table
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * colour column for details table
     * */
    @Column(name = "colour")
    private String colour;

    /**
     * retailer name column for details table
     * */
    @Column(name = "retailer_name")
    private String retailer_name;

    /**
     * retailer_url column for details table
     * */
    @Column(name = "retailer_url")
    private String retailer_url;


    /**
     * Models table Foreign key
     * */
    @ManyToOne
    @JoinColumn(name="model_id")
    private Models model_id;


    /**
     *
     * Default constructor for the Details Class
     * */
    public Details() {
    }


    /**
     *
     * Sets the colour for the product in details table
     *
     * @param colour set the id of the product
     * */
    public void setColour(String colour) {
        this.colour = colour;
    }


    /**
     *
     * Sets the retailer name for the product in details table
     *
     * @param retailer_name set the id of the product
     * */
    public void setRetailer_name(String retailer_name) {
        this.retailer_name = retailer_name;
    }


    /**
     *
     * Sets the retailer URL for the product in details table
     *
     * @param retailer_url set the id of the product
     * */
    public void setRetailer_url(String retailer_url) {
        this.retailer_url = retailer_url;
    }


    /**
     *
     * Sets the model Id for the product in details table
     *
     * @param model_id set the id of the product
     * */
    public void setModel_id(Models model_id) {
        this.model_id = model_id;
    }
}
