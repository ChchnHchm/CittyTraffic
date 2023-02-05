<script setup>
import {dateCounter}  from "@/store/Date.js"
</script>


<template :key="dateCounter.date" >
<div id="InfoCard"  :key="HourCounter.Hour">
    {{dateCounter.date}}
    selected hour : {{HourCounter.Hour}} H
    <a href="#" class="block max-w-sm p-6 bg-white border border-gray-200 rounded-lg shadow hover:bg-gray-100 dark:bg-gray-800 dark:border-gray-700 dark:hover:bg-gray-700">
        <h5 class="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">Noteworthy technology acquisitions 2021</h5>
        <p class="font-normal text-gray-700 dark:text-gray-400">Here are the biggest enterprise technology acquisitions of 2021 so far, in reverse chronological order.</p>
    </a>
</div>
</template>

<script>
import {dateCounter}  from "@/store/Date.js"
import {HourCounter}  from "@/store/Hour.js"

export default {
    name: "infoCard",
}
</script>

<style scoped>
#InfoCard{
    border-radius: 25px;
    overflow-y: scroll;
    margin-left: 0.5%;
    background-color: #191C24;
} 
</style>