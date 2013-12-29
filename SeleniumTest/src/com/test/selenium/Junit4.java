package com.test.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleneseTestBase;

public class Junit4 extends SeleneseTestBase {
	private Selenium selenium;

	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "https://www.google.fr/");
		selenium.start();
	}

	@Test
	public void testJunit4() throws Exception {
		selenium.open("/");
		selenium.typeKeys("id=gbqfq", "test selenium");
		Thread.sleep(5000);
		verifyEquals("test selenium - Recherche Google", selenium.getTitle());
		verifyTrue(selenium.isTextPresent(""));
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
