package Browser.FireFox;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestRegister {
    private List<WebDriver> drivers = new ArrayList<>();

    @BeforeMethod
    public void init(){
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:4200/register");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        drivers.add(driver); // Thêm driver vào danh sách
        Helper.setDriver(driver); // Set driver cho Helper
    }

    @Test(priority = 1)
    public void TestCase10() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "0963101750", "123123", "123123", "Luu Bang Thuan", "20012000", "HCM City");
        Helper.acceptAlert(driver);
    }

    @Test(priority = 2)
    public void TestCase11() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "0345872900", "123123", "123123", "Bui Thanh Son", "11112001", "HCMs");
        Helper.acceptAlert(driver);
    }

    @AfterMethod
    public void tearDown(){
        Helper.closeAllBrowser();
    }
}
