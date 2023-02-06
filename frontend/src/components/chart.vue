<template>
<div>
     <Line class="Chart" v-if="loaded" :data="chartData" :options="options" ref="line" /> 
</div>
</template>
 
<script>
import {Line} from "vue-chartjs";
import {option} from "@/store/chartConfig.js";
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
    name:'Chart',
    components:{
        Line,
    },
    props: ["data"], 
    data(){
        return{
            loaded: false,
            chartData: null,
            /*
            {
                labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
                datasets: [
                {
                    label: 'Data One',
                    backgroundColor: '#f87979',
                    data: [40, 39, 10, 40, 39, 80, 40]
                }]             
            },
            */
            options: option
        } 
    },
    async mounted(){
        
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