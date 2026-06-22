import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebsiteTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String BASE_URL = "https://www.saucedemo.com/";

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    @Order(1)
    @DisplayName("Test Case 01: Đăng nhập thành công")
    public void testLogin() {
        driver.get(BASE_URL);

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("inventory"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory"), "Đăng nhập thất bại!");
        System.out.println("-> Hoàn thành TC01: Đăng nhập thành công.");
    }

    @Test
    @Order(2)
    @DisplayName("Test Case 02: Thêm sản phẩm vào giỏ hàng")
    public void testAddToCart() {
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")));
        addToCartBtn.click();

        WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='shopping-cart-badge']")));

        Assertions.assertEquals("1", cartBadge.getText(), "Thêm vào giỏ hàng thất bại!");
        System.out.println("-> Hoàn thành TC02: Thêm sản phẩm vào giỏ hàng thành công.");
    }

    @Test
    @Order(3)
    @DisplayName("Test Case 03: Đăng xuất khỏi hệ thống")
    public void testLogout() {
        WebElement menuBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn")));
        menuBtn.click();

        WebElement logoutLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logout_sidebar_link")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", logoutLink);

        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        Assertions.assertTrue(loginBtn.isDisplayed(), "Đăng xuất thất bại!");
        System.out.println("-> Hoàn thành TC03: Đăng xuất thành công bằng kỹ thuật JS Executor.");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}