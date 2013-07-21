//main namespaces
var App = window.App = {
	Views : {},
	Models : {},
	Collections : {},
	Routers : {}
};

App.Views.MainView = Backbone.View.extend({
	initialize : function() {
		console.log("Initializing MainView");

		this.eventCollection = new App.Collections.EventCollection();
		this.eventCollectionView = new App.Views.EventCollectionView({
			el : this.$("#event-list"),
			collection : this.eventCollection
		});
		this.eventView = new App.Views.EventView({
			el : this.$("#event-view")
		});

		var router = new App.Routers.MainRouter({
			mainView : this
		});
		Backbone.history.start();
	},

	showEvent : function(eventId) {
		var event = this.eventCollection.get(eventId);
		if (event) {
			this.eventView.model = event;
			this.eventView.render();
		}
	}
});

$(function() {
	var mainView = new App.Views.MainView({
		el : $("body")
	});
});
