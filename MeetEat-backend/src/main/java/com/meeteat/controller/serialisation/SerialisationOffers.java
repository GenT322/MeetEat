/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meeteat.controller.serialisation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.meeteat.model.Offer.Offer;
import com.meeteat.model.Preference.Cuisine;
import com.meeteat.model.Preference.Diet;
import com.meeteat.model.Preference.Ingredient;
import com.meeteat.model.Preference.PreferenceTag;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.PriorityQueue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gvnge
 */
public class SerialisationOffers extends Serialisation{
    
    @Override
    public void serialise(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject container = new JsonObject();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        List <Offer> offers = (List <Offer>)request.getAttribute("offers");
        if (offers==null || offers.isEmpty()){
            container.addProperty("hasResults",false);
        } else{
            container.addProperty("hasResults",true);
            JsonArray jsonOfferList = new JsonArray();
            offers.stream().map(offer->{
                JsonObject jsonOffer = new JsonObject();
                jsonOffer.addProperty("id",offer.getId());
                JsonObject jsonCook = new JsonObject();

                jsonCook.addProperty("id",offer.getCook().getId());
                jsonCook.addProperty("firstName",offer.getCook().getFirstName());
                jsonCook.addProperty("lastName",offer.getCook().getLastName());
                jsonCook.addProperty("rating",offer.getCook().getRating());
                jsonCook.addProperty("numberOfReviews", offer.getCook().getNumberOfReviews());
                jsonCook.addProperty("image", offer.getCook().getUser().getProfilePhotoPath());

                jsonOffer.add("cook",jsonCook);
                if (offer.getPublicationDate()!=null){
                jsonOffer.addProperty("publicationDate",df.format(offer.getPublicationDate()));
                }
                if (offer.getExpirationDate()!=null){
                    jsonOffer.addProperty("expirationDate",df.format(offer.getExpirationDate()));
                }
                if (offer.getAvailableFrom() != null) {
                    jsonOffer.addProperty("availableFrom", df.format(offer.getAvailableFrom()));
                }
                jsonOffer.addProperty("title",offer.getTitle());
                jsonOffer.addProperty("price",offer.getPrice());
                jsonOffer.addProperty("totalPortion",offer.getTotalPortions());
                jsonOffer.addProperty("details",offer.getDetails());

                JsonArray jsonCuisineList = new JsonArray();
                JsonArray jsonDietList = new JsonArray();
                List<PreferenceTag> classifications = offer.getClassifications();
                classifications.forEach(classification -> {
                    JsonObject jsonPref = new JsonObject();
                    jsonPref.addProperty("id", classification.getId());
                    jsonPref.addProperty("name", classification.getName());
                    if (classification instanceof Cuisine){
                        jsonCuisineList.add(jsonPref);
                    } else if (classification instanceof Diet){
                        jsonDietList.add(jsonPref);
                    }
                });
                jsonOffer.add("cuisines",jsonCuisineList);
                jsonOffer.add("diets",jsonDietList);

                JsonArray jsonIngredientList = new JsonArray();
                List<Ingredient> ingredients = offer.getIngredients();
                ingredients.stream().map(ingredient -> {
                    JsonObject jsonPref = new JsonObject();
                    jsonPref.addProperty("id", ingredient.getId());
                    jsonPref.addProperty("name", ingredient.getName());
                    return jsonPref;
                }).forEachOrdered(jsonPref -> {
                    jsonIngredientList.add(jsonPref);
                });
                jsonOffer.add("ingredients",jsonIngredientList);

                jsonOffer.addProperty("specifications", offer.getSpecifications());
                jsonOffer.addProperty("address", offer.getAddress());
                jsonOffer.addProperty("remainingPortions",offer.getRemainingPortions());
                jsonOffer.addProperty("city", offer.getCity());
                jsonOffer.addProperty("zipCode", offer.getZipCode());
                jsonOffer.addProperty("distanceToUser", offer.getDistanceToUser());
                jsonOffer.addProperty("image",offer.getOfferPhotoPath());
                jsonOffer.addProperty("state",offer.getState().name());
                jsonOffer.addProperty("soldPortions",offer.getTotalPortions()-offer.getRemainingPortions());

                return jsonOffer;
            }).forEachOrdered(jsonOffer -> {
                jsonOfferList.add(jsonOffer);
            });
            
            container.add("offers", jsonOfferList);
        }
        try (PrintWriter out = this.getWriter(response)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            gson.toJson(container,out);
        }
    }
}
