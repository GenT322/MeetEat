<template>
  <div>
    <NavbarReturnHome></NavbarReturnHome>
    <br/>
    <div class="container">
      <button class="btn btn-success" @click="createButton()">
        Add an offer
      </button>
    </div>    
    <div class="container">
      <h2>My ongoing offers :</h2>
    </div>
    <OnGoingOffers></OnGoingOffers>
    <div class="container">
      <h2>My previous offers :</h2>
    </div>
    <PreviousOffers></PreviousOffers>
    <br/>
    <Footer/>
  </div>
</template>

<script>
import NavbarReturnHome from "@/components/SmartComponents/NavbarReturnHome";
import OnGoingOffers from "@/components/SmartComponents/OnGoingOffers";
import PreviousOffers from "@/components/SmartComponents/PreviousOffers"//;
import Footer from "@/components/SmartComponents/Footer"
import router from "@/router";

export default {
  name: "CookOfferHistoryPage",
  components: {NavbarReturnHome, OnGoingOffers, PreviousOffers,Footer},

  methods : {
    createButton: function(){
        router.replace('/CreateOfferPage');
    },

},
}


</script>

<style scoped>

h2{
    text-align: left;
    margin-left: 70px;
    margin-bottom: 25px;
    font-weight: bold;
}

.btn{
  width: 500px;
  margin-top: 50px;
  margin-bottom: 60px;
}

.line-color {
    width: 70%;
    margin-top: 50px;
    margin-right: 5%;
    height: 1px;
    background-color: gray;
}

.line-color2 {
    width: 100%;
    margin-top: 50px;
    margin-right: -10%;
    /*height: 1px;*/
    background-color: rgb(150, 150, 150);
}

</style>