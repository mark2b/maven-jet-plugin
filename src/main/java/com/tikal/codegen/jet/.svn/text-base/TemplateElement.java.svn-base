package com.tikal.codegen.jet;

abstract public class TemplateElement {
	private final StringBuilder content = new StringBuilder();

	public TemplateElement(String content) {
		this.content.append(content);
	}

	public StringBuilder getContent() {
		return content;
	}

	abstract public void generate(StringBuilder builder);
}
