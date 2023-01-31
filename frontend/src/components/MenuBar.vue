<script setup>
import {useDark, useToggle} from "@vueuse/core";

const isDark = useDark();
const toggleDark = useToggle(isDark);
</script>

<template>
  <header id="headerBar" class="flex flex-row box-border">
  <div id="logoBar">
    <img id="logoHeader" src="/src/assets/logoTrasnparent.png" alt="logo">
  </div>
  <div id="DateBar">
   {{DateBar}}  
  </div>
  <div id="ToggleBar">
    <label class="switch">
    <input type="checkbox" @click="toggleDark()" checked>
    <span class="slider round"></span>
    </label>
  </div>
  </header>
</template>

<script>
export default {
    name: "menuBar",
    data(){
        return {
            DateBar: new Date(),
        } 
    }
}
</script>
<style scoped>
#headerBar{
  width: 100vw;
  justify-content: space-between;
  background-color: #6C7293;
  white-space: nowrap;
  align-items: center;
  border-block-width: 1px;
    border-color: grey;
}
#logoBar{
       padding:10px; 
}  

#ToggleBar{
    padding:10px;
}  
#logoHeader{
    margin: auto;
    width: 40%;
    height: 100%;
}
#DateBar{
    display: inline-block;
}
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

.switch input { 
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  background-image: url("@/assets/brightness.png");;
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}
.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

</style>