function parse_json(i) {
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
    var terms = json["terms"];
    for (i; i < 30; i++) {
      console.log(terms[i]);
      console.log(data);
      data["labels"].push(terms[i]["text"])
      data["datasets"][0]["data"].push(terms[i]["precision"])
      data["datasets"][1]["data"].push(terms[i]["terminology_trend"])

  }
  });

  return data;
}


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

var data = parse_json(0)



var myBarChart = new Chart(ctx, {
  type: 'bar',
  data: data,
  options: options
});
