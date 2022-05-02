/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meeteat.DBPopulation;
import com.github.javafaker.Address;
import com.github.javafaker.Country;
import com.github.javafaker.Faker;
import com.github.javafaker.Food;
import com.github.javafaker.Name;
import com.github.javafaker.DateAndTime;
import com.github.javafaker.RickAndMorty;
import com.github.javafaker.Number;
import com.meeteat.dao.JpaTool;
import com.meeteat.model.Offer.Offer;
import com.meeteat.model.Preference.Cuisine;
import com.meeteat.model.Preference.Diet;
import com.meeteat.model.Preference.Ingredient;
import com.meeteat.model.Preference.PreferenceTag;
import com.meeteat.model.User.Cook;
import com.meeteat.model.User.User;
import com.meeteat.model.VerificationRequest.CookRequest;
import com.meeteat.service.Service;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
/**
 *
 * @author ithan
 */
public class DBPopulation {
    Faker faker;
    Service service;
    LinkedList<Long> userIdList = new LinkedList<>();
    LinkedList<Long> cookIdList = new LinkedList<>();
    LinkedList<Ingredient> ingredientsList = new LinkedList<>();
    LinkedList<Diet> dietList = new LinkedList<>();
    LinkedList<Cuisine> cuisineList = new LinkedList<>();
    LinkedList<CookRequest> cookRequestList = new LinkedList<>();
    Locale locale = new Locale("fr");
    int nbProfilePictures = 20;
    int nbOfferPictures = 20;
    public DBPopulation(){
        faker = new Faker(locale);
        service = new Service();
    }
    
    public void createUsers(int nbUsers){
        System.out.println("creatin users...");
        for(int i = 0; i<nbUsers; i++){
            String email = faker.internet().emailAddress();
            String profilePhoto = faker.internet().image();
            String payementInfo = faker.crypto().sha1();
            Address address = faker.address();
            Name name = faker.name();
            String phone = faker.phoneNumber().cellPhone();
            User user = new User(name.firstName(), name.lastName(), address.streetAddress(), address.city(), address.zipCode(), phone, email);
            user.setProfilePhotoPath("./Images/profile_images/profile" + (i%nbProfilePictures + 1));
            userIdList.add(service.createAccount(user));
        }
    }
    
    public void createCooks(int nbCooks){
        assert(nbCooks < userIdList.size());
        System.out.println("creatin cooks...");
        for(int i = 0; i<nbCooks; i++){
            User user = service.findUserById(userIdList.get(i));
            DateAndTime dat = faker.date();
            Date validationDate = dat.birthday(0, 2);
            Cook cook = new Cook(user, validationDate, faker.number().numberBetween(1, 6), faker.university().name(), faker.job().keySkills());
            cookIdList.add(service.approveCook(cook));
        }
    }
    
    public void createIngedients(int nbIngredients){
        System.out.println("creatin ingredients...");
        for(int i =0; i<(nbIngredients/2); i++){
            Food food = faker.food();
            Ingredient ingredient = new Ingredient(food.ingredient());
            service.createPreferenceTag(ingredient);
            ingredientsList.add(ingredient);
        }
        for(int i =0; i<(nbIngredients/2); i++){
            Food food = faker.food();
            Ingredient ingredient = new Ingredient(food.spice());
            service.createPreferenceTag(ingredient);
            ingredientsList.add(ingredient);
        }
    }
    
    public void createDiets(){
        System.out.println("creatin diets...");
        dietList.add(new Diet("Vegetarian"));
        dietList.add(new Diet("Vegan"));
        dietList.add(new Diet("Pesco Vegetarian"));
        dietList.add(new Diet("Diary-free"));
        dietList.add(new Diet("Gluten free"));
        dietList.add(new Diet("No pork"));
        for(Diet diet : dietList){
            service.createPreferenceTag(diet);
        }
    }
    
    public void createCuisines(int nbCuisines){
        System.out.println("creatin cuisines...");
        for(int i = 0; i<nbCuisines; i++){
            Country country = faker.country();
            String name = country.name();
            Cuisine cuisine = new Cuisine(name);
            service.createPreferenceTag(cuisine);
            cuisineList.add(cuisine);
        }
    }
    
    public void createOffers(int nbOffers){
        int min = 0;
        System.out.println("creatin offers...");
        for(int i = 0; i<nbOffers; i++){
            Number number = faker.number();
            Address address = faker.address();
            List<Ingredient> ingredients = getIngredientsForOffer();
            List<PreferenceTag> classifications = getPreferenceTagForOffer();
            Cook cook = service.findCookById(cookIdList.get(number.numberBetween(min, cookIdList.size())));
            RickAndMorty ram = faker.rickAndMorty();
            DateAndTime dat = faker.date();
            Date creationDate = dat.birthday(0, 2);
            String title = ram.character();
            double price = number.randomDouble(2, 0, 20);
            int totalPortions = number.numberBetween(0, 30);
            String details = ram.quote();
            Offer offer = new Offer(cook, creationDate, title, price, totalPortions, 
                                    details, classifications, ingredients, "", address.streetAddress(), address.city(), 
                                    address.zipCode());
            offer.setOfferPhotoPath("./Images/profile_images/meal" + (i%nbOfferPictures + 1));
            System.out.println(address.streetAddress());
            service.makeOffer(offer);
            //offer.setIngredients(ingredients);
            for(Ingredient ing : ingredients){
                //System.out.println(ing);
            }
            //offer.setClassifications(classifications);
            for(PreferenceTag ing : classifications){
                //System.out.println(ing);
            }
            //service.updateOffer(offer);
        }
    }
    
    public void populateDatabase(int nbUsers, int nbCooks, int nbIngredients, int nbCuisines, int nbOffers){
        JpaTool.init();
        createUsers(nbUsers);
        createCooks(nbCooks);
        createIngedients(nbIngredients);
        createDiets();
        createCuisines(nbCuisines);
        createOffers(nbOffers);
        JpaTool.destroy();
    }
    private List<Ingredient> getIngredientsForOffer(){
        int min = 0;
        int max = ingredientsList.size();
        Number number = faker.number();
        int lower = number.numberBetween(min, max);
        int upper = number.numberBetween(lower, max);
        List<Ingredient> ll = new LinkedList<>();
        for(int i = lower; i<upper; i++){
            ll.add(ingredientsList.get(i));
        }
        return ll;
    }
    
    private List<PreferenceTag> getPreferenceTagForOffer(){
        Number number = faker.number();
        int nbCuisines = number.numberBetween(0, cuisineList.size());
        int nbDiets = number.numberBetween(0, dietList.size());
        List<PreferenceTag> ll = new LinkedList<>();
        int startCuisine = number.numberBetween(0, cuisineList.size());
        while((startCuisine + nbCuisines) >= cuisineList.size()){
            startCuisine = number.numberBetween(0, cuisineList.size());
        }
        int startDiet = number.numberBetween(0, dietList.size());
        while((startDiet + nbDiets) >= dietList.size()){
            startDiet = number.numberBetween(0, dietList.size());
        }
        for(int i = startCuisine; i<(startCuisine + nbCuisines); i++){
            ll.add(cuisineList.get(i));
        }
        for(int i = startDiet; i<(startDiet + nbDiets); i++){
            ll.add(dietList.get(i));
        }
        return ll;
    }
    
    public static void main(String [] args){
        DBPopulation dbp = new DBPopulation();
        dbp.populateDatabase(100, 30, 200, 20, 30);
    }
}
