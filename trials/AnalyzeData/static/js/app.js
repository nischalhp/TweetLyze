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
				alert("mofo");
				$.ajax({
					type: "GET",
					url : "/trends/"+geoid,
					}).success(function(response){
						console.log(response);
					}).error(function(response){
						console.log(response);
					});
				}

		}}());

app.init();

/* Attaching click handler to location list */ 
$("#location-list").on('click',".location-items" , function(event){
	$(".location-items").parent().removeClass("active");
	/* The click is on the anchor tag but we need make the whole line as active hence we uses this.parent*/
	$(this).parent().addClass("active");	
	app.getTrends($(this).attr("data-geoid"));

});

