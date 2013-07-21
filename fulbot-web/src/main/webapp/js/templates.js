App.TemplateRenderer = function() {
	this.templates = {};
}

_.extend(App.TemplateRenderer.prototype, {
	render : function(templateName, context) {
		if (!this.templates[templateName]) {
			this.loadTemplate(templateName);
		}
		return this.templates[templateName](context);
	},

	loadTemplate : function(templateName) {
		var templateId = templateName + "-hbs";
		var $templateElement = $("#" + templateId);
		if ($templateElement.length === 0) {
			throw new Error("Could not find template with id: " + templateId);
		}
		this.templates[templateName] = Handlebars.compile($templateElement
				.html());
	}
});

App.render = function(templateName, context) {
	return App.templateRenderer.render(templateName, context);
};

App.templateRenderer = new App.TemplateRenderer();