<template>
<div id="datepicker">
    <Datepicker v-model="date" />
</div>
</template>
 
<script>
import  {ref} from "vue";
import Datepicker from "@vuepic/vue-datepicker";
import '@vuepic/vue-datepicker/dist/main.css';

export default {
    name:'datepicker',
    components:{
        Datepicker,
    },
    setup(){
      const date = ref(new Date());
      return {date};  
    }
}
</script> 
 
<style>
#datepicker{
    
}  


</style> 