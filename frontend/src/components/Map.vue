<template>
<div>
  <div id="capteurs">
    <div id="sensorButon">
      <div>Capteurs</div>
      <img id="SensorDiplay" src="/src/assets/hide.png" alt="Hide" @click="diplayALLMarker">
    </div>
    <div id="sensorsDiv" v-for="marker, index in markersName">
      <img id="sensors" v-bind:src="`/src/assets/${marker}.png`" v-bind:alt=index  @click="displayMarker">
      <div id="sensorName">{{marker}}  {{index+1}} </div>
    </div>
  </div>
  <div id="map">
    <div style="width: 50vw; height: 80vh">
      <l-map ref="map" :zoom="zoom" :center="center" :options="mapOptions" @update:zoom="zoomUpdate" :minZoom="minZoom"  :maxZoom="maxZoom" :bounds="bounds" :max-bounds="maxBounds">
        <l-tile-layer
          :url="url"
          layer-type="base"
          name="OpenStreetMap"
          :attribution="attribution"
        ></l-tile-layer>
        <l-control-zoom positions="bottomright" ></l-control-zoom>
        <l-marker class="markerDiv" v-for="marker, index in markersCoord" :lat-lng="marker" @click="showDetails" >
          <LTooltip >    </LTooltip>
        </l-marker>
        <l-polyline :lat-lngs="markersCoord" color="green" />
      </l-map>
    </div>
  </div>
</div>
</template>

<script>
import "leaflet/dist/leaflet.css";
import { LMap, LTileLayer, LControlZoom,LMarker, LTooltip, LPolyline } from "@vue-leaflet/vue-leaflet";
import {latLngBounds , latLng} from "leaflet"; 
import {markersCoord, markersName, marker,  markerMap}  from "@/data/data.js";
import { CityTrafficAPI }  from "@/store/cityTrafficAPI.js";
import  { RadarCounter } from "@/store/Radar.js";


export default {
  name: "Map",
  components: {
    LMap,
    LTileLayer,
    LControlZoom,
    LMarker,
    LTooltip,
    LPolyline
  },
  mounted(){
  },
  data() {
    return {
      minZoom: 14,
      maxZoom: 18,
      url:"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
      center: [44.80081916928184, -0.6094419612199954],
      bounds: latLngBounds([
        [44.8106985806742, -0.5564507584073208],
        [44.80460909428135, -0.6545552385142254]
      ]
      ),
      maxBounds:latLngBounds([
        [44.8544588480959, -0.594160200952336], 
        [44.8106985806742, -0.5564507584073208],
        [44.77195427124244, -0.6182786238527068], 
        [44.80460909428135, -0.6545552385142254]  
      ]),
      zoomControl: true,
      zoom: 14,
      currentZoom:2,
      mapOptions: {
        zoomControl: false,
        zoomSnap: 0.5
      },
      attribution:'&copy; <a target="_blank" href="http://osm.org/copyright">OpenStreetMap</a> contributors',
      markersCoord: Array.from(markersCoord),
      markersName:markersName,
      markerID: Array.from(marker),
    };
  },
  methods:{
    zoomUpdate(zoom){
      this.currentZoom  = zoom;
    },
    async showDetails(event){
      const str = event.latlng.lat+ ", " + event.latlng.lng;
      RadarCounter.changeName(markerMap.get(str));
    },
    diplayALLMarker(){
      let mode = document.getElementById("SensorDiplay").alt;
      if(mode === "Hide"){
        document.getElementById("SensorDiplay").alt = "Display";
        document.getElementById("SensorDiplay").src = "/src/assets/show.png";
        this.markersCoord = [];
      }else{
        document.getElementById("SensorDiplay").alt = "Hide";
        document.getElementById("SensorDiplay").src = "/src/assets/hide.png";
        this.markersCoord = Array.from(markersCoord);
      }
    },
    displayMarker(event){
      if(this.markersCoord.includes(markersCoord[event.target.alt])){
          const index = this.markersCoord.indexOf(markersCoord[event.target.alt]);
          this.markersCoord.splice(index,1);        
      }else{
        this.markersCoord.push(markersCoord[event.target.alt]);
      }
    },
  }
};
</script>

<style scoped>
#map{
  box-sizing: border-box;
  margin-right: 0.5%;
  border-radius: 25px;
}

#capteurs{
  border-radius: 25px;
  width: 50vw;
  height: 7vw;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
  background-color: #191C24;
  margin-bottom: 2%;
}

#sensorsDiv{
  margin-top: auto;
  display: inline-block;
  justify-content: space-between;
  align-items: center;
}
#SensorDiplay{
  cursor: pointer;
  width: 30px;
  height: 30px;
} 

#sensors{
  cursor: pointer;
  width: 30px;
  height: 30px;
  margin: 10px;
} 

#sensorButon{
  margin-left: 25px;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-row: 3;
  grid-column: 1;
  white-space: nowrap;
}
#sensorName{
  margin-left: 10px;
  font-size: 10px;
} 




</style>