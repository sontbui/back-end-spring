package Browser.Chrome;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestManageAccount {
    WebDriver Driver;
    private List<WebDriver> drivers = new ArrayList<>();

    @BeforeMethod
    public void init(){
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:4200");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        drivers.add(driver); // Thêm driver vào danh sách
        Helper.setDriver(driver); // Set driver cho Helper
    }

    // cập nhập thông tin cá nhân
    @Test(priority = 1)
    public void TestCase29() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        driver.findElement(By.id("btn-login")).click();

        //thao tác đăng nhập vào user
        Helper.login(driver,"0345872900","123123");
        driver.findElement(By.className("password-toggle")).click();

        driver.findElement(By.className("name")).click();
        Helper.clickElement(driver, "popover-item", 0);
        Helper.fillFieldIfEmpty(driver, By.id("fullname"),"Bui Thanh Son");
        Helper.fillFieldIfEmpty(driver, By.id("address"),"27082003");
        Helper.fillFieldIfEmpty(driver, By.id("password"),"123123");
        Helper.fillFieldIfEmpty(driver, By.id("retype_password"),"123123");
        driver.findElement(By.className("btn-dark")).click();

    }

    // đăng xuất tài khoản
    @Test(priority = 2)
    public void TestCase30() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        driver.findElement(By.id("btn-login")).click();

        //thao tác đăng nhập vào user
        Helper.login(driver, "0345872900", "123123");
        driver.findElement(By.className("password-toggle")).click();

        driver.findElement(By.className("name")).click();
        Helper.clickElement(driver, "popover-item", 1);
    }


    // Đổi lại mật khẩu khi nhập đúng định dạng
    @Test
    public void TestCase31() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        driver.findElement(By.id("btn-login")).click();

        //thao tác đăng nhập vào user
        Helper.login(driver, "0963101750", "123123");
        driver.findElement(By.className("name")).click();
        Helper.clickElement(driver, "popover-item", 0);
        Helper.fillFieldIfEmpty(driver, By.id("fullname"),"Phan Huu Thinh");
        Thread.sleep(2000);
        Helper.fillFieldIfEmpty(driver, By.id("address"),"Ho Chi Minh City");
        Thread.sleep(2000);
        Helper.fillFieldIfEmpty(driver, By.id("date_of_birth"),"11111111");
        Helper.fillFieldIfEmpty(driver, By.id("password"),"123123");
        Helper.fillFieldIfEmpty(driver, By.id("retype_password"),"123123");
        driver.findElement(By.className("btn-dark")).click();
        Helper.acceptAlert(driver);
    }


    //đổi mật khẩu nhưng nhập không đúng mật
    @Test
    public void TestCase32() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        driver.findElement(By.id("btn-login")).click();

        //thao tác đăng nhập vào user
        Helper.login(driver, "0963101750", "123123");
        driver.findElement(By.className("name")).click();
        Helper.clickElement(driver, "popover-item", 0);
        Helper.fillFieldIfEmpty(driver, By.id("fullname"),"Phan Huu Thinh");
        Thread.sleep(2000);
        Helper.fillFieldIfEmpty(driver, By.id("address"),"Ho Chi Minh City");
        Thread.sleep(2000);
        Helper.fillFieldIfEmpty(driver, By.id("date_of_birth"),"11111111");
        Helper.fillFieldIfEmpty(driver, By.id("password"),"123123");
        Helper.fillFieldIfEmpty(driver, By.id("retype_password"),"123456");
        driver.findElement(By.className("btn-dark")).click();
        Thread.sleep(2000);
        Helper.acceptAlert(driver);
    }
    @AfterMethod
    public void teardown() {
        Helper.closeAllBrowser();
    }

}
