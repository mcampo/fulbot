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
		this.listenTo(this.collection, "destroy", this.onModelDestroy);
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
	},

	onModelDestroy : function(model) {
		this.$("li[data-id='"+ model.id + "']").remove();
	}

});

App.Views.EventView = Backbone.View.extend({
	events : {
		"click .btn-delete" : "onBtnDeleteClick",
		"click .btn-add" : "onBtnAddClick",
		"click .btn-remove" : "onBtnRemoveClick",
		"keypress .new-attendee" : "onNewAttendeeKeypress"
	},

	initialize : function() {
		this.listenTo(this.model, "change", this.render);
		this.listenTo(this.model, "destroy", this.remove);
	},

	render : function() {
		var html = App.render("event-view", this.model.toJSON());
		this.$el.html(html);
	},

	onBtnDeleteClick : function() {
		var that = this;
		this.model.destroy();
	},

	onBtnAddClick : function() {
		var attendee = this.$(".new-attendee").val();
		this.model.get("attendance").push(attendee);
		this.model.save();
		this.render();
	},

	onNewAttendeeKeypress : function(e) {
		if (e.which == 13) {
			this.onBtnAddClick();
		}
		this.$(".new-attendee").focus();
	},

	onBtnRemoveClick : function(e) {
		var attendance = this.model.get("attendance"), attendee = $(e.target)
				.parent().find(".attendee").text();

		this.model.set("attendance", _.without(attendance, attendee));
		this.model.save();
	}
});