package com.tikal.codegen.jet;

import org.apache.commons.lang.StringUtils;

public class TextElement extends TemplateElement {

	boolean removeFirstNewLine = false;

	public TextElement(String content) {
		super(content);
	}

	@Override
	public void generate(StringBuilder builder) {
		String content = getContent().toString();
		if (removeFirstNewLine) {
			int pos = content.indexOf('\n');
			if (pos == 0) {
				content = content.substring(1);
			}
			else if (pos > 0) {
				if (StringUtils.isBlank(content.substring(0, pos))) {
					content = content.substring(pos + 1);
				}
			}
		}
		content = content.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\t", "\\\\t").replaceAll("\"", "\\\\\"");
		builder.append("\n\t\tbuilder.append(\"" + content + "\");");
	}

	public void removeFirstNewLine() {
		removeFirstNewLine = true;
	}
}
