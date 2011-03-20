package com.tikal.maven.plugin.jet.test;

import org.junit.Test;

import com.tikal.codegen.jet.TemplateEmitter;

public class ParserTest {

	@Test
	public void parse() {
		try {
			TemplateEmitter emitter = new TemplateEmitter(getClass().getResource("/project/templates/t1.template"));
			emitter.parse();
			String s = emitter.generate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
