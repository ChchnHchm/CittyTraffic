<script setup>
import { RouterLink, RouterView } from "vue-router";
import HelloWorld from "./components/HelloWorld.vue";
import Map from '@/components/Map.vue';
import menuBar from "@/components/MenuBar.vue";
import infoCard from "@/components/infoCard.vue";
</script>

<template >
<link href="/dist/output.css" rel="stylesheet">
<div id="app">
  <menuBar />
  <div id="containerData" >
    <Map />
    <infoCard />
  </div>
</div>
</template>

<style >
html.dark{
  color-scheme: dark;
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
  padding-top: 2%;
  border: none;
  margin-top: 2%;
} 

</style>