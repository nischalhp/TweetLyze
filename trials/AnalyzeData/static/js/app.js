$( function(){
	app = {
			//init is the property of the object app which is
			// represented as a function	
			init : function(){
				$.ajax({
					type: "GET",
					url: "/locations"
				}).success(function(response){
					app.onLocationSuccess(response);
				});
			},

			onLocationSuccess : function(response){
				var lineItems = '';
				$.each(response.data, function(index,location){
					var lineItem = '<li> <a href="#" class="location-items" data-geoid="'+location.geoid+'">'+location.city+'</a></li>';
					lineItems = lineItems+lineItem;
				});
				$("#location-list").html(lineItems);
			},


			getTrends : function(geoid){
				$.ajax({
					type: "GET",
					url : "/trends/"+geoid,
				}).success(function(response){
					graph.displayTrends(response.data);
				}).error(function(response){
					console.log(response);
				});
			}

		}

		graph = {

			displayTrends: function(trends){
				var fill = d3.scale.category20();

				d3.layout.cloud().size([800, 300])
				.words(trends
					.map(function(d) {
						return {text: d.trend, size: 20 + d.count};
					}))
				.padding(5)
				.font("Impact")
				.fontSize(function(d) { return d.size; })
				.on("end", draw)
				.start();

				function draw(words) {
					console.log(words);
					d3.select(".trends-chart").append("svg")
					.attr("width", 800)
					.attr("height", 300)
					.append("g")
					.attr("width",800)
					.attr("height",300)
					.attr("transform", "translate(350,100)")
					.selectAll("text")
					.data(words)
					.enter().append("text")
					.style("font-size", function(d) { return d.size + "px"; })
					.style("font-family", "Impact")
					.style("fill", function(d, i) { return fill(i); })
					.attr("text-anchor", "middle")
					.attr("transform", function(d) {
						return "translate(" + [d.x+20, d.y+10] + ")";
					})
					.text(function(d) { return d.text; });
				}

			}
		}




	}());




app.init();

/* Attaching click handler to location list */ 
$("#location-list").on('click',".location-items" , function(event){
	$(".location-items").parent().removeClass("active");
	/* The click is on the anchor tag but we need make the whole line as active hence we uses this.parent*/
	$(this).parent().addClass("active");	
	app.getTrends($(this).attr("data-geoid"));

});
