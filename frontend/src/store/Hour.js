import  {reactive, ref} from "vue";

export const HourCounter = reactive({
    Hour: ref(new Date().getHours()),
    changeHour(newHour){
        this.Hour = newHour;
    }
});