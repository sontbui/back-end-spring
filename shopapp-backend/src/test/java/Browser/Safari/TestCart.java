package Browser.Safari;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestCart {

    WebDriver driver;
    private List<WebDriver> drivers = new ArrayList<>();

    @BeforeMethod
    public void init() {
        WebDriverManager.safaridriver().setup();

        driver = new SafariDriver();
        driver.get("http://localhost:4200");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30)); // Page load timeout
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(3000)); // Script timeout
        drivers.add(driver); // Thêm driver vào danh sách
        Helper.setDriver(driver); // Set driver cho Helper

    }


    // xóa sản phẩm sau khi đã thêm vào giỏ hàng
//    @Test(priority = 1)
//    public void TestCase21() throws InterruptedException {
//        WebDriver driver = drivers.get(drivers.size() - 1);
//        driver.findElement(By.id("btn-login")).click();
//        //thao tác đăng nhập vào user
//        Helper.login(driver,"0963101750","123123");
//        Thread.sleep(1000);
//
//        //chọn sản phẩm thứ 2 trong trang
//        Helper.clickElement(driver, "product-item", 1);
//        Thread.sleep(1000);
//        //click nút mua ngay
//        driver.findElement(By.className("btn-success")).click();
//
//        Thread.sleep(3000);
//
//        //xóa sản phẩm trong giỏ hàng
//        Helper.clickElement(driver, "btn-danger",0);
//        Thread.sleep(1000);
//
//        Helper.acceptAlert(driver);
//        Thread.sleep(1000);
//        Helper.clickElement(driver, "nav-link",0);
//
//    }
//
//    // tăng giảm số lượng sau khi thêm vào giỏ hàng
//    @Test(priority = 2)
//    public void TestCase22() throws InterruptedException {
//        WebDriver driver = drivers.get(drivers.size() - 1);
//        driver.findElement(By.id("btn-login")).click();
//        //thao tác đăng nhập vào user
//        Helper.login(driver,"0963101750","123123");
//        Thread.sleep(1000);
//
//        //chọn sản phẩm thứ 2 trong trang
//        Helper.clickElement(driver, "product-item", 1);
//        Thread.sleep(1000);
//        //click nút mua ngay
//        driver.findElement(By.className("btn-success")).click();
//
//        Thread.sleep(2000);
//        driver.findElement(By.id("increaseBtn")).click();
//        Thread.sleep(2000);
//        driver.findElement(By.id("increaseBtn")).click();
//        Thread.sleep(2000);
//        driver.findElement(By.className("btn-de-in")).click();
//
//    }
//
//    // kiểm tra đã có sản phẩm được thêm vào giỏ hàng hay chưa
//    @Test(priority = 3)
//    public void TeseCase23() throws InterruptedException {
//        WebDriver driver = drivers.get(drivers.size() - 1);
//        driver.findElement(By.id("btn-login")).click();
//        //thao tác đăng nhập vào user
//        Helper.login(driver,"0963101750","123123");
//        Thread.sleep(3000);
//        driver.findElement(By.id("cart")).click();
//
//
//        List<WebElement> elements = driver.findElements(By.className("orderBTN"));
//
//        if (!elements.isEmpty()) {
//            System.out.println("Chưa có sản phẩm");
//            // điền thông tin nếu đã có thêm sản phẩm
//
//        } else {
//            Helper.clickElement(driver, "nav-link",0);
//        }
//    }
//    // mua hàng và điền đầy đủ thông tin
//    @Test(priority = 4)
//    public void TestCase24() throws InterruptedException {
//        WebDriver driver = drivers.get(drivers.size() - 1);
//        driver.findElement(By.id("btn-login")).click();
//
//        // Thao tác đăng nhập vào user
//        Helper.login(driver, "0963101750", "123123");
//        Thread.sleep(1000);
//
//        // Chọn sản phẩm thứ 2 trong trang
//        Helper.clickElement(driver, "product-item", 1);
//        Thread.sleep(1000);
//
//        // Click nút mua ngay
//        driver.findElement(By.className("btn-success")).click();
//        Thread.sleep(1000);
//
//        // Mở giỏ hàng
//        driver.findElement(By.id("cart")).click();
//
//        // Điền thông tin vào các trường nếu chúng rỗng
//        Helper.fillFieldIfEmpty(driver, By.id("fullname"), "Luu Bang Thuan");
//        Helper.fillFieldIfEmpty(driver, By.id("email"), "thuan@gmail.com");
//        Helper.fillFieldIfEmpty(driver, By.id("phone"), "0963101750");
//        Helper.fillFieldIfEmpty(driver, By.id("address"), "HCM city");
//
//        // Chọn phương thức vận chuyển và thanh toán nếu chưa được chọn
//        Helper.selectOptionByIndex(driver, "#shippingMethod",0);
//        Helper.selectOptionByIndex(driver, "#paymentMethod",1);
//
//        // Click nút thanh toán
//        driver.findElement(By.className("orderBTN")).click();
//        Thread.sleep(2000);
//        Helper.acceptAlert(driver);
//    }

    // nhập không đúng định dạng email vẫn order thành công
    @Test(priority = 5)
    public void TestCase() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1);
        driver.findElement(By.id("btn-login")).click();

        // Thao tác đăng nhập vào user
        Helper.login(driver, "0963101750", "123123");
        Thread.sleep(1000);

        // Chọn sản phẩm thứ 2 trong trang
        Helper.clickElement(driver, "product-item", 1);
        Thread.sleep(1000);

        // Click nút mua ngay
        driver.findElement(By.className("btn-success")).click();
        Thread.sleep(1000);

        // Mở giỏ hàng
        driver.findElement(By.id("cart")).click();

        // Điền thông tin vào các trường nếu chúng rỗng
        Helper.fillFieldIfEmpty(driver, By.id("fullname"), "Luu Bang Thuan");
        Helper.fillFieldIfEmpty(driver, By.id("email"), "done");
        Helper.fillFieldIfEmpty(driver, By.id("phone"), "0963101750");
        Helper.fillFieldIfEmpty(driver, By.id("address"), "HCM city");

        // Chọn phương thức vận chuyển và thanh toán nếu chưa được chọn
        Helper.selectOptionByIndex(driver, "#shippingMethod",0);
        Helper.selectOptionByIndex(driver, "#paymentMethod",1);

        // Click nút thanh toán
        driver.findElement(By.className("orderBTN")).click();
        Thread.sleep(2000);
        Helper.acceptAlert(driver);
    }


    // không nhập tên vẫn order thành công
    @Test(priority = 1)
    public void TestCase1() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1);
        driver.findElement(By.id("btn-login")).click();

        // Thao tác đăng nhập vào user
        Helper.login(driver, "0963101750", "123123");
        Thread.sleep(1000);

        // Chọn sản phẩm thứ 2 trong trang
        Helper.clickElement(driver, "product-item", 1);
        Thread.sleep(1000);

        // Click nút mua ngay
        driver.findElement(By.className("btn-success")).click();
        Thread.sleep(1000);

        // Mở giỏ hàng
        driver.findElement(By.id("cart")).click();

        // Điền thông tin vào các trường nếu chúng rỗng
        Helper.fillFieldIfEmpty(driver, By.id("fullname"), "");
        Helper.fillFieldIfEmpty(driver, By.id("email"), "done");
        Helper.fillFieldIfEmpty(driver, By.id("phone"), "0963101750");
        Helper.fillFieldIfEmpty(driver, By.id("address"), "HCM city");

        // Chọn phương thức vận chuyển và thanh toán nếu chưa được chọn
        Helper.selectOptionByIndex(driver, "#shippingMethod",0);
        Helper.selectOptionByIndex(driver, "#paymentMethod",1);

        // Click nút thanh toán
        driver.findElement(By.className("orderBTN")).click();
        Thread.sleep(2000);
        Helper.acceptAlert(driver);
    }

    @AfterMethod
    public void tearDown(){
        Helper.closeAllBrowser();
    }
}
