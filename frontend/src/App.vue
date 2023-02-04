<script setup>
import { RouterLink, RouterView } from "vue-router";
import HelloWorld from "./components/HelloWorld.vue";
import Map from '@/components/Map.vue';
import menuBar from "@/components/MenuBar.vue";
import infoCard from "@/components/infoCard.vue";
import timeRange from  "@/components/timeRange.vue";
import datepicker from "@/components/datePicker.vue";
</script>
<script>
import { CityTrafficAPI } from "@/store/cityTrafficAPI.js";
await CityTrafficAPI.getByDate("2022-10-12");

</script>

<template >
<link href="/dist/output.css" rel="stylesheet">
<div id="app">
  <menuBar />
  <div id="options">
    <datepicker />
    <timeRange />
  </div>
  <div id="containerData" >
    <Map />
    <infoCard />
  </div>
</div>
</template>

<style >
html.dark{
  color-scheme: dark;
  background-color: #000000;
}
#app{
  display: grid;
  position: relative;
  justify-content: space-between;
}  
:root {
    --primary: #EB1616;
    --secondary: #191C24;
    --light: #6C7293;
    --dark: #000000;
}
#containerData{
  display: flex;
  padding-top: 1%;
  border: none;
  margin-top: 1%;
}
#options{
  display: flex;
  padding-top: 2%;
  margin-top: 2%;
  justify-content: space-evenly;
  background-color: #191C24;
  align-items: center;
}  

</style>