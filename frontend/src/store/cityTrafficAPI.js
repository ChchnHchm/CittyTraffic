import axios from 'axios';
const API = "http://localhost:1369/citytraffic/api/";

export const CityTrafficAPI =  {
    getByDate: async function(date){
        const URL = API + "getDate";
        return await  axios.get(
            URL,{
                params: { 
                    date: date
                }
            }
        ).then( res => res.data);
    },
    getByHours: async function(date,hour){
        const URL = API + "getHours";
        return await  axios.get(
            URL,{
                params: {
                    date: date,
                    hours:hour
                }
            }
        ).then( res => res.data);
    },
    getByRadarDate: async function(radar,date){
        const URL = API + "getRadarDate";
        return await  axios.get(
            URL,{
                params: {
                    radar: radar,
                    date: date
                }
            }
        ).then( res => res.data);
    },
    getByRadarHours: async function(radar,date,hour){
        const URL = API + "getRadarHours";
        return await  axios.get(
            URL,{
                params: {
                    radar: radar,
                    date: date,
                    hour: hour
                }
            }
        ).then( res => res.data);
    }
}
