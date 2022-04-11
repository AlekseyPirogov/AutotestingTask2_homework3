package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Задача №2: Задача 2. Выберите любой публичный сайт, не попадающий под NDA, который вы используете в течение всего курса.
 * Автоматизируйте два любых сценария, например, авторизация на сайте и создание новой записи. Абсолютный минимум — один
 * тест на каждый позитивный сценарий (без негативных проверок).
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {

        // Задержки для функций
        int delay = 1300, delayAfterClose = 1900;

        // Набор тест-кейсов для автоматизированного тестирования:
        userRegistration(delay, delayAfterClose);            // <-- тест-кейс №1 регистрация пользователя (пользователям запрещен вход)
        userRegistrationAndExit(delay, delayAfterClose);     // <-- тест-кейс №2 регистрация пользователя и выход
        workWithGUI(delay, delayAfterClose);                 // <-- тест-кейс №3 работа с пользовательским интерфейсом
        sortItem(delay, delayAfterClose);                    // <-- тест-кейс №4 сортировка товаров витрины
        aadItemInBasket(delay, delayAfterClose);             // <-- тест-кейс №5 обавление товаров в корзину
        createOrder(delay,delayAfterClose);                    // <-- тест-кейс №6 создание заказа
    }

    // Метод для инициализации драйвера браузера Google Chrome
    public static WebDriver initWebDriver(Boolean incognito) {
        // Набор настроек для браузера:
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (incognito)
            options.addArguments("--incognito");
        //options.addArguments("--headless");
        options.addArguments("start-maximized");
        // Создание объекта типа WebDriver с заданными опциями
        WebDriver driver = new ChromeDriver(options);
        // Переход по ссылке
        driver.get("https://www.saucedemo.com/");
        // Установка стандартной задержки (неявное ожидание) для браузера
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        return driver;
    }

    // Метод для проверки страницы на наличие элемента
    public static void itemIsAvailable(WebDriver driver, String xpathElement) {
        // Вывод в консоль информации о работе функции
        try {
            // Установка задержки (явного ожидания) перед каждым обращением к элементу
            WebDriverWait webDriver = new WebDriverWait(driver, 2);
            webDriver.until(ExpectedConditions.elementToBeSelected(By.xpath(xpathElement)));
            driver.findElement(By.xpath(xpathElement));
        } catch (WebDriverException e){
            System.out.println(e.getMessage());
            System.out.println(e.getSupportUrl());
        }
        // Проверка условия, что xpath(xpathElement)).size() не пуст
        if(driver.findElements(By.xpath(xpathElement)).size()>0) {
            System.out.println("\nЭлемент c xpath: " + xpathElement + " найден - " + (driver.findElements(By.xpath(xpathElement)).size() > 0));
        }
    }

    // Тест-кейс №1. Добавление пользователя (заблокированный и отсутствующий пользователь)
    public static void userRegistration(int delay, int closeDelay) throws InterruptedException {
        // Инициализация драйвера браузера
        WebDriver driver = initWebDriver(true);
        // Объявление переменной типа WebElement для адресации элемента страницы:
        WebElement webElement;
        // Объявление переменной типа String для хранения xpath элемента страницы:
        String xpathElement;

        // Заполнение текстовых полей для аутентификации пользователя
        // с использованием xPath:
        xpathElement = "//input[@placeholder='Username']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("locked_out_user");

        xpathElement = "//input[@placeholder='Password']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("secret_sauce");

        // Аутентификация пользователя
        xpathElement = "//input[@value='Login']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();

        // Нажатие кнопки с ошибкой для сброса аутентификации
        xpathElement = "//h3[@data-test='error']/button";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        Thread.sleep(delay);
        webElement.click();

        // Заполнение текстовых полей для аутентификации пользователя
        // с использованием CSS:
        String cssSelector;
        cssSelector = "input[id='user-name']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.cssSelector(cssSelector));
        webElement.click();
        webElement.clear();
        webElement.sendKeys("nobody");

        cssSelector = "input[id='password']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.cssSelector(cssSelector));
        webElement.click();
        webElement.clear();
        webElement.sendKeys("zero");

        // Аутентификация пользователя
        cssSelector = "input[id='login-button']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.cssSelector(cssSelector));
        Thread.sleep(delay);
        webElement.click();

        Thread.sleep(closeDelay);
        driver.close();     // <-- закрытие страницы
        //driver.quit();    // <-- закрытие браузера
    }

    // Тест-кейс №2. Регистрация пользователя и выход из витрины
    public static void userRegistrationAndExit(int delay, int closeDelay) throws InterruptedException{
        // Инициализация драйвера браузера
        WebDriver driver = initWebDriver(true);
        // Объявление переменной типа WebElement для адресации элемента страницы:
        WebElement webElement;
        // Объявление переменной типа String для хранения xpath элемента страницы:
        String xpathElement;

        // Заполнение текстовых полей для аутентификации пользователя
        // с использованием xPath:
        xpathElement = "//input[@placeholder='Username']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("performance_glitch_user");

        xpathElement = "//input[@placeholder='Password']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("secret_sauce");

        // Аутентификация пользователя
        xpathElement = "//input[@value='Login']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        Thread.sleep(delay);
        webElement.click();

        xpathElement = "//button[@id='react-burger-menu-btn']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay);

        xpathElement = "//a[contains(text(),'Logout')]";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay);

        Thread.sleep(closeDelay);
        driver.close();     // <-- закрытие страницы
        //driver.quit();    // <-- закрытие браузера
    }

    // Тест-кейс №3. Работа с пользовательским интерфейсом
    public static void workWithGUI(int delay, int closeDelay) throws InterruptedException {
        // Инициализация драйвера браузера
        WebDriver driver = initWebDriver(true);
        // Объявление переменной типа WebElement для адресации элемента страницы:
        WebElement webElement;
        // Объявление переменной типа String для хранения xpath элемента страницы:
        String xpathElement;

        // Заполнение текстовых полей для аутентификации пользователя
        // с использованием xPath:
        xpathElement = "//input[@placeholder='Username']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("standard_user");

        xpathElement = "//input[@placeholder='Password']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("secret_sauce");

        // Аутентификация пользователя
        xpathElement = "//input[@value='Login']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay);

        // Скролл странцы, переход в футер
        xpathElement = "//div[@id='page_wrapper']/footer/div";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay);

        // Переход в главное меню
        xpathElement = "//button[@id='react-burger-menu-btn']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay);

        // Закрытие главного меню
        xpathElement = "//button[@id='react-burger-cross-btn']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay/2);

        // Проверка доступности меню сортировки товаров на странице витрины
        // Сортировка в прямом алфавитном порядке
        xpathElement = "//div[@id='header_container']/div[2]/div[2]/span/select/option[@value='az']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 50);

        // Сортировка в обратном алфавитном порядке
        xpathElement = "//div[@id='header_container']/div[2]/div[2]/span/select/option[@value='za']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 50);

        // Переход в корзину
        xpathElement = "//div[@id='shopping_cart_container']/a";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Выход из корзины
        xpathElement = "//button[@id='continue-shopping']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 150);

        // Просмотр товара
        xpathElement = "//a[@id='item_4_img_link']/img";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 750);

        // Возврат к покупкам
        xpathElement = "//button[@id='back-to-products']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 150);

        // Просмотр товара
        xpathElement = "//a[@id='item_3_img_link']/img";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 750);

        // Переход в коризину
        xpathElement = "//div[@id='shopping_cart_container']/a";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Переход в главное меню
        xpathElement = "//button[@id='react-burger-menu-btn']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 150);

        // Выход из витрины
        xpathElement = "//*[@id='logout_sidebar_link']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 150);

        Thread.sleep(closeDelay);
        driver.close();     // <-- закрытие страницы
        //driver.quit();    // <-- закрытие браузера
    }

    // Тест-кейс №4. Сортировка товаров витрины
    public static void  sortItem(int delay, int closeDelay) throws InterruptedException {

        // Инициализация драйвера браузера
        WebDriver driver = initWebDriver(true);
        // Объявление переменной типа WebElement для адресации элемента страницы:
        WebElement webElement;
        // Объявление переменной типа String для хранения xpath элемента страницы:
        String xpathElement;

        // Заполнение текстовых полей для аутентификации пользователя
        // с использованием xPath:
        xpathElement = "//input[@placeholder='Username']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("standard_user");

        xpathElement = "//input[@placeholder='Password']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("secret_sauce");

        // Аутентификация пользователя
        xpathElement = "//input[@value='Login']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();

        // Проверка доступности меню сортировки товаров на странице витрины
        // Сортировка в прямом алфавитном порядке
        xpathElement = "//*[@value='az']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Сортировка в обратном алфавитном порядке
        xpathElement = "//*[@value='za']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Сортировка в порядке убывания цены
        xpathElement = "//*[@value='hilo']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Сортировка в порядке возрастания цены
        xpathElement = "//*[@value='lohi']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        Thread.sleep(closeDelay);
        driver.close();     // <-- закрытие страницы
        //driver.quit();    // <-- закрытие браузера
    }

    // Тест-кейс №5. Добавление товаров в корзину
    public static void aadItemInBasket(int delay, int closeDelay) throws InterruptedException {
        // Инициализация драйвера браузера
        WebDriver driver = initWebDriver(false);
        // Объявление переменной типа WebElement для адресации элемента страницы:
        WebElement webElement;
        // Объявление переменной типа String для хранения xpath элемента страницы:
        String xpathElement;

        // Заполнение текстовых полей для аутентификации пользователя
        // с использованием xPath:
        xpathElement = "//input[@placeholder='Username']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("standard_user");

        xpathElement = "//input[@placeholder='Password']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("secret_sauce");

        // Аутентификация пользователя
        xpathElement = "//input[@value='Login']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();

        // Добавление товаров витрины в корзину
        xpathElement = "//*[@id='add-to-cart-sauce-labs-backpack']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 50);

        xpathElement = "//*[@id='add-to-cart-sauce-labs-onesie']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 50);

        xpathElement = "//*[@id='add-to-cart-sauce-labs-fleece-jacket']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 50);

        // Переход в корзину
        xpathElement = "//div[@id='shopping_cart_container']/a";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Удаление товаров
        xpathElement = "//button[@id='remove-sauce-labs-onesie' and contains(.,'Remove')]";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        xpathElement = "//button[contains(.,'Remove') and @id='remove-sauce-labs-backpack']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Возврат к покупкам
        xpathElement = "//*[@id='continue-shopping']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 150);

        xpathElement = "//button[@id='add-to-cart-sauce-labs-bike-light']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 150);

        // Переход в корзину
        xpathElement = "//div[@id='shopping_cart_container']/a";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Переход в главное меню
        xpathElement = "//button[@id='react-burger-menu-btn']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 150);

        // Выход из витрины
        xpathElement = "//*[@id='logout_sidebar_link']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 150);

        // Заполнение текстовых полей для аутентификации пользователя
        // с использованием xPath:
        xpathElement = "//input[@placeholder='Username']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("standard_user");

        xpathElement = "//input[@placeholder='Password']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("secret_sauce");

        // Аутентификация пользователя
        xpathElement = "//input[@value='Login']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();

        // Переход в корзину
        xpathElement = "//div[@id='shopping_cart_container']/a";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Удаление товаров
        xpathElement = "//button[contains(.,'Remove') and @id='remove-sauce-labs-fleece-jacket']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        xpathElement = "//button[contains(.,'Remove') and @id='remove-sauce-labs-bike-light']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        Thread.sleep(closeDelay);
        driver.close();     // <-- закрытие страницы
        //driver.quit();    // <-- закрытие браузера
    }

    // Тест-кейс №6. Оформление заказа
    public static void createOrder(int delay, int closeDelay) throws InterruptedException {
        // Инициализация драйвера браузера
        WebDriver driver = initWebDriver(true);
        // Объявление переменной типа WebElement для адресации элемента страницы:
        WebElement webElement;
        // Объявление переменной типа String для хранения xpath элемента страницы:
        String xpathElement;

        // Заполнение текстовых полей для аутентификации пользователя
        // с использованием xPath:
        xpathElement = "//input[@placeholder='Username']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("standard_user");

        xpathElement = "//input[@placeholder='Password']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("secret_sauce");

        // Аутентификация пользователя
        xpathElement = "//input[@value='Login']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();

        // Добавление товаров витрины в корзину
        xpathElement = "//*[@id='add-to-cart-sauce-labs-onesie']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 50);

        xpathElement = "//*[@id='add-to-cart-sauce-labs-fleece-jacket']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 50);

        // Переход в корзину
        xpathElement = "//div[@id='shopping_cart_container']/a";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Переход к оформлению заказа
        xpathElement = "//button[@id='checkout']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        // Заполнить тектовые поля с информацией о новом пользователе:
        xpathElement = "//input[@id='first-name']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("Ivan");

        xpathElement = "//input[@id='last-name']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("Ivanov");

        xpathElement = "//input[@id='postal-code']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        webElement.sendKeys("E77777");
        Thread.sleep(delay + 550);

        xpathElement = "//input[@id='continue']";
        itemIsAvailable(driver, xpathElement);
        driver.findElement(By.xpath(xpathElement)).click();
        Thread.sleep(delay + 250);

        // Скролл странцы, переход в футер
        xpathElement = "//div[@id='page_wrapper']/footer/div";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();

        xpathElement = "//button[@id='finish']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();

        xpathElement = "//*[@id='header_container']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();
        Thread.sleep(delay + 550);

        xpathElement = "//button[@id='back-to-products']";
        itemIsAvailable(driver, xpathElement);
        webElement = driver.findElement(By.xpath(xpathElement));
        webElement.click();

        Thread.sleep(closeDelay);
        driver.close();     // <-- закрытие страницы
        //driver.quit();    // <-- закрытие браузера
    }
}