<template>
<div>
     <Bar class="Chart" :id="id" v-if="loaded" :data="chartData" :options="options" ref="bar" /> 
</div>
</template>
 
<script>
import {Bar} from "vue-chartjs";
import {option} from "@/store/Barchartconfig.js";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  TimeScale,
  BarElement
} from 'chart.js'
import 'chartjs-adapter-luxon';
ChartJS.register(
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend,
  BarElement
)

async function initData(data){
 return {
                label: data[1],
                data: data[0],
                backgroundColor: '#f87979',
                showLine: false
        };
}

export default {
    name:'BarChart',
    components:{
        Bar,
    },
    props: ["data"], 
    data(){
        return{
            loaded: false,
            chartData: null,
            options: option,
            id: this.data[1],
        } 
    },
    async mounted(){
        console.log(this.data[0]);
        if(this.data[2] == "all"){
        }else{
            this.options.scales.x.time.unit = this.data[2];
        } 
        this.chartData = {
            datasets: [
                await initData(this.data),
            ]  
        }  

        this.loaded = true;
    } 
}
</script> 
 
<style>

</style> 