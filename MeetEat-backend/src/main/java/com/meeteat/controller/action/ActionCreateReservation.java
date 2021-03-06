/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meeteat.controller.action;

import com.meeteat.model.Offer.Offer;
import com.meeteat.model.Offer.Reservation;
import com.meeteat.model.Offer.ReservationState;
import com.meeteat.model.User.User;
import com.meeteat.service.Service;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author yousr
 */

public class ActionCreateReservation extends Action{
    @Override
    public void executer(HttpServletRequest request){
        Service service = new Service();
        HttpSession session = request.getSession();
        Long customerId = (Long)session.getAttribute("userId");
        User customer = service.findUserById(customerId);
        Long offerId = Long.parseLong(request.getParameter("offerId"));
        Offer offer = service.findOfferById(offerId);
        Date reservationDate = new Date();
        Integer nbOfPortions = Integer.parseInt(request.getParameter("nbOfPortions"));
        Reservation reservation=null;
        if (customer!=null && nbOfPortions<=offer.getRemainingPortions()){
            reservation=new Reservation(reservationDate, ReservationState.REQUEST, nbOfPortions, offer, customer);
            service.createReservation(reservation);
        } else if (customer==null){
            //errorCode=1 -> not connected
            request.setAttribute("errorCode", 1);
        } else if (nbOfPortions>offer.getRemainingPortions()){
            //errorCode=2 -> not enough remaining portions
            request.setAttribute("errorCode", 2);
        }
        request.setAttribute("reservation",reservation);
    }
    
}
