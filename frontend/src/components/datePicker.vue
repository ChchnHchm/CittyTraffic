<template>
<div id="datepicker">
    <Datepicker  v-model="date" @update:modelValue="handleDate"  />
</div>
</template>
 
<script>
import  {ref} from "vue";
import Datepicker from "@vuepic/vue-datepicker";
import '@vuepic/vue-datepicker/dist/main.css';
import {dateCounter} from '@/store/Date.js'; 

export default {
    name:'datepicker',
    components:{
        Datepicker,
    },
    setup(){
        const date = ref(new Date());
        const handleDate = (modelDate) => {
            date.value = modelDate;
            dateCounter.changeDate(modelDate);
        }
        return{ 
            date,
            handleDate
        }
  },
}
</script> 
 
<style>
#datepicker{
    
}  
</style> 