
var draw = function draw(id,data,yAxe){
  $(document).ready(function(){

    var ctx = document.getElementById(id).getContext("2d");

    var options = {
      scales: {
        yAxes: [{
          scaleLabel: {
            display: true,
            labelString: yAxe
          }
        }],
        xAxes: [{
          scaleLabel: {
            display: true,
            labelString: 'number of file'
          }
        }]
      }
    }

function parse_json(file,callback,id,yAxe){
  var data = {}
  var values = {};
  data["labels"] = [];
  data["datasets"] = [];
  $.getJSON(file, function(json) {
    json["run"].forEach(function(el,index){
      if(index === 0){
        for (var label in el) {
          values[label] = []
          if (label != "corpus_size"){
            var color = random_color()
            data["datasets"].push({
              label:label,
              data: values[label],
              fill: false,
              pointBorderColor: "black",
              pointBackgroundColor: "black",
              backgroundColor: color,
              borderColor: color,
              borderWidth: 5,
              pointBorderWidth: 1,
              pointHoverRadius: 6,
              pointHoverBackgroundColor: "black",
              pointHoverBorderColor: "rgba(220,220,220,1)",
              pointHoverBorderWidth: 4,
              pointRadius: 2
            })
          }
        }
      }
      data["labels"].push(el["corpus_size"])
      for(var value in el){
        if (value != "corpus_size"){
          values[value].push(el[value])
        }
      }
    });
  })
  callback(id,data,yAxe)
}

function random_color(){
  return '#80'+(0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6);
}

parse_json("time_history.json",draw,"time", "elapsed time (sec)")
parse_json("memory_history.json",draw,"memory","allocated memory (Mo)")
