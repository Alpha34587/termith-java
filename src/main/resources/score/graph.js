function parse_json(counter, callback) {
  var data = {};
  data["labels"] = [];
  data["datasets"] = [];

  data["datasets"].push(
    {
      label: "precision",
      backgroundColor: 'rgba(54, 162, 235, 0.2)',
      borderColor: 'rgba(54, 162, 235, 1)',
      data: []
    }
  );

  data["datasets"].push(
    {
      label: "terminology trend",
      backgroundColor: 'rgba(255, 99, 132, 0.2)',
      borderColor: 'rgba(255,99,132,1)',
      data: []
    }
  );

  $.getJSON("termith-score.json", function(json) {
    console.log("retour getjson ",json);
    var terms = json["terms"];
    if (counter < 0){
      counter = terms.length - 15;
      g_counter= counter
    }

    if (counter > terms.length - 15){
      counter = 0;
      g_counter= counter
    }
    for (i = 0 + counter; i < 15 + counter; i++) {
      data["labels"].push(terms[i]["text"])
      data["datasets"][0]["data"].push(terms[i]["precision"])
      data["datasets"][1]["data"].push(terms[i]["terminology_trend"])

    }
    callback(data);
  });
}

function draw(counter){
    parse_json(counter,function callback(data){
    console.log("j'ai fini de charger ici, c'est un callback " , data);

      var ctx = document.getElementById("graph").getContext("2d");

      var options = {
        barValueSpacing: 20,
        scales: {
          yAxes: [{
            ticks: {
              min: 0,
            }
          }]
        }
      };

      myBarChart = new Chart(ctx, {
        type: 'bar',
        data: data,
        options: options
      });
  });

}

function inc(){
  myBarChart.destroy();
  g_counter += 10;
  draw(g_counter);
}


function dec(){
  myBarChart.destroy();
  g_counter -= 10;
  draw(g_counter);
}
var myBarChart;
var g_counter = 0;
$(document).ready(function() {
  draw(g_counter);
})
