package com.tikal.codegen.jet.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.Tree;

import com.tikal.codegen.jet.EmitterException;
import com.tikal.codegen.jet.ErrorCode;
import com.tikal.codegen.jet.ExpressionElement;
import com.tikal.codegen.jet.ScripletElement;
import com.tikal.codegen.jet.TemplateElement;
import com.tikal.codegen.jet.TextElement;

public class TemplateParser {

	final private URL templateURL;

	EmitterException faultReason = null;

	List<TemplateElement> elements = new ArrayList<TemplateElement>();

	private String className = "";

	private String packageName = "";

	private String skeleton = "";

	private String imports = "";

	public TemplateParser(URL templateURL) {
		super();
		this.templateURL = templateURL;
	}

	public boolean parse() throws IOException {
		elements.clear();
		return parseInternal(templateURL.openStream());
	}

	private boolean parseInternal(InputStream inputStream) {
		CharStream stream = null;
		try {
			stream = new ANTLRInputStream(inputStream);
			JetLexer lexer = new JetLexer(stream);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			JetParser parser = new JetParser(tokens);
			parser.setTreeAdaptor(new Adaptor());
			JetTree tree = (JetTree) parser.document().getTree();
			if (tree != null) {
				int count = tree.getChildCount();
				for (int i = 0; i < count; i++) {
					JetTree child = (JetTree) tree.getChild(i);
					if (child instanceof JetDirectiveTree) {
						JetDirectiveTree directive = (JetDirectiveTree) child;
						if (directive.getId() != null) {
							if (directive.getId().equals("jet")) {
								packageName = directive.getAttribute("package");
								className = directive.getAttribute("class");
								skeleton = directive.getAttribute("skeleton");
								imports = directive.getAttribute("imports");
							} else if (directive.getId().equals("include")) {
								String file = directive.getAttribute("file");
								if (file == null) {
									throw new EmitterException(ErrorCode.INCLUDE_FILE_NAME_ABSENT);
								}
								URL url = getUrlForName(file);
								parseInternal(url.openStream());
							}
						}
					}
					child.createElement();
				}
				postParseProcess();
				return true;
			} else {
				setFaultReason(new EmitterException(ErrorCode.TEMPLATE_PARSING_FAILED));
			}
		} catch (IOException e) {
			setFaultReason(new EmitterException(ErrorCode.TEMPLATE_READ_FAILED, e.getMessage()));
		} catch (RecognitionException e) {
			setFaultReason(new EmitterException(ErrorCode.TEMPLATE_PARSING_FAILED, e.getMessage()));
		}
		return false;
	}

	private void postParseProcess() {
		boolean removeFirstNewLine = true;
		for (TemplateElement element : elements) {
			if (element instanceof ScripletElement) {
				removeFirstNewLine = true;
			} else if (element instanceof ExpressionElement) {
				removeFirstNewLine = false;
			} else if (element instanceof TextElement) {
				if (removeFirstNewLine) {
					((TextElement) element).removeFirstNewLine();
				}
				removeFirstNewLine = false;
			}
		}
	}

	public EmitterException getFaultReason() {
		return faultReason;
	}

	public void setFaultReason(EmitterException faultReason) {
		this.faultReason = faultReason;
	}

	public List<TemplateElement> getElements() {
		return elements;
	}

	public String getClassName() {
		return className;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getSkeleton() {
		return skeleton;
	}

	public String getImports() {
		return imports;
	}

	final class Adaptor extends CommonTreeAdaptor {

		private Adaptor() {
			super();
		}

		@Override
		public Object create(final Token payload) {
			if (payload == null) {
				return new JetTree(payload);
			}
			switch (payload.getType()) {
			case JetLexer.ID:
				return new JetGenericIdTree(payload);
			case JetLexer.ATTRIBUTES:
				return new JetAttributesTree(payload);
			case JetLexer.ATTRIBUTE:
				return new JetAttributeTree(payload);
			case JetLexer.JET_DIRECTIVE:
				return new JetDirectiveTree(payload);
			case JetLexer.JET_EXPRESSION:
				return new JetExpressionTree(payload);
			case JetLexer.JET_SCRIPTLET:
				return new JetScripletTree(payload);
			case JetLexer.TEXT:
				return new JetTextTree(payload);
			default:
				return new JetTree(payload);
			}
		}
	}

	class JetTree extends CommonTree {

		public JetTree(Token payload) {
			super(payload);
		}

		public JetTree getChildByType(int type) {
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				JetTree tree = (JetTree) getChild(i);
				if (tree.getType() == type) {
					return tree;
				}
			}
			return null;
		}

		public void createElement() {
		}
	}

	class JetDirectiveTree extends JetTree {

		public JetDirectiveTree(Token payload) {
			super(payload);
		}

		public String getId() {
			Tree id = getChildByType(JetParser.ID);
			return id == null ? null : id.getText();
		}

		public String getAttribute(String name) {
			JetAttributesTree attrubutes = (JetAttributesTree) getChildByType(JetParser.ATTRIBUTES);
			if (attrubutes != null) {
				JetAttributeTree attrubute = attrubutes.getAttribute(name);
				if (attrubute != null) {
					return attrubute.getValue();
				}
			}
			return null;
		}
	}

	class JetGenericIdTree extends JetTree {

		public JetGenericIdTree(Token payload) {
			super(payload);
		}
	}

	class JetAttributesTree extends JetTree {

		public JetAttributesTree(Token payload) {
			super(payload);
		}

		public JetAttributeTree getAttribute(String name) {
			for (int i = 0; i < getChildCount(); i++) {
				JetAttributeTree attribute = (JetAttributeTree) getChild(i);
				if (attribute.getName().equals(name)) {
					return attribute;
				}
			}
			return null;
		}
	}

	class JetAttributeTree extends JetTree {

		public JetAttributeTree(Token payload) {
			super(payload);
		}

		public String getName() {
			return getChild(0).getText();
		}

		public String getValue() {
			return getChild(1).getText();
		}
	}

	class JetExpressionTree extends JetTree {

		public JetExpressionTree(Token payload) {
			super(payload);
		}

		public void createElement() {
			elements.add(new ExpressionElement(getText()));
		}
	}

	class JetScripletTree extends JetTree {

		public JetScripletTree(Token payload) {
			super(payload);
		}

		public void createElement() {
			elements.add(new ScripletElement(getText()));
		}
	}

	class JetTextTree extends JetTree {

		public JetTextTree(Token payload) {
			super(payload);
		}

		public void createElement() {
			String text = getText().replace("\r\n", "\n");
			boolean addNewElement = true;
			if (elements.size() > 0) {
				TemplateElement element = elements.get(elements.size() - 1);
				if (element instanceof TextElement) {
					TextElement textElement = (TextElement) element;
					textElement.getContent().append(text);
					addNewElement = false;
				}
			}
			if (addNewElement) {
				elements.add(new TextElement(text));
			}
		}
	}

	URL getUrlForName(String fileName) {
		String templateURLName = templateURL.toString();
		int pos = templateURLName.lastIndexOf("/");
		if (pos >= 0) {
			String urlString = templateURLName.substring(0, pos + 1) + fileName;
			try {
				return new URL(urlString);
			} catch (MalformedURLException e) {
				throw new EmitterException(ErrorCode.INCLUDE_NOT_FOUND, urlString, e.getMessage());
			}
		}
		throw new EmitterException(ErrorCode.INCLUDE_NOT_FOUND, fileName);
	}
}
