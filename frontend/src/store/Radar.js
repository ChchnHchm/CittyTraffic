import  {reactive, ref} from "vue";
import { marker}  from "@/data/data.js";


export const RadarCounter = reactive({
    name: ref(marker[0]),
    changeName(newName){
        this.name = newName;
    }
});