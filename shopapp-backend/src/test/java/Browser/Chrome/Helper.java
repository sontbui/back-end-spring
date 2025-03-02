package Browser.Chrome;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    private static List<WebDriver> drivers = new ArrayList<>();

    public static void setDriver(WebDriver webDriver) {
        drivers.add(webDriver);
    }

    private static String identifyLocatorType(String locator) {
        if (locator.startsWith(".")) {
            return "className";
        } else if (locator.startsWith("#")) {
            return "id";
        } else {
            return "unknown";
        }
    }

    private static WebElement findElementByLocator(WebDriver driver, String locator) {
        String locatorType = identifyLocatorType(locator);

        if (locatorType.equals("className")) {
            return driver.findElement(By.className(locator.substring(1)));
        } else if (locatorType.equals("id")) {
            return driver.findElement(By.id(locator.substring(1)));
        } else {
            System.out.println("Invalid locator type provided: " + locator);
            return null;
        }
    }

    public static void selectOptionByIndex(WebDriver driver, String locator, int index) {
        WebElement dropdownElement = findElementByLocator(driver, locator);

        if (dropdownElement != null) {
            Select dropdown = new Select(dropdownElement);
            dropdown.selectByIndex(index);
        } else {
            System.out.println("No element found with locator: " + locator);
        }
    }

    public static void clickElement(WebDriver driver, String locator, int index) {
        try {
            List<WebElement> elements = driver.findElements(By.className(locator));
            if (index >= 0 && index < elements.size()) {
                WebElement product = elements.get(index);
                product.click();
            } else {
                System.out.println("Chỉ số index không hợp lệ hoặc không tìm thấy phần tử.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("Không tìm thấy phần tử với locator: " + locator);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fillFieldIfEmpty(WebDriver driver, By by, String value) {
        WebElement element = driver.findElement(by);
        if (element != null && element.getAttribute("value").isEmpty()) {
            element.sendKeys(value);
        }
    }


    public static void login(WebDriver driver, String phone, String password) {
        driver.findElement(By.name("phone")).sendKeys(phone);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.className("login-button")).click();
    }

    public static void register(WebDriver driver, String phone, String pass, String retypePass, String name, String dob, String address) throws InterruptedException {
        driver.findElement(By.name("phone")).sendKeys(phone);
        driver.findElement(By.name("password")).sendKeys(pass);
        driver.findElement(By.name("retypePassword")).sendKeys(retypePass);
        driver.findElement(By.name("fullName")).sendKeys(name);
        driver.findElement(By.name("dateOfBirth")).sendKeys(dob);
        driver.findElement(By.name("address")).sendKeys(address);
        Thread.sleep(2000);
        driver.findElement(By.className("register-button")).click();
    }


    public static void acceptAlert(WebDriver driver) {
        while (true) {
            try {
                Alert alert = driver.switchTo().alert();
                alert.accept();
                // or alert.dismiss(); to cancel
            } catch (Exception e) {
                break;
            }
        }
    }
    public static void dismissAlert(WebDriver driver) {
        while (true) {
            try {
                Alert alert = driver.switchTo().alert();
                alert.dismiss();
            } catch (Exception e) {
                break;
            }
        }
    }

    public static void closeAllBrowser() {
        for (WebDriver driver : drivers) {
            if (driver != null) {
                driver.quit();
            }
        }
        drivers.clear();
    }
}
