<template>
<div>

    <h1>Previous orders</h1>
    <br/>
    <br/>
    <div v-for="order in orders.reservations" :key="order.id" style="margin-bottom: 30px; padding-left: 30px; padding-right: 30px;" >
      <div class="row">
        <div class="col-sm">
          <img v-bind:src="order.offer.offerImage" height="200px">
        </div>
        <div class="col-sm" style="text-align: left">
          <div class="row">
            <div class="col-sm-6">
              <b>{{order.offer.offerTitle}}</b> <br/>

            </div>
            <div class="col-sm-4">
              <b>{{order.reservationDate}}</b> <br/>
            </div>
          </div>
          <p>
            {{order.offerDetails}} <br/>
          </p>
          <div class="row">
            <div class="col-sm">
              {{order.nbOfPortion}} portions
            </div>
            <div class="col-sm">
              {{order.price.toFixed(2)}}$
            </div>
          </div>
          <h4 v-if="order.state === 'purchasedMeal'" style="color: green">Passed</h4>
          <h4 v-if="order.state === 'reservation'"  style="color: green">Accepted</h4>

        </div>

        <div class="col-sm-1">
          <img v-bind:src="order.cook.cookPhoto" width="100px" height="80px"/>
          <h5 style="text-align: left">{{ order.cook.cookFirstName }} {{ order.cook.cookLastName }}</h5>
          <p style="text-align: left"> {{order.offer.offerAddress}}</p>
        </div>


        <div class="col-sm">
            <template v-if="!order.evaluatedByCustomer">
              <button class="btn btn-success" @click="evaluate(order.id)" style="margin-bottom: 10px;"> Evaluate </button>
              <br/>
            </template>
          <button class="btn btn-danger" @click="report()"> Report </button>
        </div>
      </div>
      <hr class="my-4"/>


    </div>
</div>
</template>

<script>
import router from "@/router";
import {urlAPI} from "@/variables";
import axios from "axios";

export default {
  name: "Historic",
  data() {
    return {
      orders : [],
      user: null,

    }
  },

  methods : {
    evaluate : function (id)
    {
      console.log('evaluate');
      sessionStorage.setItem("idEvaluate", id);
      router.replace('/evaluatemeal');
    },

    report : function ()
    {

      router.replace('/reportcook');

    },
  },

  async mounted() {
    this.user = JSON.parse(sessionStorage.getItem("user"));
    this.user = this.user.user;
    //viewOffersHistory
    await axios.get(urlAPI + "todo=viewPurchasedMeals")
        .then(response => (this.orders = response.data));

    console.log(this.orders);
  }


}
</script>

<style scoped>

</style>