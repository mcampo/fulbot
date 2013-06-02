App.Routers.MainRouter = Backbone.Router.extend({

	routes : {
		"" : "index"
	},

	initialize : function(options) {
		this.mainView = options.mainView;
	},

	index : function() {
		console.log("index");

		this.mainView.eventCollection.fetch();
	}

});