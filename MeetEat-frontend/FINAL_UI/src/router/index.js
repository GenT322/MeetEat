import Vue from 'vue'
import VueRouter from 'vue-router'
import "chart.js/auto";
import 'chart.js';
import axios from 'axios'
import Home from "@/views/Home";
import OrderPage from "@/views/OrderPage";
import OfferPage from "@/views/OfferPage";
import Login_page from "@/views/Login_page";

axios.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem("auth-token")}`;
axios.interceptors.response.use(response => response, error => {
  if (error.response || error.response.status == 401) {
    localStorage.removeItem('username');
    localStorage.removeItem('auth-token');
    router.replace("/");
  }
});

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },

  {
    path: '/orderPage',
    name: 'OrderPage',
    component: OrderPage
  },
  {
    path: '/orderPage/:id',
    name: 'OfferPage', //même nom que la view
    component: OfferPage //composant de la dossier vue
  },
  {
    path: '/login',
    name: 'Login_page', //même nom que la view
    component: Login_page //composant de la dossier vue
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


export default router