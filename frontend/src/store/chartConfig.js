export const option = {
    responsive: true,
    maintainAspectRatio: false,
    scales:{
        x:{
            type: 'time',
            time: {
                unit: 'day'
            },
            ticks: {
                source: 'data',
                autoSkip: true,
                maxTicksLimit: 10,
                stepSize: 10
            },
            title: {
                display: true,
                text: "Time",
            },
        },
        y:{
            type: "linear",
            display: true,
            position: "left",
        },
    },
};

export default option;