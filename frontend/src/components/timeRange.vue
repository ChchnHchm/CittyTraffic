<template>
<div id="RangeSlider">
  <p>Time :  {{range}} H</p>
  <div id="inputRange">
  <input v-model="range" type="range" min="0" max="23" class="slider" id="Range">
  <input type="reset" @click="resetTime"   />
  </div>
</div>
</template>
 
<script>
import  {ref} from "vue";

export default {
    name:'timeRange',
    props: ['range'], 
    setup(){
      const range = ref(new Date().getHours());
      return {range};  
    },
    methods:{
      resetTime(){
        document.getElementById("Range").value = new Date().getHours();
        this.range = new Date().getHours()
      }
    } 
}
</script> 
 
<style>

#RangeSlider{
    width: 20vh;
}

#Range{
  width: 100%;
  height: 10px;
  background: #d3d3d3;
  opacity: 0.7;
  transition: opacity .2s;
} 
#inputRange{
  display: flex;
}  

</style> 