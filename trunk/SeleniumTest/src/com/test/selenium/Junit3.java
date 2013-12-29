package com.test.selenium;

import com.thoughtworks.selenium.SeleneseTestCase;

public class Junit3 extends SeleneseTestCase {
	public void setUp() throws Exception {
		setUp("https://www.google.fr/", "*chrome");
	}
	public void testJunit3() throws Exception {
		selenium.open("/");
		selenium.typeKeys("id=gbqfq", "test seleniu");
		Thread.sleep(5000);
		verifyEquals("Selenium", selenium.getText("//ol[@id='rso']/li[2]/div/h3/a/em"));
		verifyEquals("test selenium - Recherche Google", selenium.getTitle());
	}
}
