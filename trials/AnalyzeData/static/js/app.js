var geoid = '';
var trend = '';
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

				var source = $("#tweet-tpl").html();
				var template = Handlebars.compile(source);
				this.tweet_template = template;



				var source_adj = $("#adj-tpl").html();
				var template_adj = Handlebars.compile(source_adj);
				this.template_adj = template_adj;

			},


			onLocationSuccess : function(response){
				var lineItems = '';
				$.each(response.data, function(index,location){
					var lineItem = '<li class="pull-left li-items" > <a href="#" class="location-items" data-geoid="'+location.geoid+'">'+location.city+'</a></li>';
					lineItems = lineItems+lineItem;
				});
				$("#location-list").html(lineItems);
			},


			getTrends : function(geoid,min_date,max_date){
				console.log(min_date,max_date);
				$.ajax({
					type: "GET",
					url : "/trends?locationid="+geoid+"&min_date="+min_date+"&max_date="+max_date
				}).success(function(response){
					graphTrends.displayTrends(response.data);
				}).error(function(response){
					console.log(response);
				});
			},

			getDates : function(locationid){
				$.ajax({
					type: "GET",
					url : "/dates/"+locationid
				}).success(function(response){
					app.setSlider(response.data[0].min_date,response.data[0].max_date);
					app.getTrends(geoid,response.data[0].min_date,response.data[0].max_date);
				}).error(function(response){
					console.log(response);
				});


			},

			setSlider : function(min_date,max_date){
				console.log(min_date,max_date);
				var min = new Date(min_date),max = new Date(max_date);
				$('#slider').dateRangeSlider({
					bounds:{
						min  : min,
						max : max
					},
					defaultValues:{
						min : min,
						max : max
					},
					arrows:true,
				});	
			},

			getTfidfEntites : function(locationid,trend){
				console.log(trend);
				$.ajax({
					type: "GET",
					url : "/tfidf/?locationid="+encodeURIComponent(locationid)+"&trend="+encodeURIComponent(trend)
				}).success(function(response){
					graphTfidf.displayTfidf(response.data);
				}).error(function(response){
					console.log(response);
				});
			},
			getTweets : function(trend,entity){
				console.log(trend);
				$.ajax({
					type: "GET",
					url : "/tweets/?trend="+encodeURIComponent(trend)+"&entity="+encodeURIComponent(entity)
				}).success(function(response){
					$("#tweets-container").html(app.tweet_template(response));
					$("#tweetModal").modal("show");
					console.log(response)
				}).error(function(response){
					console.log(response);
				});
			},
				getAdjacencyMatrix : function(geoid){
				console.log(trend);
				$.ajax({
					type: "GET",
					url : "/kmedoids/"+geoid
				}).success(function(response){
					miserables = response;
					$("#adj-cont").html(app.template_adj(response));
				}).error(function(response){
					console.log(response);
				});

		}
	}

		graphTrends = {

			displayTrends: function(trends){
				var fill = d3.scale.category20();

				d3.layout.cloud().size([1200, 300])
				.words(trends
					.map(function(d) {
						return {text: d.trend, size: 20 + d.count};
					}))
				.padding(30)
				.font("Impact")
				.fontSize(function(d) { return d.size; })
				.on("end", draw)
				.start();

				function draw(words) {
					d3.select(".trends-chart").append("svg")
					.attr("width", 960)
					.attr("height", 300)
					.append("g")
					.attr("width",900)
					.attr("height",250)
					.attr("transform", "translate(400,100)")
					.selectAll("text")
					.data(words)
					.enter().append("text")
					.style("font-size", function(d) { return d.size + "px"; })
					.style("font-family", "Impact")
					.style("fill", function(d, i) { return fill(i); })
					.attr("text-anchor", "middle")
					.attr("transform", function(d) {
						return "translate(" + [d.x, d.y] + ")";
					})
					.text(function(d) { return d.text; })
					.on("click",function(d){
						$("#entity-chart").html("");
						trend = d.text;
						app.getTfidfEntites(geoid,d.text);
					});
				}

			}

		}

		graphTfidf = {

			displayTfidf: function(trends){
				var fill = d3.scale.category10();

				d3.layout.cloud().size([1200, 300])
				.words(trends
					.map(function(d) {
						return {text: d.entity, size: 20 + Math.log(d.tfidf)};
					}))
				.padding(5)
				.font("Impact")
				.fontSize(function(d) { return d.size; })
				.on("end", draw)
				.start();

				function draw(words) {
					d3.select("#entity-chart").append("svg")
					.attr("width", 960)
					.attr("height", 300)
					.append("g")
					.attr("width",900)
					.attr("height",250)
					.attr("transform", "translate(400,100)")
					.selectAll("text")
					.data(words)
					.enter().append("text")
					.style("font-size", function(d) { return d.size + "px"; })
					.style("font-family", "Impact")
					.style("fill", function(d, i) { return fill(i); })
					.attr("text-anchor", "middle")
					.attr("transform", function(d) {
						return "translate(" + [d.x, d.y] + ")";
					})
					.text(function(d) { return d.text; })
					.on("click",function(d){
						app.getTweets(trend,d.text);
					});
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
	/*app.getTrends($(this).attr("data-geoid"));*/
	geoid = '';
	geoid = $(this).attr("data-geoid") ;
	$('#trends').html("");
	$('#entity-chart').html("");
	app.getDates(geoid);

});


// bind function to date slider
$("#slider").bind("valuesChanged", function(e, data){
	var  min_date = convertMillisecondsToDate(Date.parse(data.values.min));
	var max_date = convertMillisecondsToDate(Date.parse(data.values.max));
	$("#trends").html("");
	$("#entity-chart").html("");
	app.getTrends(geoid,min_date,max_date);
});

$("#adj-mat").on('click',"#btn-sim" , function(event){
	app.getAdjacencyMatrix(geoid);

});

function convertMillisecondsToDate(millseconds){
	date = new Date(millseconds).getDate();
	year = new Date(millseconds).getFullYear();
	month = new Date(millseconds).getMonth();
	month = month + 1;
	if (month < 10){
		month = '0'+month;
	}
	if (date < 10){
		date = '0'+date;
	}
	return year+'-'+month+'-'+date;//+"-"+month+"-"date;
}