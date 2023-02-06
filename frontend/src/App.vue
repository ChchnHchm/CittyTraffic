<script setup>
import Map from '@/components/Map.vue';
import menuBar from "@/components/MenuBar.vue";
import infoCard from "@/components/infoCard.vue";
import timeRange from  "@/components/timeRange.vue";
import datepicker from "@/components/datePicker.vue";
import {CityTrafficAPI} from "@/store/cityTrafficAPI.js";
</script>
<script>
///// TEST ///////

//let data = await CityTrafficAPI.getByDate("2022-10-12");
// let data =await CityTrafficAPI.getByHours("2022-10-12","8");
// let data =await CityTrafficAPI.getByRadarDate("P13","2022-10-12");
// let data =await CityTrafficAPI.getByRadarHours("P13","2022-10-12","8");

//console.log(data);
/////////////////

export default {
  name: 'App',
}
</script>

<template >
<link href="/dist/output.css" rel="stylesheet">
<div id="app">
  <menuBar />
  <div id="options">
    <datepicker   />
    <timeRange /> 
  </div>
  <div id="containerData" >
    <Map />
    <infoCard />
  </div>
</div>
</template>

<style >
@media (prefers-color-scheme: dark){

} 
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
  --main-bg-color: dark;
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
  padding-top: 1%;
  margin-top: 1%;
  justify-content: space-evenly;
  background-color: #191C24;
  align-items: center;
}  

</style>