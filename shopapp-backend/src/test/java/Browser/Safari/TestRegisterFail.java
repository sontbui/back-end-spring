package Browser.Safari;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestRegisterFail {
    private List<WebDriver> drivers = new ArrayList<>();

    @BeforeMethod
    public void init(){
        WebDriverManager.safaridriver().setup();
        WebDriver driver = new SafariDriver();
        driver.get("http://localhost:4200/register");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        drivers.add(driver); // Thêm driver vào danh sách
        Helper.setDriver(driver); // Set driver cho Helper
    }
    // null phone
    @Test(priority = 1)
    public void TestCase1() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "", "123123", "123123", "Luu Bang Thuan", "20012000", "HCM City");
        Helper.acceptAlert(driver);
    }

    // null password
    @Test(priority = 2)
    public void TestCase2() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "0345872900", "", "123123", "Bui Thanh Son", "11112001", "HCMs");
        Helper.acceptAlert(driver);
    }


    // null retype password
    @Test(priority = 3)
    public void TestCase3() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "0345872900", "123123", "", "Bui Thanh Son", "11112001", "HCMs");
        Helper.acceptAlert(driver);
    }


    // null name
    @Test(priority = 4)
    public void TestCase4() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "0345872900", "123123", "123123", "", "11112001", "HCMs");
        Helper.acceptAlert(driver);
    }

    //null DOB
    @Test(priority = 5)
    public void TestCase5() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "0345872900", "123123", "123123", "Bui Thanh Son", "", "HCMs");
        Helper.acceptAlert(driver);
    }

    //null address
    @Test(priority = 6)
    public void TestCase6() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "0345872900", "123123", "123123", "Bui Thanh Son", "11112001", "");
        Helper.acceptAlert(driver);
    }

    // retypePassword không trùng khớp
    @Test(priority = 7)
    public void TestCase7() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "1122334455", "123123", "hhhhhh", "Bui Thanh Son", "20012000", "HCMs");
        Helper.acceptAlert(driver);
    }

    // Phone bị trùng với tài khoản đã có

    @Test(priority = 8)
    public void TestCase8() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.register(driver, "1122334455", "123123", "123123", "Bui Thanh Son", "20012000", "HCMs");
        Helper.acceptAlert(driver);
    }

    @AfterMethod
    public void tearDown(){
        Helper.closeAllBrowser();
    }
}

