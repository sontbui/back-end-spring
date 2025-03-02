package Browser.Edge;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestLogin {

	private List<WebDriver> drivers = new ArrayList<>();

	@BeforeMethod
	public void init() throws InterruptedException {
		WebDriverManager.edgedriver().setup();
		WebDriver driver = new EdgeDriver();
		driver.get("http://localhost:4200/login");
		Thread.sleep(1000);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30)); // Page load timeout
		driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(30)); // Script timeout
		drivers.add(driver); // Thêm driver vào danh sách
		Helper.setDriver(driver); // Set driver cho Helper
	}

	// sign in for admin
	@Test
	public void TestCase1() throws InterruptedException {

		WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
		Helper.login(driver,"0123456789", "123123");

		driver.findElement(By.className("password-toggle")).click();
		Thread.sleep(3000);
	}

	//sign in for user
	@Test
	public void TestCase2() {
		WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
		Helper.login(driver,"0963101750","123123");
		driver.findElement(By.className("password-toggle")).click();

	}

	// null phone and password
	@Test
	public void TestCase3() {
		WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
		Helper.login(driver,"","");
		Helper.acceptAlert(driver);
	}

	// null phone and correct password
	@Test
	public void TestCase4() {
		WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
		Helper.login(driver,"","123123");
		Helper.acceptAlert(driver);
	}

	// correct phone and null password
	@Test
	public void TestCase5() {
		WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại

		Helper.login(driver,"0963101750","");
		Helper.acceptAlert(driver);
	}

	// correct phone and wrong password
	@Test
	public void TestCase6() {
		WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
		Helper.login(driver,"0963101750","55555555");
		driver.findElement(By.className("password-toggle")).click();

		Helper.acceptAlert(driver);
	}

	// wrong phone and correct password
	@Test
	public void TestCase7() {
		WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
		Helper.login(driver,"11111111111111","123123");
		driver.findElement(By.className("password-toggle")).click();

		Helper.acceptAlert(driver);
	}
	// wrong phone and wrong password
	@Test
	public void TestCase8() {
		WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
		Helper.login(driver,"11111111","555555555555");
		driver.findElement(By.className("password-toggle")).click();

		Helper.acceptAlert(driver);
	}

	// nhập dư hoặc thiếu số điện thoại
	@Test
	public void TestCase9(){
		WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
		Helper.login(driver,"012346578","123123");
		driver.findElement(By.className("password-toggle")).click();

		Helper.acceptAlert(driver);
	}





	@AfterMethod
	public void teardown() {
		Helper.closeAllBrowser();
	}
}
