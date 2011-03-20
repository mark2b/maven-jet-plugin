package com.tikal.maven.plugin.jet.test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.tikal.maven.plugin.jet.JetMojo;

public class JetMojoTest extends AbstractMojoTestCase {

	JetMojo mojo = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		File pom = new File(getBasedir(), "src/test/resources/project/pom.xml");
		mojo = (JetMojo) lookupMojo("generate", pom);
		setVariableValueToObject(mojo, "templateDirectory", new File(getBasedir(),
				"src/test/resources/project/templates"));
		setVariableValueToObject(mojo, "generateDirectory", new File(getBasedir(),
				"src/test/resources/project/generated-sources"));
	}

	public void testEnvironment() throws Exception {
		assertNotNull(mojo);
	}

	public void testIncludeAll() throws Exception {
		Set<String> includeTemplates = new HashSet<String>();
		includeTemplates.add("**/*.template");
		setVariableValueToObject(mojo, "includeTemplates", includeTemplates);

		Set<File> templates = mojo.getTemplates();
		assertEquals(2, templates.size());
	}

	public void testInclude() throws Exception {
		Set<String> includeTemplates = new HashSet<String>();
		includeTemplates.add("**/t1.template");
		setVariableValueToObject(mojo, "includeTemplates", includeTemplates);

		Set<File> templates = mojo.getTemplates();
		assertEquals(1, templates.size());
	}

	public void testExclude() throws Exception {
		Set<String> includeTemplates = new HashSet<String>();
		includeTemplates.add("**/*");
		Set<String> excludeTemplates = new HashSet<String>();
		excludeTemplates.add("**/t2.template");
		setVariableValueToObject(mojo, "includeTemplates", includeTemplates);
		setVariableValueToObject(mojo, "excludeTemplates", excludeTemplates);

		Set<File> templates = mojo.getTemplates();
		assertEquals(1, templates.size());
	}

	public void testGenerate() throws Exception {
		mojo.execute();
	}
}
