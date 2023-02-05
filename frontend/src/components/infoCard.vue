<script setup>
import {dateCounter}  from "@/store/Date.js"
import {HourCounter}  from "@/store/Hour.js"
import {RadarCounter}  from "@/store/Radar.js"
import {CityTrafficAPI} from "@/store/cityTrafficAPI.js";
</script>


<template  :key="RadarCounter.name">
<div id="InfoCard"  :key="dateCounter.date">
    <div id="infos" :key="HourCounter.Hour">
        <div id="spec">
            <p>{{dateCounter.date.toDateString()}}</p>
            <p> {{ HourCounter.Hour}} H</p>
            <p>Radar : {{RadarCounter.name}} </p>
        </div>
    </div>
</div>
</template>

<script>
async function changeData(){
    const dateFormat = dateCounter.date.getFullYear()+ "-" + (dateCounter.date.getMonth()+1) + "-" + dateCounter.date.getDate();
    let data = await CityTrafficAPI.getByRadarDate(RadarCounter.name,dateFormat);
    if(data.length == 0){
        if(document.getElementById("noData") == null){
            let noDataDiv = document.createElement("div");
            noDataDiv.setAttribute("id","noData");
            noDataDiv.setAttribute("class","noData");
            noDataDiv.setAttribute("style","display:inline-block; margin: 0 auto; padding: 3px; background-color: #2a2f3c; margin-top: 10%; font-style: italic;");
            noDataDiv.innerHTML = "No data for this specified Spec.";
            document.getElementById("infos").appendChild(noDataDiv); 
        }
        return;
    }
    console.log(data);
}

export default {
    name: "infoCard",
    async mounted(){
        await changeData();
    },
    async beforeUpdate(){
        await changeData();
    } 
}
</script>

<style scoped>
#InfoCard{
    width: 50vw;
    border-radius: 25px;
    overflow-y: scroll;
    margin-left: 0.5%;
    background-color: #191C24;
}
#infos{
    display: grid;
    grid-template-columns: 1fr;
    margin-left: 2%;
}

#noData{
    display: inline-block;
    margin: 0 auto;
    margin-top: 10%;
    padding: 3px;
    background-color: #8ebf42;
}
.noData{
    display: inline-block;
    margin: 0 auto;
    padding: 3px;
    background-color: #8ebf42;
} 
#spec{
    border-radius: 10px;
    display: flex;
    justify-content: space-evenly;
    white-space: nowrap;
    background-color:#2a2f3c;
    padding-top: 1%;
    margin-top: 1%;
}  

</style>