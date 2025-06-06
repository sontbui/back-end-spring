package Browser.Chrome;

import org.openqa.selenium.By;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.*;

//@SpringBootTest
public class TestAdminOperation {

    WebDriver driver;
    private List<WebDriver> drivers = new ArrayList<>();

    @BeforeMethod
    public void init() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.get("http://localhost:4200");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30)); // Page load timeout
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(30)); // Script timeout
        drivers.add(driver); // Thêm driver vào danh sách
        Helper.setDriver(driver); // Set driver cho Helper
    }

    // Using order
    @Test(priority = 1)
    public void TestCase25() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); 

        driver.findElement(By.id("btn-login")).click();
        Helper.login(driver, "0123456789", "123123");
        Thread.sleep(1000);
        driver.findElement(By.className("form-control")).sendKeys("thuan");
        Thread.sleep(1000);
        Helper.clickElement(driver, "btn-primary", 0);
        Thread.sleep(1000);
        Helper.clickElement(driver, "btn-primary", 1);
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 500)");
        Helper.selectOptionByIndex(driver, "#statusSelect", 2);
        Thread.sleep(1000);
        driver.findElement(By.id("btn-save")).click();
        Thread.sleep(1000);

        //delete order
        Helper.clickElement(driver, "btn-danger", 1);
        Thread.sleep(2000);
        Helper.acceptAlert(driver);
    }


    // Using categories
    @Test(priority = 2)
    public void TestCase26() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1);
        driver.findElement(By.id("btn-login")).click();
        Helper.login(driver, "0123456789", "123123");
        driver.findElement(By.id("test_categories")).click();
        Thread.sleep(5000);
        driver.findElement(new By.ByClassName("btn-success")).click();
        driver.findElement(By.id("name")).sendKeys("a");
        Thread.sleep(5000);
        driver.findElement(By.className("btn-primary")).click();
        Thread.sleep(5000);
        Helper.clickElement(driver, "btn-primary", 0);
        Thread.sleep(2000);
        WebElement textField = driver.findElement(By.id("name"));
        textField.clear();
        textField.sendKeys("abcxyz");
        Thread.sleep(1000);
        driver.findElement(By.className("btn-primary")).click();
        Thread.sleep(1000);

        //delete category
        Helper.clickElement(driver,"btn-danger", 1);
        Thread.sleep(2000);
        Helper.acceptAlert(driver);

    }


    // using product
    @Test(priority = 3)
    public void TestCase27() throws InterruptedException {


        driver.findElement(By.id("btn-login")).click();
        Helper.login(driver, "0123456789", "123123");
        Thread.sleep(1000);


        // searching product
        driver.findElement(By.id("test_product")).click();
        driver.findElement(By.className("form-control")).sendKeys("Tivi");
        driver.findElement(By.id("btn-find")).click();
        Thread.sleep(1000);


        // adding product
        driver.findElement(By.id("btn-add")).click();
        driver.findElement(By.id("name")).sendKeys("test");
        driver.findElement(By.id("price")).sendKeys("10");
        driver.findElement(By.id("description")).sendKeys("test description");
        Thread.sleep(1000);
        Helper.selectOptionByIndex(driver, "#category", 7);
        Thread.sleep(1000);


        WebElement fileInput = driver.findElement(By.id("images"));
        fileInput.sendKeys("E:\\dog.jpg");

        driver.findElement(By.className("btn-primary")).click();
        Thread.sleep(2000);

        // delete product
        Helper.clickElement(driver, "page-item", 3);
        Thread.sleep(2000);
        Helper.clickElement(driver, "btn-danger", 1);
        Thread.sleep(2000);
        Helper.dismissAlert(driver);


    }

    // using user
    @Test(priority = 4)
    public void TestCase28() throws InterruptedException {
        driver.findElement(By.id("btn-login")).click();

        //thao tác đăng nhập vào admin
        Helper.login(driver, "0123456789", "123123");
        Thread.sleep(1000);
        driver.findElement(By.id("test_user")).click();

        //thao tác quản lý user

        driver.findElement(By.id("test_user")).click();
        Thread.sleep(1000);
        Helper.clickElement(driver, "btn-danger", 2);
        Thread.sleep(1000);
        Helper.dismissAlert(driver);
    }

    @AfterMethod
    public void tearDown(){
        Helper.closeAllBrowser();
    }
}
