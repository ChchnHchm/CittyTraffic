<template>
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
      <l-marker v-for="marker, index in markers" :lat-lng="marker" @click="showDetails" ></l-marker>
    </l-map>
  </div>
</div>
</template>

<script>

/*


      <l-marker id="1" :lat-lng="[44.793469904751085, -0.6078416174777933]" @click="showDetails" ></l-marker>


      <l-marker id="2" :lat-lng="[44.795480918499464, -0.6041196319276992]" ></l-marker>


      <l-marker id="3" :lat-lng="[44.79812829441045, -0.6038893833715705]" ></l-marker>

      <l-marker id="4.1" :lat-lng="[44.8019903395983, -0.59830808752805]" ></l-marker>
      <l-marker  id="4.2" :lat-lng="[44.802726197988996, -0.5994481926816773]" ></l-marker>


      <l-marker  id="5" :lat-lng="[44.8050567965058, -0.601147599781788]" ></l-marker>
      <l-marker  id="6" :lat-lng="[44.80640271698327, -0.600322157679394]" ></l-marker>
      <l-marker  id="7" :lat-lng="[44.80640669696472, -0.599794289364735]" ></l-marker>
      <l-marker  id="8" :lat-lng="[44.80834915058863, -0.5981746798849495]" ></l-marker>
      <l-marker  id="9" :lat-lng="[44.808866852332784, -0.5957394317593594]" ></l-marker>
      <l-marker  id="10" :lat-lng="[44.80999061439174, -0.5953803306932977]" ></l-marker>

      <l-marker  id="11" :lat-lng="[44.80977772947405, -0.5941210176082866]" ></l-marker>
      <l-marker  id="12" :lat-lng="[44.80917745957286, -0.5915581186952361]" ></l-marker>
      <l-marker  id="13" :lat-lng="[44.80744763017207, -0.6031012664302314]" ></l-marker>
      <l-marker  id="14" :lat-lng="[44.805938199145075, -0.6078027612825729]" ></l-marker>
      <l-marker  id="15" :lat-lng="[44.80330144782458, -0.6071418037677067]" ></l-marker>
      <l-marker  id="16" :lat-lng="[44.80081916928184, -0.6094419612199954]" ></l-marker>

      <l-marker  id="17.1" :lat-lng="[44.8026819562842, -0.6102969935863783]" ></l-marker>
      <l-marker  id="17.2" :lat-lng="[44.803536425606026, -0.6102247373379459]" ></l-marker>

      <l-marker  id="18" :lat-lng="[44.80205391325668, -0.6128500478420797]" ></l-marker>
      <l-marker  id="19" :lat-lng="[44.800216741341856, -0.6165050099452328]" ></l-marker>
      <l-marker  id="20" :lat-lng="[44.797956512363406, -0.6187208684220015]" ></l-marker>

      <l-marker  id="21" :lat-lng="[44.796700316556404, -0.6208614599099357]" ></l-marker>
      <l-marker  id="22" :lat-lng="[44.79388018560965, -0.6230261367605902]" ></l-marker>
      <l-marker  id="23" :lat-lng="[44.7921111240608, -0.6211534956508575]" ></l-marker>
      <l-marker  id="24" :lat-lng="[44.791153928027704, -0.6217977805462955]" ></l-marker>
      <l-marker  id="25" :lat-lng="[44.790401834309286, -0.6131210089434798]" ></l-marker>
      <l-marker  id="26" :lat-lng="[44.79284610303488, -0.6132835854594414]" ></l-marker>
      <l-marker  id="27" :lat-lng="[44.796268759971646, -0.609441961380266]" ></l-marker>

*/
import "leaflet/dist/leaflet.css";
import { LMap, LTileLayer, LControlZoom,LMarker } from "@vue-leaflet/vue-leaflet";
import {latLngBounds , latLng} from "leaflet"; 

export default {
  name: "Map",
  components: {
    LMap,
    LTileLayer,
    LControlZoom,
    LMarker,
  },
  mounted(){
    console.log(LMap);
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
        [44.8106985806742, -0.5564507584073208],
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
      markers: [
        latLng(44.793469904751085, -0.6078416174777933),
        latLng(44.793469904751085, -0.6078416174777933),
        latLng(44.795480918499464, -0.6041196319276992),
        latLng(44.79812829441045, -0.6038893833715705),
        latLng(44.8019903395983, -0.59830808752805),
        latLng(44.802726197988996, -0.5994481926816773),
        latLng(44.8050567965058, -0.601147599781788),
        latLng(44.80640271698327, -0.600322157679394),
        latLng(44.80640669696472, -0.599794289364735),
        latLng(44.80834915058863, -0.5981746798849495),
        latLng(44.808866852332784, -0.5957394317593594),
        latLng(44.80999061439174, -0.5953803306932977),
        latLng(44.80977772947405, -0.5941210176082866),
        latLng(44.80917745957286, -0.5915581186952361),
        latLng(44.80744763017207, -0.6031012664302314),
        latLng(44.805938199145075, -0.6078027612825729),
        latLng(44.80330144782458, -0.6071418037677067),
        latLng(44.80081916928184, -0.6094419612199954),
        latLng(44.8026819562842, -0.6102969935863783),
        latLng(44.803536425606026, -0.6102247373379459),
        latLng(44.80205391325668, -0.6128500478420797),
        latLng(44.800216741341856, -0.6165050099452328),
        latLng(44.797956512363406, -0.6187208684220015),
        latLng(44.796700316556404, -0.6208614599099357),
        latLng(44.79388018560965, -0.6230261367605902),
        latLng(44.7921111240608, -0.6211534956508575),
        latLng(44.791153928027704, -0.6217977805462955),
        latLng(44.790401834309286, -0.6131210089434798),
        latLng(44.79284610303488, -0.6132835854594414),
        latLng(44.796268759971646, -0.609441961380266),
      ],  
    };
  },
  methods:{
    zoomUpdate(zoom){
      this.currentZoom  = zoom;
    },
    showDetails(event){
      document.getElementById("InfoCard").innerHTML= "Here are the biggest enterprise technology acquisitions of 2021 so far, in reverse chronological order.";
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
</style>