<template>
<div>
     <Line class="Chart" :id="id" v-if="loaded" :data="chartData" :options="options" ref="line" /> 
</div>
</template>
 
<script>
import {Line} from "vue-chartjs";
import {option} from "@/store/linechartConfig.js";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  TimeScale
} from 'chart.js'
import 'chartjs-adapter-luxon';
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  TimeScale
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
    name:'lineChart',
    components:{
        Line,
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
        this.options.scales.x.time.unit = this.data[2];
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