<template>
  <div>
    <NavbarReturnHome></NavbarReturnHome>
    <br/>
    <div><h3>Pending requests</h3></div>
    <PendingRequests/>
    <br/>
    <Footer></Footer>
  </div>
</template>

<script>
// @ is an alias to /src
import NavbarReturnHome from '@/components/SmartComponents/NavbarReturnHome.vue';
import PendingRequests from "@/components/SmartComponents/PendingRequests";
import Footer from "@/components/SmartComponents/Footer";
export default {
  name: 'SelectPreferencesPage',
  components: {
    NavbarReturnHome,
    PendingRequests,
    Footer,
  }
}
</script>

<style scoped>
.h3{
  text-align: left;
}
</style>
