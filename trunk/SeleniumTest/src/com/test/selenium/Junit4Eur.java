package com.test.selenium;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class Junit4Eur {
	private Selenium selenium;

	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://www.eurostar.com/");
		selenium.start();
	}

	@Test
	public void testJunit4Eur() throws Exception {
		selenium.open("/fr-fr");
		selenium.click("id=dialog-element");
		selenium.waitForPageToLoad("30000");
		assertEquals("Train aller", selenium.getText("css=h1"));
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
