package com.tikal.codegen.jet;


public class ExpressionElement extends TemplateElement {


	public ExpressionElement(String content) {
		super(content);
	}

	@Override
	public void generate(StringBuilder builder) {
		builder.append("\n\t\tbuilder.append(" + getContent() + ");");
	}
}
