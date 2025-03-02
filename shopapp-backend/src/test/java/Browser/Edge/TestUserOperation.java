package Browser.Edge;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestUserOperation {

    WebDriver driver;
    private List<WebDriver> drivers = new ArrayList<>();

    @BeforeMethod
    public void init() {
        WebDriverManager.edgedriver().setup();

        driver = new EdgeDriver();
        driver.get("http://localhost:4200");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30)); // Page load timeout
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(3000)); // Script timeout
        drivers.add(driver); // Thêm driver vào danh sách
        Helper.setDriver(driver); // Set driver cho Helper

    }



    //tìm kiếm sản phẩm có chọn loại sản phẩm
    // vd: pc, index pc là3
    @Test(priority = 1)
    public void TestCase12() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại

        // tìm kiếm
        driver.findElement(By.className("search-input")).sendKeys("pc");
        Thread.sleep(1000);
        //chọn category phù hợp
        Helper.selectOptionByIndex(driver, ".product-category", 7);// hoặc index 0 để không cần chọn category
        driver.findElement(By.id("btn-search")).click();
        Thread.sleep(1000);
    }


    // tìm kiếm sản phẩm bằng số liệu mà không chọn loại sản phẩm
    // vd: 13
    @Test(priority = 2)
    public void TestCase13() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại

        // tìm kiếm
        driver.findElement(By.className("search-input")).sendKeys("13");
        Thread.sleep(1000);
        //nếu không chọn category thì mặc định sẽ là all
        driver.findElement(By.id("btn-search")).click();
        Thread.sleep(1000);
    }


    // tìm kiếm sản phẩm mà lựa chọn loại sản phẩm không phù hợp
    // vd: tìm Mac nhưng loại sản phẩm là Iphone (index: 4)
    @Test(priority = 3)
    public void TestCase14() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại

        // tìm kiếm
        driver.findElement(By.className("search-input")).sendKeys("Mac");
        Thread.sleep(1000);
        Helper.selectOptionByIndex(driver, ".product-category", 4);

        driver.findElement(By.id("btn-search")).click();
        Thread.sleep(1000);
    }


    // tìm kiếm không nhập vào tên sản phẩm mà chọn loại sản phẩm
    // vd: chọn loại sp Accessories (index: 1)
    @Test(priority = 4)
    public void TestCase15() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại

        // tìm kiếm
        driver.findElement(By.className("search-input")).sendKeys("");
        Thread.sleep(1000);
        Helper.selectOptionByIndex(driver, ".product-category", 1);

        driver.findElement(By.id("btn-search")).click();
        Thread.sleep(1000);
    }


    //chọn sản phẩm sau đó xem ảnh
    @Test(priority = 5)
    public void TestCase16() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại

        Helper.clickElement(driver, "product-item", 6);
        driver.findElement(By.className("carousel-control-next-icon")).click();
        Thread.sleep(2000);
        driver.findElement(By.className("carousel-control-next-icon")).click();
        Thread.sleep(2000);
        driver.findElement(By.className("carousel-control-next-icon")).click();
        Thread.sleep(2000);

    }

    //chọn sản phẩm sau đó tăng số lượng bằng nút "+" hoặc giảm bằng nút "-"
    @Test(priority = 6)
    public void TestCase17() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.clickElement(driver, "product-item", 7);

        // thêm 3 sản phẩm
        for (int i = 0; i < 3; i++){
            driver.findElement(By.id("increaseIndex")).click();
            Thread.sleep(1000);
        }
        // giảm 2 sản phẩm
        for (int i = 0; i < 2; i++){
            driver.findElement(By.id("decreaseIndex")).click();
            Thread.sleep(1000);
        }
    }

    //chọn sản phẩm sau đó tăng giảm số lượng thủ công bằng cách nhập vào
    @Test(priority = 7)
    public void TestCase18() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        Helper.clickElement(driver, "product-item", 7);
        Thread.sleep(2000);

        WebElement input = driver.findElement(By.id("inputNumber"));
        input.clear();
        Thread.sleep(2000);
        input.sendKeys("5");
    }


    //đăng nhập sau đó thêm 1 sản phẩm vào giỏ hàng
    @Test(priority = 8)
    public void TestCase19() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1); // Lấy driver hiện tại
        driver.findElement(By.id("btn-login")).click();

        //thao tác đăng nhập vào user
        Helper.login(driver,"0963101750","123123");
        Thread.sleep(3000);

        //chọn sản phẩm thứ 4 trong trang
        Helper.clickElement(driver, "product-item", 3);

        //click nút thêm vào giỏ hàng
        driver.findElement(By.className("btn-success")).click();
    }

    //đăng nhập sau đó mua sản phẩm và điền thông tin đầy đủ
    @Test(priority = 9)
    public void TeseCase20() throws InterruptedException {
        WebDriver driver = drivers.get(drivers.size() - 1);
        driver.findElement(By.id("btn-login")).click();
        //thao tác đăng nhập vào user
        Helper.login(driver,"0345872900","123123");
        Thread.sleep(3000);

        //chọn sản phẩm thứ 2 trong trang
        Helper.clickElement(driver, "product-item", 2);
        Thread.sleep(3000);
        //click nút thêm vào giỏ hàng
        driver.findElement(By.className("btn-success")).click();


        // điền thông tin
        driver.findElement(By.id("fullname")).sendKeys("Bui Thanh Son");
        driver.findElement(By.id("email")).sendKeys("son@gmail.com");
        driver.findElement(By.id("phone")).sendKeys("00123456789");
        driver.findElement(By.id("address")).sendKeys("HCM city");
        Thread.sleep(1000);
        Helper.selectOptionByIndex(driver, "#shippingMethod",1);
        Thread.sleep(1000);
        Helper.selectOptionByIndex(driver, "#paymentMethod",1);
        Thread.sleep(1000);
        driver.findElement(By.className("orderBTN")).click();
        Helper.acceptAlert(driver);
    }




    //Operation full for user
