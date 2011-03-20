package com.tikal.codegen.jet;

import java.util.ResourceBundle;

public enum ErrorCode {
	UNKNOWN("error.unknown"), TEMPLATE_READ_FAILED("error.template.read.failed"), SKELETON_READ_FAILED(
			"error.skeleton.read.failed"), SKELETON_NOT_FOUND("error.skeleton.not.found"), INCLUDE_NOT_FOUND(
			"error.include.not.found"), UNKNOWN_DIRECTIVE("error.unknown.directive"), CLASS_NAME_UNDEFINED(
			"error.class.name.undefined"), DIRECTIVE_JET_ABSENT("error.directive.jet.absent"), TEMPLATE_PARSING_FAILED(
			"error.template.parsing.failed"), INCLUDE_FILE_NAME_ABSENT("error.include.file.name.absent");

	final String key;

	final String template;

	ErrorCode(String key) {
		this.key = key;
		ResourceBundle bundle = ResourceBundle.getBundle("com.tikal.codegen.jet.i18n.messages");
		template = bundle.getString(key);
	}

	public String getTemplate() {
		return template;
	}

	public String getMessage(Object... args) {
		return String.format(template, args);
	}
}
