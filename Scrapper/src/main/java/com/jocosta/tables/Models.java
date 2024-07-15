package com.jocosta.tables;

import jakarta.persistence.*;

/**
* Represents the "models" table in the database.
* it is used to map java fields to database fields
* */

@Entity
@Table(name="models")
public class Models {

    /**
     * Unique id for models table
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * name column for models table
     * */
    @Column(name="name")
    private String name;

    /**
     * description column for model table
     * */
    @Column(name="description")
    private String description;

    /**
     * img_url column for the model table
     * */
    @Column(name="img_url")
    private String img_url;


    /**
     *
     * Default constructor for the Models Class
     * */
    public Models() {
    }

    /**
     *
     * Returns the id for the models table
     *
     * @return the name of the product
     * */
    public String getName() {
        return name;
    }

    /**
     *
     * Sets the id for the models table
     *
     * @param name set the id of the product
     * */
    public void setName(String name) {
        this.name = name;
    }


    /**
     *
     * Sets the description for the product in the models table
     *
     * @param description set the id of the product
     * */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     *
     * Sets the Image URL for the product associated with models table
     *
     * @param img_url set the id of the product
     * */
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

}