//    @Test
//    public void TestCase2() throws InterruptedException {
//
//        driver.findElement(By.id("btn-login")).click();
//
//        //thao tác đăng nhập vào user
//        Helper.login(driver,"0345872900","123123");
//
//
//        //tìm kiếm sản phẩm hiện có
//        driver.findElement(new By.ByClassName("search-input")).sendKeys("15");
//        Thread.sleep(500);
//        Helper.selectOptionByIndex(driver, ".product-category", 3);
//        driver.findElement(By.id("btn-search")).click();
//        Thread.sleep(500);
//
//        //chọn sản phẩm đầu tiên sau khi tìm kiếm
//        Helper.clickElement(driver, "product-item", 0);
//        Thread.sleep(2000);
//
//        //thêm số lượng sản phẩm
//        driver.findElement(By.id("increaseIndex")).click();
//        driver.findElement(By.className("btn-primary")).click();
//
//        //quay lại trang và tiếp tục mua sắm
//        driver.navigate().back();
//
//        //tìm kiếm sản phẩm thứ 2
//        driver.findElement(By.className("form-control")).sendKeys("air");
//        Thread.sleep(2000);
//        Helper.selectOptionByIndex(driver, ".product-category", 1);
//        driver.findElement(By.id("btn-search")).click();
//        Thread.sleep(2000);
//        Helper.clickElement(driver, "product-item", 3);
//        Thread.sleep(2000);
//        driver.findElement(By.className("btn-primary")).click();
//        Thread.sleep(2000);
//        driver.findElement(By.id("cart")).click();
//        Thread.sleep(2000);
//
//        //điền thông tin để thanh toán 2 sản phẩm đã lựa chọn
//        driver.findElement(By.id("cart")).click();
//        driver.findElement(By.id("fullname")).sendKeys("Luu Bang Thuan");
//        driver.findElement(By.id("email")).sendKeys("thuan@gmail.com");
//        driver.findElement(By.id("phone")).sendKeys("0963101750");
//        driver.findElement(By.id("address")).sendKeys("HCM city");
//        Helper.selectOptionByIndex(driver, "#shippingMethod",0);
//        Helper.selectOptionByIndex(driver, "#paymentMethod",1);
//        // Click nút thanh toán
//        driver.findElement(By.className("orderBTN")).click();
//        Thread.sleep(2000);
//        Helper.acceptAlert(driver);
//
//    }



    @AfterMethod
    public void tearDown(){
        Helper.closeAllBrowser();
    }
}