package com.tikal.maven.plugin.jet.test;

import groovy.lang.GroovyClassLoader;

import java.io.StringReader;
import java.net.URL;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.tikal.codegen.jet.TemplateEmitter;
import com.tikal.codegen.jet.parser.JetLexer;
import com.tikal.codegen.jet.parser.JetParser;

public class JetParsetTest {

	@Test
	public void readDirective() throws RecognitionException {
		CharStream stream = new ANTLRStringStream(
				"<%@ jet a='b' c='d'  %><%-- comment 1--%>outside text<% scriplet %>\n<%= expression %>outside text2\n<%-- comment 2--%><%= expression 2 %><html><% another scriplet%>");
		JetLexer lexer = new JetLexer(stream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JetParser parser = new JetParser(tokens);
		CommonTree tree = (CommonTree) parser.document().getTree();
		printTree(tree);
	}

	private void printTree(Tree tree) {
		System.out.println(tree + " " + tree.getType() + " " + tree.getText());
		for (int i = 0; i < tree.getChildCount(); i++) {
			printTree(tree.getChild(i));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testEmitter() throws Exception {
		URL in = getClass().getResource("/t1.template");
		TemplateEmitter emitter = new TemplateEmitter(in);
		emitter.parse();
		String source = emitter.generate();
		GroovyClassLoader gcl = new GroovyClassLoader();
		Class clazz = gcl.parseClass(source);
		Object instance = clazz.newInstance();
		String generated = (String) clazz.getMethod("generate", Object.class).invoke(instance, "");
		generated = generated.toString();
		List<String> expectedLines = IOUtils.readLines(getClass().getResourceAsStream("/t1.result"));
		List<String> genLines = IOUtils.readLines(new StringReader(generated));
		Assert.assertEquals(expectedLines.size(), genLines.size());
		for (int i = 0; i < expectedLines.size() && i < genLines.size(); i++) {
			Assert.assertEquals(expectedLines.get(i), genLines.get(i));
		}
	}
}
