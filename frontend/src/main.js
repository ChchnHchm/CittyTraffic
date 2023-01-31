import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

const app = createApp(App);
app.component('Datepicker',Datepicker);
app.use(router);

app.mount("#app");
