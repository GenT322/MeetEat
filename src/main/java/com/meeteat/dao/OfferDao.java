/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meeteat.dao;

import com.meeteat.model.Offer.Offer;
import javax.persistence.EntityManager;

/**
 *
 * @author gvnge
 */
public class OfferDao {
    public void create(Offer offer) {
        EntityManager em = JpaTool.obtainPersistenceContext();
        em.persist(offer);
    }
    
    public void merge(Offer offer){
        EntityManager em = JpaTool.obtainPersistenceContext();
        em.merge(offer);
    }
    
    public Offer searchById(Long offerId) {
        EntityManager em = JpaTool.obtainPersistenceContext();
        return em.find(Offer.class, offerId); // renvoie null si l'identifiant n'existe pas
    }
}