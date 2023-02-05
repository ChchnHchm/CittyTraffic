import  {reactive, ref} from "vue";

export const dateCounter = reactive({
    date: ref(new Date()),
    changeDate(newDate){
        this.date = newDate;
    }
});