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
					
				})

			}



		}}());

app.init();

$("#location-list").on('click',".location-items" , function(event){
	$(".location-items").parent().removeClass("active");
	$(this).parent().addClass("active");	
	app.getTrends($(this).attr("data-geoid"));

});

