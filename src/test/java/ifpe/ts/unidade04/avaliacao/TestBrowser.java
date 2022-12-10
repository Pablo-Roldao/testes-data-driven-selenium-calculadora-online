package ifpe.ts.unidade04.avaliacao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

class TestBrowser {

	static ChromeDriver chromeDriver;
	
	@BeforeAll
	static void setUpBeforeClass() {
		WebDriverManager.chromedriver().setup();
		chromeDriver = new ChromeDriver();
	}
	
	@AfterAll
	static void tearDownAfterClass() {
		chromeDriver.quit();
	}
	
	@Test
	void test() {
		chromeDriver.get("https://www.google.com/");
	}

}
