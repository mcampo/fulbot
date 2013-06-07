App.Models.Event = Backbone.Model.extend({

});

App.Collections.EventCollection = Backbone.Collection.extend({
	model : App.Models.Event,
	url : "/api/events"
});

App.Views.EventCollectionView = Backbone.View.extend({
	initialize : function() {
		this.listenTo(this.collection, "sync", this.render);
	},

	render : function() {
		console.log("render event collection");
		console.log(this.collection);
		var html = App.render("event-list", {
			events : this.collection.toJSON()
		});
		this.$el.html(html);
		return this;
	}
});