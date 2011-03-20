package com.tikal.codegen.jet;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.tikal.codegen.jet.parser.TemplateParser;

public class TemplateEmitter {

	final private URL templateURL;

	final private TemplateParser parser;

	private Skeleton skeleton = null;

	public TemplateEmitter(URL templateURL) {
		this.templateURL = templateURL;
		parser = new TemplateParser(templateURL);
	}

	public String getPackageName() {
		return skeleton.packageName;
	}

	public String getClassName() {
		return skeleton.className;
	}

	public void parse() throws EmitterException {
		try {
			if (parser.parse()) {
				skeleton = createSkeleton();
			} else {
				throw parser.getFaultReason();
			}
		} catch (IOException e) {
			throw new EmitterException(ErrorCode.UNKNOWN, e);
		}
	}

	public String generate() {
		StringBuilder builder = new StringBuilder();
		builder.append(skeleton.header);
		builder.append("\n\t\tStringBuffer builder = new StringBuffer();");
		for (TemplateElement templateElement : parser.getElements()) {
			templateElement.generate(builder);
		}

		builder.append("\n\t\treturn builder.toString();\n\t");
		builder.append(skeleton.footer);

		return builder.toString();
	}

	private Skeleton createSkeleton() throws EmitterException {
		Skeleton skeleton = new Skeleton();
		skeleton.packageName = parser.getPackageName();
		skeleton.className = parser.getClassName();

		if (StringUtils.isBlank(skeleton.className)) {
			throw new EmitterException(ErrorCode.CLASS_NAME_UNDEFINED);
		}

		URL skeletonURL = null;
		if (StringUtils.isNotEmpty(parser.getSkeleton())) {
			String templateURLName = templateURL.toString();
			int pos = templateURLName.lastIndexOf("/");
			if (pos >= 0) {
				String skeletonURLString = templateURLName.substring(0, pos + 1) + parser.getSkeleton();
				try {
					skeletonURL = new URL(skeletonURLString);
				} catch (MalformedURLException e) {
					throw new EmitterException(ErrorCode.SKELETON_NOT_FOUND, skeletonURLString, e.getMessage());
				}
			}
		} else {
			skeletonURL = getClass().getResource("/default.skeleton");
		}

		StringWriter skeletonWriter = new StringWriter();

		try {
			IOUtils.copy(skeletonURL.openStream(), skeletonWriter);
		} catch (IOException e) {
			throw new EmitterException(ErrorCode.SKELETON_READ_FAILED, skeletonURL.toString());
		}
		String skeletonContent = skeletonWriter.toString();
		int pos = skeletonContent.indexOf("generate");
		if (pos > 0) {
			int pos1 = skeletonContent.indexOf("{", pos);
			int pos2 = skeletonContent.indexOf("}", pos1);
			String left = skeletonContent.substring(0, pos1 + 1);
			String right = skeletonContent.substring(pos2);
			if (StringUtils.isNotBlank(skeleton.packageName)) {
				left = left.replace("PACKAGE", parser.getPackageName());
			}
			left = left.replace("CLASS", skeleton.className);

			if (StringUtils.isNotBlank(parser.getImports())) {
				String[] imports = parser.getImports().split(" ");
				StringBuilder builder = new StringBuilder("\n");
				for (String imp : imports) {
					builder.append("import ");
					builder.append(imp);
					builder.append(";\n");
				}

				pos = left.indexOf("package");
				if (pos >= 0) {
					pos = left.indexOf(";", pos + 1);
				} else {
					pos = 0;
				}
				left = left.substring(0, pos + 1) + builder.toString() + left.substring(pos + 1);
			}
			skeleton.header = left;
			skeleton.footer = right;
		}

		return skeleton;
	}

	static class Skeleton {

		String packageName;

		String className;

		String header;

		String footer;
	}
}
