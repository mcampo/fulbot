App.Models.Event = Backbone.Model.extend({

});

App.Collections.EventCollection = Backbone.Collection.extend({
	model : App.Models.Event,
	url : "/api/events"
});

App.Views.EventCollectionView = Backbone.View.extend({
	events : {
		"click li a" : "onClick"
	},

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
	},

	onClick : function(e) {
		this.$("li").removeClass("active");
		$(e.target).parent("li").addClass("active");
	}
});

App.Views.EventView = Backbone.View.extend({
	initialize : function() {
		console.log("initialized event view");
	},

	render : function() {
		console.log("showing event: " + this.model.id);
		var html = App.render("event-view", this.model.toJSON());
		this.$el.html(html);
	}
});