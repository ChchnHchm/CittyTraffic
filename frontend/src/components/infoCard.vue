<script setup>
import {dateCounter}  from "@/store/Date.js"
import {HourCounter}  from "@/store/Hour.js"
import {RadarCounter}  from "@/store/Radar.js"
import {CityTrafficAPI} from "@/store/cityTrafficAPI.js";
import lineChart from "@/components/chart.vue";
import  BarChart from "@/components/Barchart.vue";
import { createApp, h } from "vue"; 
</script> 


<template  :key="RadarCounter.name">
<div id="InfoCard"  :key="dateCounter.date">
    <div id="infos" :key="HourCounter.Hour">
        <div id="spec">
            <p>{{dateCounter.date.toDateString()}}</p>
            <p> {{ HourCounter.Hour}} H</p>
            <p>Capteur : {{RadarCounter.name}} </p>
        </div>
        <div id="ButtonsOptions">
            <button class="buttonChange" type="button" @click="changeData(`day`)" >Day</button>
            <button class="buttonChange" type="button" @click="changeData(`hour`)">Hour</button>
            <button class="buttonChange" type="button" @click="changeData(`all`)">ALL Radar</button>
        </div>
    </div>
</div>
</template>

<script>
async function changeData(displayType){
    document.querySelectorAll(".temp").forEach(el => el.remove());
    const dateFormat = dateCounter.date.getFullYear()+ "-" + (dateCounter.date.getMonth()+1) + "-" + dateCounter.date.getDate();
    let data = null;
    if(displayType == "day"){
        //données par jours
        //data = await CityTrafficAPI
        data = await CityTrafficAPI.getByDate(dateFormat);
    }else if(displayType == "all"){
        //données pour touts les radar
        data = await CityTrafficAPI.getByHours(dateFormat,HourCounter.Hour);
    }else{
        //données sur 24H
        data = await CityTrafficAPI.getByRadarDate(RadarCounter.name,dateFormat);
    }

    if(data.length == 0){
        if(document.getElementById("noData") == null){
            let noDataDiv = document.createElement("div");
            noDataDiv.setAttribute("id","noData");
            noDataDiv.setAttribute("class","temp");
            noDataDiv.setAttribute("style","display:inline-block; margin: 0 auto; padding: 3px; background-color: #2a2f3c; margin-top: 10%; font-style: italic;");
            noDataDiv.innerHTML = "No data for this specified Spec.";
            document.getElementById("infos").appendChild(noDataDiv); 
        }
        return;
    }
    let div = document.getElementById("noData");
    if(div){
        div.remove();
    } 
    let nbEnterDay = 0;
    let nbExitDay = 0;
    let mapDataHour = new Map();
    let divEnter = document.createElement("div");
    divEnter.setAttribute("class","temp");
    divEnter.setAttribute("style","display:inline-flex; padding: 3px; background-color: #2a2f3c;  width:10vw; text-align: center;");

    let divExit = document.createElement("div");
    divExit.setAttribute("class","temp");
    divExit.setAttribute("style","display:inline-flex; padding: 3px; background-color: #2a2f3c; width:10vw; text-align: center;");

    await data.forEach(value =>{
        const key = dateCounter.date.getFullYear()+ "-" + (dateCounter.date.getMonth()+1) + "-" + dateCounter.date.getDate() +","+HourCounter.Hour+","+RadarCounter.name;
        const keyDate = value.key.split(',');
        const type = value.column.split(':');
        if(keyDate[1] == HourCounter.Hour ){
            if(value.column == "direction:enter"){
                divEnter.innerHTML = value.$ + "  Véhicule entrant ";
            }else if (value.column == "direction:exit"){
                divExit.innerHTML = value.$ + "  Véhicule sortant ";
            }
        }
        if(value.column == "direction:enter"){
            try {
                nbEnterDay = nbEnterDay + parseInt(value.$,10);
            } catch (error) {
                console.error(error);
            }
        }else if (value.column == "direction:exit"){
            try {
                nbExitDay = nbExitDay + parseInt(value.$,10);
            } catch (error) {
                console.error(error);
            }
        }
        if(mapDataHour.has(type[1])){
            let dataset = mapDataHour.get(type[1]);
            if(displayType == "all"){
                dataset.push({x:keyDate[2],y:value.$});
            }else{
                dataset.push({x:convertDate(keyDate),y:value.$});
            }
        }else{
            mapDataHour.set(type[1],[]);
            let dataset = mapDataHour.get(type[1]);
            if(displayType == "all"){
                dataset.push({x:keyDate[2],y:value.$});
            }else{
                dataset.push({x:convertDate(keyDate),y:value.$});
            }
        }
    });
    let divInfos = document.createElement("div");
    divInfos.appendChild(divEnter);
    divInfos.appendChild(divExit);
    divInfos.setAttribute("style","display:flex; justify-content: space-evenly;");
    divInfos.setAttribute("class","temp");
    document.getElementById("infos").appendChild(divInfos);
    mapDataHour.forEach((val,key) => {
        let componentsApp = createApp({
            setup(){
                if(displayType == "all"){
                    return () => h(BarChart, {data: [val,key,displayType] },"")
                }
                return () => h(lineChart, {data: [val,key,displayType] },"")
            } 
        });
        const wrapper = document.createElement("div");
        wrapper.setAttribute("class","temp");
        wrapper.setAttribute("style","display:inline-block; padding: 3px; background-color: #2a2f3c; margin-top: 10%; margin-right: 1%; ");
        componentsApp.mount(wrapper);
        document.getElementById("infos").appendChild(wrapper);
    });
}
function convertDate(date){
    let mydate = date[0].split('-');
    let d = new Date(mydate[0], mydate[1]-1, mydate[2]);
    d.setHours(date[1]);
    return d;
} 
export default {
    name: "infoCard",
    async mounted(){
        await changeData("hour");
    },
    async beforeUpdate(){
        await changeData("hour");
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
    overflow-y: scroll;
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
    margin-right: 1%;
}
.buttonChange{
    background-color: #2a2f3c; color: black;
    border: none;
    color: white;
    padding: 15px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    cursor: pointer;
} 
.divInfodirection{
    display: inline-block;
    padding: 200px;
}
#ButtonsOptions{
    display: flex;
    justify-content: center;
}  
</style>