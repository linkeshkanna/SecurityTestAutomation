package com_test;

import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


public class BodgeITTest {
	
	private WebDriver driver;
	private String site = "http://localhost:8080/bodgeit/";
	
	public void setUp() throws Exception {
		  Proxy proxy = new Proxy();
		  proxy.setHttpProxy("localhost:8090");
		  proxy.setFtpProxy("localhost:8090");
		  proxy.setSslProxy("localhost:8090");
		  DesiredCapabilities capabilities = new DesiredCapabilities();
		  capabilities.setCapability(CapabilityType.PROXY, proxy);
		  driver = new FirefoxDriver(capabilities);
		  this.setDriver(driver);
		  driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	public void tstMenuLinks() {
		checkMenuLinks("home.jsp");
	}
	
	public void tearDown() throws Exception {
		driver.close();
	}
	
	public void tstRegisterUser() {
		// Create random username so we can rerun test
		String randomUser = RandomStringUtils.randomAlphabetic(10) + "@test.com";
		this.registerUser(randomUser, "password");
		if (driver.getPageSource().indexOf("You have successfully registered with The BodgeIt Store.") > 0)
			System.out.println("USER CREATION: PASS");
		else
			System.out.println("USER CREATION: FAIL");
	}
	
	public void registerUser(String user, String password) {
		driver.get(site + "login.jsp");
		checkMenu("Register", "register.jsp");
		
		WebElement link = driver.findElement(By.name("username"));
		link.sendKeys(user);

		link = driver.findElement(By.name("password1"));
		link.sendKeys(password);
		
		link = driver.findElement(By.name("password2"));
		link.sendKeys(password);
		
		link = driver.findElement(By.id("submit"));
		link.click();
		sleep();
	}
	
	public void checkMenu(String linkText, String page) {
		sleep();
		WebElement link = driver.findElement(By.linkText(linkText));
		link.click();
		sleep();
		
		if ((site + page).equalsIgnoreCase(driver.getCurrentUrl()))
			System.out.println("CheckCurrentURL from " + linkText + ": PASS");
		else
			System.out.println("CheckCurrentURL from " + linkText + ": FAIL");
	}
	
	public void checkMenuLinks(String page) {
		driver.get(site + page);
		checkMenu("Home", "home.jsp");

		driver.get(site + page);
		checkMenu("About Us", "about.jsp");
		
		driver.get(site + page);
		checkMenu("Contact Us", "contact.jsp");
		
		driver.get(site + page);
		checkMenu("Login", "login.jsp");
		
		driver.get(site + page);
		checkMenu("Your Basket", "basket.jsp");
		
		driver.get(site + page);
		checkMenu("Search", "search.jsp");		
	}
	
	public void tstRegisterAndLoginUser() {
		// Create random username so we can rerun test
		String randomUser = RandomStringUtils.randomAlphabetic(10) + "@test.com";
		this.registerUser(randomUser, "password");
		if (driver.getPageSource().indexOf("You have successfully registered with The BodgeIt Store.") > 0)
			System.out.println("REGISTRATION PAGE: PASS");
		else
			System.out.println("REGISTRATION PAGE: FAIL");
		checkMenu("Logout", "logout.jsp");
		
		this.loginUser(randomUser, "password");
		if (driver.getPageSource().indexOf("You have logged in successfully:") > 0)
			System.out.println("LOGIN: PASS");
		else
			System.out.println("LOGIN: FAIL");
	}
	
	public void loginUser(String user, String password) {
		driver.get(site + "login.jsp");
		
		WebElement link = driver.findElement(By.name("username"));
		link.sendKeys(user);

		link = driver.findElement(By.name("password"));
		link.sendKeys(password);
		
		link = driver.findElement(By.id("submit"));
		link.click();
		sleep();
	}
	
	public void tstAddProductsToBasket() {
		driver.get(site + "product.jsp?typeid=1");
		sleep();
		driver.findElement(By.linkText("Basic Widget")).click();
		sleep();
		driver.findElement(By.id("submit")).click();
		sleep();
		
		driver.get(site + "product.jsp?typeid=2");
		sleep();
		driver.findElement(By.linkText("Thingie 2")).click();
		sleep();
		driver.findElement(By.id("submit")).click();
		sleep();
		
		driver.get(site + "product.jsp?typeid=3");
		sleep();
		driver.findElement(By.linkText("TGJ CCC")).click();
		sleep();
		driver.findElement(By.id("submit")).click();
		sleep();
	}

	public void tstSearch() {
		driver.get(site + "search.jsp?q=doo");
		sleep();		
	}
	
	protected WebDriver getDriver() {
		return driver;
	}

	protected void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	protected String getSite() {
		return site;
	}

	protected void setSite(String site) {
		this.site = site;
	}
	
	private void sleep() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.getMessage();
		}		
	}
	
	public void testAll() {
		tstMenuLinks();
		tstRegisterUser();
		tstRegisterAndLoginUser();
		tstAddProductsToBasket();
		tstSearch();
	}
	
	public static void main(String[] args) throws Exception {
		BodgeITTest test = new BodgeITTest();
		test.setUp();
		test.testAll();
		test.tearDown();		
	}
}
