//Context Chart
var data = {};

var ctx = document.getElementById("Line").getContext("2d");

var options = {
  scales: {
    yAxes: [{
      scaleLabel: {
        display: true,
        labelString: 'elapsed time (min)'
      }
    }],
    xAxes: [{
      scaleLabel: {
        display: true,
        labelString: 'number of terms'
      }
    }]
  }
}

var myLineChart = new Chart(ctx, {
    type: 'line',
    data: data,
    options : options

});