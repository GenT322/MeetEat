/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meeteat.dao;

import com.meeteat.model.Offer.Reservation;
import com.meeteat.model.Offer.ReservationState;
import com.meeteat.model.User.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author gvnge
 */
public class ReservationDao extends AbstractDao<Reservation> {
    public ReservationDao() {
        super(Reservation.class);
    }
    
    public List<Reservation> searchPurchasedMeals(User user){
        EntityManager em = JpaTool.obtainPersistenceContext();
        // a changer mais on a ajoute aussi les reservations a l etat reservation (acceptée)
        String jpql="select r from Reservation r where r.customer= :customer and (r.state = :purchasedState OR r.state = :reservationState)";
        TypedQuery query=em.createQuery(jpql, Reservation.class);
        query.setParameter("customer",user);
        query.setParameter("purchasedState",ReservationState.PURCHASEDMEAL);
        query.setParameter("reservationState",ReservationState.RESERVATION);
        List<Reservation> purchasedMeals = query.getResultList();
        return purchasedMeals;
    }
    public List<Reservation> getReservationsListByUserId(Long userId){
        EntityManager em = JpaTool.obtainPersistenceContext();
        String jpql="select r from Reservation r where r.customer.id= :userId";
        TypedQuery query=em.createQuery(jpql, Reservation.class);
        query.setParameter("userId",userId);
        List<Reservation> reservations = query.getResultList();
        return reservations;
    }
    public List<Reservation> getReservationsRequests(Long cookId){
        EntityManager em = JpaTool.obtainPersistenceContext();
        String jpql="select r from Reservation r where r.offer.cook.id= :cookId AND r.state = :state";
        TypedQuery query=em.createQuery(jpql, Reservation.class);
        query.setParameter("cookId",cookId);
        query.setParameter("state",ReservationState.REQUEST);
        List<Reservation> reservations = query.getResultList();
        return reservations;
    }
}
