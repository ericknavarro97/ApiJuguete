/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barbaro.apijuguete.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 *
 * @author ericknavarro
 */
public class HibernateUtil {

    public static Session getSession() {

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // lee hibernate.cfg.xml
                .build(); //Construlle el objeto

        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        Session session = sessionFactory.openSession();
        
        System.out.println("Se abrió la conexión");

        return session;

    }
    
    public static void closeSession(Session session){
        
        System.out.println("Se cerró la conexión");
        
        session.close();
        
    }

}
