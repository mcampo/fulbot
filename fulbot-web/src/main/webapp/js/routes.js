App.Routers.MainRouter = Backbone.Router.extend({

	routes : {
		"" : "index",
		"events/:eventId" : "event"
	},

	initialize : function(options) {
		this.mainView = options.mainView;
	},

	index : function() {
		console.log("index");

		this.mainView.eventCollection.fetch();
	},

	event : function(eventId) {
		this.mainView.showEvent(eventId);
	}

});