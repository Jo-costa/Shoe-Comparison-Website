package com.jocosta;

import com.jocosta.tables.Comparison;
import com.jocosta.tables.Details;
import com.jocosta.tables.Models;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


/**
 *
 * Class For saving data to the databse using hibernate
 * */
public class SaveData {


    private SessionFactory sessionFactory;


    /**
     * Default constructor
     * */
    public SaveData(){

    }


    /**
     * Method to init hibernate sesseion factory
     *
     * */
    public void init(){
        try {


            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();

            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            } catch (Exception e) {

                System.out.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }catch (Throwable ex){
            System.out.println("SessionFactory Creation failed: " + ex);
        }

    }

    /**
     * Method to add Models entity to the database
     *
     * @param model Model Entity to be added to the database
     * */
     public void addModels(Models model){
         Session session = sessionFactory.getCurrentSession();

         session.beginTransaction();

         session.save(model);

         session.getTransaction().commit();

         session.close();

     }


    /**
     * Method to add Details entity to the database
     *
     * @param detail Model Entity to be added to the database
     * */
     public void addDetails(Details detail){
         Session session = sessionFactory.getCurrentSession();

         session.beginTransaction();

         session.save(detail);

         session.getTransaction().commit();

         session.close();

     }

    /**
     * Method to add Comparison entity to the database
     *
     * @param comp Comparison Entity to be added to the database
     * */
     public void addComparison(Comparison comp){
         Session session = sessionFactory.getCurrentSession();

         session.beginTransaction();

         session.save(comp);

         session.getTransaction().commit();

         session.close();

     }




    /**
     * Method to shutdown hibernate session factory
     * */
     public void shutdown(){
        sessionFactory.close();
     }


}
