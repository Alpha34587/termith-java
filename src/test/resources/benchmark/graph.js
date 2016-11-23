//Context Chart
var data = {
    labels: [],
    datasets: [
        {
        label : "Disambiguation",
        data: [],
        fill: false,
        pointBorderColor: "black",
        pointBackgroundColor: "black",
        backgroundColor: "rgba(238, 133, 133,0.4)",
        borderColor: "rgba(238, 133, 133,0.4)",
        pointBorderWidth: 1,
        pointHoverRadius: 5,
        pointHoverBackgroundColor: "black",
        pointHoverBorderColor: "rgba(220,220,220,1)",
        pointHoverBorderWidth: 2,
        pointRadius: 1,
        },

        {
        label: "Context Lexicon",
        data: [],
        fill: false,
        pointBorderColor: "black",
        borderColor: "rgba(75,192,192,0.4)",
        backgroundColor: "rgba(75,192,192,0.4)",
        pointBackgroundColor: "black",
        pointBorderWidth: 1,
        pointHoverRadius: 5,
        pointHoverBackgroundColor: "black",
        pointHoverBorderColor: "rgba(220,220,220,1)",
        pointHoverBorderWidth: 2,
        pointRadius: 1,
        },

        {
        label: "R coefficient",
        data: [],
        fill: false,
        pointBorderColor: "black",
        borderColor: "rgba(235,188,147,0.89)",
        backgroundColor: "rgba(235,188,147,0.89)",
        pointBackgroundColor: "black",
        pointBorderWidth: 1,
        pointHoverRadius: 5,
        pointHoverBackgroundColor: "black",
        pointHoverBorderColor: "rgba(220,220,220,1)",
        pointHoverBorderWidth: 2,
        pointRadius: 1,
        }
    ]

};

var ctx = document.getElementById("Line").getContext("2d");

var myLineChart = new Chart(ctx, {
    type: 'line',
    data: data
});