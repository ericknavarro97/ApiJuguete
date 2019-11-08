/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barbaro.apijuguete.resources;

/**
 *
 * @author ericknavarro
 */
import com.barbaro.apijuguete.Models.Juguete;
import com.barbaro.apijuguete.util.HibernateUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.HibernateException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

@Path("juguetero")
public class JugueteRecurso {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJuguetes() {

        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        Map<String, Object> response = new HashMap<String, Object>();

        response.put("mensaje", "jala");

        Session session = null;

        List<Juguete> lista = null;

        SessionFactory sessionFactory = null;

        try {

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            session = sessionFactory.openSession();

            Query<Juguete> query = session.createQuery("FROM Juguete j", Juguete.class);

            lista = query.getResultList();

        } catch (HibernateException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (session != null) {
                session.close();
            }

            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }

        response.put("data: ", lista);

        return Response.status(status).entity(response).build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createJuguete(Juguete juguete) {

        Session session = null;

        Transaction tx = null;

        Map<String, Object> response = null;

        int codigo = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String mensaje = null;

        try {

            session = HibernateUtil.getSession();

            tx = session.beginTransaction();

            juguete.setFecha(new java.util.Date());
            session.save(juguete); //Solo las clases que tienen las etquetas JPA y mapeadas

            /*
            Save
            saveOrUpdate
            delete
            persist
            merge
            detached
             */
            tx.commit();

            codigo = Response.Status.CREATED.getStatusCode();
            mensaje = "Se almacenó el juguete";

        } catch (TransactionException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback(); //Deshace la operacon con la database
            }
            mensaje = "Falló en la transacción, lee bien la facking api prro";

        } catch (HibernateException e) {
            e.printStackTrace();
            mensaje = "Error en el Servidor alv";

        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Intentalo de nuevo más tarde Prro";

        } finally {

            if (session != null) {
                session.close();
            }

        }

        response = new HashMap<>();

        response.put("Codigo: ", codigo);
        response.put("Mensaje: ", mensaje);
        response.put("data", juguete);

        return Response.status(codigo).entity(response).build();

    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJuguete(@PathParam("id") Integer id, Juguete juguete) {

        Session session = null;
        Transaction tx = null;
        Map<String, Object> response = null;

        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        int codigoInt = 0;

        String mensaje = null;

        Juguete jugueteDB = null;

        try {

            session = HibernateUtil.getSession();
            
            //Operaciones con la base de datos
            
            jugueteDB = session.find(Juguete.class, id); 
            
            if(jugueteDB != null){
            
                tx = session.beginTransaction();
                
                jugueteDB.setNombre(juguete.getNombre());
                jugueteDB.setDescripcion(juguete.getDescripcion());
                jugueteDB.setPrecio(juguete.getPrecio());
                
                session.update(jugueteDB);
                
                tx.commit();
                
                status = Response.Status.OK.getStatusCode();
                mensaje = "Se actualizo el recurso";
                codigoInt = 1;
                
            } else{
            
                status = Response.Status.NOT_FOUND.getStatusCode();
                
                mensaje = "Recurso No Encontrado";
                
            }

        } catch (TransactionException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback(); //Deshace la operacon con la database
            }
            mensaje = "Falló en la transacción, lee bien la facking api prro";

        } catch (HibernateException e) {
            e.printStackTrace();
            mensaje = "Error en el Servidor alv";

        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Intentalo de nuevo más tarde Prro";

        } finally {

            if (session != null) {
                session.close();
            }

        }
        
        // Configurar datos de respuesta
        response = new HashMap<>();
        response.put("Codigo", codigoInt);
        response.put("Mensaje", mensaje);
        response.put("Data", jugueteDB);

        // Construir la respuesta
        return Response.status(status).entity(response).build();

    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJuguete(@PathParam("id") Integer id){
        
        Session session = null;
        Transaction tx = null;
        Juguete juguete = null;
        Map<String, Object> response = null;
        
        
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        int codigoInt = 0;
        String mensaje = null;
        
        try{
            
            session = HibernateUtil.getSession();
            
            juguete = session.get(Juguete.class, id);
            
            if(juguete != null){
            
                tx = session.beginTransaction();
                session.remove(juguete);
                tx.commit();
                
                status = Response.Status.OK.getStatusCode();
                
                mensaje = "Recurso Eliminado";
                        
            } else {
            
                status = Response.Status.NOT_FOUND.getStatusCode();
                
                mensaje = "Recurso No Encontrado";
                
            }
        
        }catch (TransactionException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback(); //Deshace la operacon con la database
            }
            mensaje = "Falló en la transacción, lee bien la facking api prro";

        } catch (HibernateException e) {
            e.printStackTrace();
            mensaje = "Error en el Servidor alv (base de datos)";

        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Intentalo de nuevo más tarde Prro";

        } finally {

            if (session != null) {
                session.close();
            }

        }
        
        response = new HashMap<>();
        
        response.put("Codigo: ", codigoInt);
        response.put("Mensaje: ", mensaje);
        response.put("Data: ", null);
       
        return Response.status(status).entity(response).build();
    
    }

}
