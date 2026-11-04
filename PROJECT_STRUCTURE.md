# Cấu trúc Source Code – Selenium Automation Framework

> Tài liệu giải thích cấu trúc, nhiệm vụ từng phần và luồng chạy của project
> `selenium-automation-framework` (Java 21 · Selenium 4.50.0 · TestNG 7.12.0 · Maven).

---

## 1. Tổng quan cây thư mục

```
Selenium/
├── pom.xml                         ← Khai báo thư viện (dependency) & cách build/run
├── src/
│   ├── main/java/core/             ← TẦNG FRAMEWORK (core – dùng lại được cho mọi web)
│   │   ├── common/constant/
│   │   │   └── TimeoutConstants.java
│   │   ├── config/
│   │   │   └── DriverConfig.java
│   │   ├── driver/
│   │   │   ├── BrowserOptionsFactory.java
│   │   │   ├── DriverFactory.java
│   │   │   ├── DriverManager.java
│   │   │   └── Browser.java
│   │   └── enums/
│   │       ├── BrowserType.java
│   │       ├── DriverKey.java
│   │       └── RunMode.java
│   │
│   └── test/
│       ├── java/                   ← TẦNG TEST (gắn với website cụ thể)
│       │   ├── common/
│       │   │   ├── common/         → Random.java, Utilities.java
│       │   │   └── constant/       → Constant.java, CreateAccountFields.java, MailType.java, Menu.java
│       │   ├── pageobjects/
│       │   │   ├── automationexercise/  → GeneralPage, HomePage, LoginPage, SignUpPage,
│       │   │   │                          ProductPage, ProductDetailsPage, ViewCartPage, CheckoutPage
│       │   │   └── guerrillamail/       → GurreillamailPage
│       │   ├── testcases/
│       │   │   └── automationexercise/  → TestBase, ProductReviewTest, RegistrationAddressTest
│       │   ├── businessflow/       ← (đang trống – dự kiến chứa các flow nghiệp vụ)
│       │   └── dataobjects/        ← (đang trống – dự kiến chứa model dữ liệu test)
│       └── resources/
│           ├── config.properties   ← Cấu hình chạy (browser, headless, local/remote…)
│           └── suites/testng.xml   ← Bộ test (suite) để TestNG chạy
└── target/                         ← Output khi build (file .class) – KHÔNG commit lên Git
```

**Ý tưởng chính:** project chia làm 2 tầng rõ ràng:

| Tầng | Vị trí | Biết gì? | Dùng lại được không? |
|---|---|---|---|
| **Core / Framework** | `src/main/java/core` | Chỉ biết về *trình duyệt* (mở, đóng, điều hướng, chờ…) | Có – mang sang web khác vẫn dùng được |
| **Test project** | `src/test/java` | Biết về *website cụ thể* (automationexercise, guerrillamail) | Không – gắn chặt với web đang test |

---

## 2. Sơ đồ kiến trúc (các lớp gọi nhau thế nào)

```
┌──────────────────────────────────────────────────────────────┐
│  testng.xml  ──(parameter browser)──►  TestBase              │  TẦNG TEST
│                                         │  @BeforeMethod      │
│  RegistrationAddressTest / ProductReviewTest (extends TestBase)│
│        │ gọi                                                  │
│        ▼                                                      │
│  Page Objects (HomePage, LoginPage, ...)  ◄── Constant / enums │
│        │ gọi                                                  │
│        ▼                                                      │
│  Utilities (click, enter, wait...)       Browser (navigate...)│
└────────┬──────────────────────────────────────┬──────────────┘
         │ DriverManager.getDriver()            │
┌────────▼──────────────────────────────────────▼──────────────┐
│  DriverManager  (ThreadLocal<WebDriver>)                      │  TẦNG CORE
│        │ initDriver()                                         │
│        ▼                                                      │
│  DriverFactory ──► BrowserOptionsFactory                      │
│        │                 ▲                                    │
│        └──── DriverConfig ◄── config.properties               │
│                  (dùng BrowserType, RunMode, DriverKey)       │
└──────────────────────────────────────────────────────────────┘
```

---

## 3. File cấu hình & build

### 3.1 `pom.xml`
File cấu hình của **Maven** (công cụ quản lý thư viện và build project Java).

| Thành phần | Vai trò |
|---|---|
| `selenium-java` 4.50.0 | Thư viện điều khiển trình duyệt |
| `testng` 7.12.0 | Test framework: `@Test`, `@BeforeMethod`, chạy song song (parallel)… |
| `extentreports` 5.1.2 | Tạo báo cáo HTML – **đã khai báo nhưng chưa dùng** |
| `slf4j-api` + `logback-classic` | Ghi log (`log.info(...)`) |
| `lombok` (scope `provided`) | Tự sinh code lặp lại: `@Getter`, `@Setter`, `@Slf4j` |
| `maven-compiler-plugin` | Compile với Java 21 |
| `maven-surefire-plugin` | Plugin chạy test khi gõ `mvn test` |

> 💡 **Lombok nhắc lại:** `@Getter` trên class → tự sinh `getXxx()` / `isXxx()` (với boolean) cho mọi field.
> `@Slf4j` → tự sinh `private static final Logger log = ...` nên bạn dùng được `log.info()` mà không cần khai báo.

### 3.2 `src/test/resources/config.properties`
Nơi **tập trung cấu hình** – đổi cách chạy mà không phải sửa code.

| Key | Ý nghĩa |
|---|---|
| `run.mode` | `local` (chạy trên máy) hoặc `remote` (chạy qua Selenium Grid) |
| `run.remote.url` | URL của Grid khi `run.mode=remote` |
| `browser` | Browser mặc định: `chrome` / `firefox` / `edge` |
| `browser.headless` | `true` = chạy ẩn, không hiện cửa sổ browser |
| `browser.<tên>.disable-notifications` | Tắt popup thông báo của browser |
| `browser.<tên>.headless-argument` | Tham số headless riêng cho từng browser |

### 3.3 `src/test/resources/suites/testng.xml`
**Suite** (bộ test) cho TestNG: chọn class/method nào chạy, truyền `parameter browser`, cấu hình `parallel` (chạy song song) và `thread-count` (số luồng).
Hiện tại: chạy `ProductReviewTest.TC01` trên **firefox**, `parallel="methods"` nhưng `thread-count="1"` → thực tế vẫn chạy tuần tự.

---

## 4. Tầng CORE – `src/main/java/core`

Đây là "trái tim" của framework, tương ứng **Module 02 – Driver Wrapper** trong slide.

### 4.1 `enums/` – Các kiểu dữ liệu cố định

| File | Nhiệm vụ |
|---|---|
| `BrowserType` | `CHROME, FIREFOX, EDGE`. Hàm `from("chrome")` đổi chuỗi → enum, không phân biệt hoa thường; sai tên thì ném lỗi rõ ràng kèm danh sách hợp lệ. |
| `RunMode` | `LOCAL, REMOTE`, cũng có hàm `from(...)` tương tự. |
| `DriverKey` | Gom **tên các key** trong `config.properties` vào 1 chỗ (`RUN_MODE("run.mode")`…) → tránh gõ sai chuỗi rải rác trong code. |

> 💡 **Enum** (kiểu liệt kê): tập hợp giá trị cố định. Dùng enum thay cho String giúp compiler bắt lỗi khi gõ sai, và `switch` trên enum rất gọn.

### 4.2 `config/DriverConfig.java` – Đọc cấu hình
- Constructor đọc file `config.properties` từ **classpath** (đường dẫn mà Java tìm resource – ở đây là thư mục `resources`).
- Mỗi giá trị có **default** (giá trị mặc định) nếu key bị thiếu/trống – ví dụ thiếu `browser` thì dùng `chrome`.
- Chuyển chuỗi sang đúng kiểu: `RunMode`, `BrowserType`, `boolean`.
- Field `browser` có `@Setter` → cho phép **ghi đè** browser bằng tham số từ `testng.xml` (các field khác là `final`, không đổi được).
- Dùng `try-with-resources` (`try (InputStream input = ...)`) → stream tự đóng sau khi đọc xong.

### 4.3 `common/constant/TimeoutConstants.java`
Các mốc thời gian dùng chung cho core: `PAGE_LOAD_TIMEOUT` = 30s, `WAIT_TIMEOUT` = 15s, `POLLING_INTERVAL` = 250ms (cứ 250ms kiểm tra lại điều kiện 1 lần).
Constructor `private` → không ai `new TimeoutConstants()` được (class tiện ích chỉ chứa hằng số).

### 4.4 `driver/BrowserOptionsFactory.java` – Tạo Options cho browser
Theo **Factory pattern** (mẫu thiết kế "nhà máy": 1 hàm nhận đầu vào và quyết định tạo object loại nào).
- `buildOptions(config)` → trả về `ChromeOptions` / `FirefoxOptions` / `EdgeOptions` tùy browser.
- Thêm `--disable-notifications`, tham số headless… dựa trên config.
- Kiểu trả về chung là `MutableCapabilities` (lớp cha của mọi `XxxOptions`).

### 4.5 `driver/DriverFactory.java` – Tạo WebDriver
Trách nhiệm: **cách tạo** một browser (không lưu trữ driver).
- `LOCAL` → `new ChromeDriver(options)` / `FirefoxDriver` / `EdgeDriver`.
- `REMOTE` → `new RemoteWebDriver(url, options)` để chạy trên **Selenium Grid** (hệ thống chạy test phân tán trên nhiều máy).
- Bắt `SessionNotCreatedException` và ném lại lỗi dễ hiểu ("kiểm tra browser đã cài chưa, version có hợp không").
- Không cần set đường dẫn chromedriver: **Selenium Manager** (có sẵn từ Selenium 4.6+) tự tìm/tải driver phù hợp.

### 4.6 `driver/DriverManager.java` – Quản lý driver theo từng luồng
Thành phần quan trọng nhất để **chạy song song (parallel)**.

```java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
```

> 💡 **ThreadLocal**: một "cái hộp" mà **mỗi thread (luồng chạy) có ngăn riêng**. Thread A `set()` driver Chrome, thread B `set()` driver Firefox → khi `get()`, mỗi thread chỉ lấy được driver của chính nó → các test song song không giành browser của nhau.

| Hàm | Gọi ở đâu | Làm gì |
|---|---|---|
| `initDriver(browser)` | `@BeforeMethod` | Đọc config → ghi đè browser (nếu có) → nhờ `DriverFactory` tạo driver → `DRIVER.set()` |
| `getDriver()` | Mọi nơi (Page, Utilities, Browser) | Lấy driver của thread hiện tại; chưa init thì báo lỗi rõ ràng (*fail fast* – báo lỗi sớm) |
| `quitDriver()` | `@AfterMethod` | `quit()` browser và **luôn** `DRIVER.remove()` trong `finally` → tránh memory leak (rò rỉ bộ nhớ) |

### 4.7 `driver/Browser.java` – Browser Wrapper
**Wrapper** (lớp bọc): bọc các thao tác cấp trình duyệt để code ở Page Object ngắn gọn và luôn có log + wait.

| Nhóm | Hàm |
|---|---|
| Điều hướng | `navigateTo`, `refresh`, `back`, `forward`, `maximize`, `getCurrentUrl`, `getTitle` |
| Chờ trang | `waitForPageLoaded()` – chờ `document.readyState == "complete"` (tự gọi sau mỗi lần điều hướng) |
| Alert | `waitForAlert`, `acceptAlert`, `dismissAlert`, `getAlertText` |
| Tab / Window | `openNewTab`, `switchToWindow(index)`, `switchToWindow(title)`, `switchToLastWindow`, `closeCurrentWindow` |
| Khác | `executeScript` (chạy JavaScript), `takeScreenshot` (chụp màn hình → `byte[]`) |

Mọi hàm đều gọi `driver()` → `DriverManager.getDriver()` nên **an toàn khi chạy song song**.

---

## 5. Tầng TEST – `src/test/java`

### 5.1 `common/common/` – Tiện ích dùng chung

| File | Nhiệm vụ |
|---|---|
| `Random` | Sinh chuỗi/số ngẫu nhiên cho test data (`randomString(min,max)`, `randomNumber(...)`) |
| `Utilities` | Thao tác **cấp element**: `findElement`, `click`, `enter` (clear + sendKeys), `getText`, `hover`, `select`, `scrollToElement`, `waitForVisible`, `waitForClickable`, xử lý table (`getHeaderIndexMap`, `getCellText`). Hỗ trợ **dynamic xpath** (xpath động) qua `String.format(locator, args)`. |

> 💡 `Utilities` đóng vai trò như một **Element Wrapper** đơn giản: mỗi lần `click`/`enter` đều tự *wait → scroll → thao tác*, giảm lỗi do element chưa sẵn sàng (flaky test – test lúc pass lúc fail).

### 5.2 `common/constant/` – Hằng số & enum của nghiệp vụ

| File | Nhiệm vụ |
|---|---|
| `Constant` | URL các trang, dữ liệu test (tên, email, password, address…), timeout cho tầng test |
| `CreateAccountFields` | Map tên field → `id` HTML của form tạo tài khoản (dùng với xpath động `//input[@id='%s']`) |
| `MailType` | Tiêu đề email cần tìm trong hộp thư (xác nhận tài khoản, reset password) |
| `Menu` | Text của các menu (Home, Products, Signup / Login) |

### 5.3 `pageobjects/` – Page Object Model (POM)

> 💡 **Page Object Model**: mỗi trang web = 1 class. Class chứa **locator** (cách tìm element) và **method** (hành động người dùng trên trang). Test case chỉ gọi method, không đụng tới locator → khi UI đổi, chỉ sửa 1 chỗ.

**`automationexercise/`**

| Class | Nhiệm vụ |
|---|---|
| `GeneralPage` | **Lớp cha** của mọi page: chứa phần chung (menu). `gotoPage(Menu)` click menu và trả về đúng Page tương ứng (dùng **generic** `<T extends GeneralPage>` để tự ép kiểu). |
| `HomePage` | `open()` mở trang chủ, `addProductToCart()` thêm sản phẩm ngẫu nhiên, `clickViewCart()` |
| `LoginPage` | `signUp(name, email)` → trả về `SignUpPage` |
| `SignUpPage` | `creatNewAccount(...)` điền form đăng ký → trả về `HomePage` |
| `ProductPage` | `selectProduct(index)` mở chi tiết sản phẩm |
| `ProductDetailsPage` | Điền & gửi review, kiểm tra form/message hiển thị |
| `ViewCartPage` | `clickProceedToCheckout()` |
| `CheckoutPage` | `getDeliveryAddress()` lấy địa chỉ giao hàng để verify |

Nhiều method **trả về Page tiếp theo** (ví dụ `signUp()` → `SignUpPage`) → viết test theo kiểu **fluent/chaining** (nối chuỗi): `homePage.open().gotoPage(Menu.SIGNUP_LOGIN)`.

**`guerrillamail/GurreillamailPage`** – Page của web mail tạm: mở tab mới, đặt tên hộp thư, chờ email tới (refresh lặp đến `MAX_WAIT_TIMEOUT`) rồi click link xác nhận, sau đó quay lại tab cuối.

### 5.4 `testcases/` – Test case

| Class | Nhiệm vụ |
|---|---|
| `TestBase` | Lớp cha của mọi test. `@BeforeMethod` → `DriverManager.initDriver(browser)`; `@AfterMethod(alwaysRun = true)` → `quitDriver()`. Nhận browser từ `testng.xml` qua `@Parameters({"browser"})`, mặc định `@Optional("chrome")`. Ghi log START/END mỗi test. |
| `ProductReviewTest` | `TC01`: hiện mới mở trang chủ (đang viết dở). |
| `RegistrationAddressTest` | `TC02`: Đăng ký tài khoản → thêm sản phẩm vào giỏ → checkout → **verify** địa chỉ giao hàng khớp địa chỉ đã đăng ký. |

> 💡 `alwaysRun = true`: `@AfterMethod` vẫn chạy kể cả khi test fail/exception → browser luôn được đóng.

### 5.5 `businessflow/` và `dataobjects/` (đang trống)
- **businessflow**: gom nhiều bước của nhiều page thành 1 flow nghiệp vụ (vd `RegisterFlow.registerNewUser(user)`) để test ngắn hơn và tái sử dụng.
- **dataobjects**: class mô tả dữ liệu (vd `User { name, email, password, address… }`) thay vì truyền 8 tham số rời rạc như `creatNewAccount(...)` hiện nay.

---

## 6. Luồng chạy 1 test (end-to-end)

```
mvn test / Run testng.xml
   │
   ├─1. TestNG đọc testng.xml → lấy parameter browser="firefox"
   ├─2. TestBase.setUp("firefox")  (@BeforeMethod)
   │      └─ DriverManager.initDriver("firefox")
   │            ├─ new DriverConfig()          → đọc config.properties
   │            ├─ setBrowser(FIREFOX)         → ghi đè browser
   │            ├─ DriverFactory.createDriver()
   │            │     ├─ BrowserOptionsFactory.buildOptions() → FirefoxOptions
   │            │     └─ run.mode=local → new FirefoxDriver(options)
   │            └─ DRIVER.set(driver)          → gắn vào thread hiện tại
   ├─3. @Test TC01 chạy
   │      └─ homePage.open() → Browser.navigateTo(url) → DriverManager.getDriver().get(url)
   └─4. TestBase.tearDown()  (@AfterMethod, alwaysRun)
          └─ DriverManager.quitDriver() → quit() + DRIVER.remove()
```

---

## 7. Có thể gọi source code này là "framework" không?

**Có – nhưng là một framework ở giai đoạn đầu (framework nền / skeleton).**

### Vì sao gọi là framework được
Một *test automation framework* không chỉ là "vài test chạy được", mà là **bộ khung có kiến trúc, quy ước và thành phần dùng lại** để người khác viết test trên đó. Project này đã có:

1. **Tách tầng rõ ràng**: `core` (không biết gì về website) tách khỏi `testql` (gắn website cụ thể). Mang `core` sang test web khác vẫn dùng được.
2. **Quản lý driver chuẩn**: Factory tạo driver, `ThreadLocal` sẵn sàng chạy song song, teardown an toàn chống leak.
3. **Cấu hình ngoài code**: đổi browser / headless / local–remote chỉ bằng `config.properties` hoặc `testng.xml`.
4. **Hỗ trợ cross-browser & Selenium Grid** (Chrome/Firefox/Edge, `RemoteWebDriver`).
5. **Wrapper + Page Object Model + TestBase**: người viết test không phải lo setup/teardown, wait, scroll.
6. **Logging** tập trung.

### Vì sao mới là "giai đoạn đầu"
So với một framework hoàn chỉnh (mức senior), còn thiếu:

| Thiếu | Ghi chú |
|---|---|
| **Reporting** | ExtentReports đã có trong pom nhưng chưa tích hợp; chưa có **Listener** (`ITestListener` – lớp lắng nghe sự kiện test) để chụp screenshot khi fail |
| **Element Wrapper trong core** | Logic element đang nằm ở `Utilities` phía test, chưa đưa vào `core` |
| **Data-driven** | Chưa có `@DataProvider` / đọc JSON-Excel; `dataobjects` còn trống |
| **Business flow** | `businessflow` còn trống |
| **Retry, CI/CD, config theo môi trường** | Chưa có `IRetryAnalyzer`, pipeline, profile dev/staging |

👉 Cách gọi chính xác: **`src/main/java/core` là framework**, còn `src/test/java` là **test project xây trên framework đó**. Ở các dự án lớn, phần `core` thường được tách thành module/thư viện riêng để nhiều team dùng chung.

---

## 8. Một số điểm nên lưu ý / cải thiện

1. **Package không khớp thư mục**: ví dụ `pageobjects/automationexercise/HomePage.java` khai báo `package automationexercise;` (thiếu `pageobjects.`), `common/common/Random.java` khai báo `package common;`. Đang chạy được do IntelliJ đánh dấu các thư mục con là *source root*, nhưng sai chuẩn Java và dễ lỗi khi build bằng Maven/CI. Nên sửa thành `package pageobjects.automationexercise;`, `package common.common;` (hoặc đổi tên thư mục cho khớp).
2. **Test data `static final` trong `Constant`** (EMAIL, PASSWORD, ADDRESS…) chỉ được sinh **1 lần khi JVM chạy** → chạy nhiều test/song song sẽ dùng chung 1 email → đăng ký trùng. Nên sinh data mới trong từng test (hoặc qua `dataobjects`).
3. `MOBILE_PHONE = Random.randomNumber(10)` chỉ ra số 0–9, không giống số điện thoại thật.
4. **Hai hệ timeout song song**: `TimeoutConstants` (core, `Duration`) và `Constant.TIMEOUT` (test, `int`). Nên dùng thống nhất `TimeoutConstants`.
5. `GurreillamailPage.waitAndClickConfirmEmail`: vòng `while` refresh liên tục không nghỉ → tốn tài nguyên, nên dùng `WebDriverWait`/`FluentWait` hoặc thêm khoảng nghỉ.
6. `SignUpPage` dùng `Utilities.enter()` (sendKeys) cho thẻ `<select>` Country → nên dùng `selectByVisibleText`.
7. `DriverConfig` default `browser.headless` = `"true"` trong code, nhưng file config đang `false` → cẩn thận khi thiếu key, test sẽ chạy ẩn.
8. Trong `BrowserOptionsFactory.buildOptions`, nhánh `default` là thừa vì `switch` trên enum đã phủ hết các giá trị (Java 21 tự kiểm tra).
9. Thư mục `target/` và file `.iml`, `.idea/` không nên commit – kiểm tra `.gitignore` (Module 01).
10. `testng.xml`: `parallel="methods"` + `thread-count="1"` = không song song thật. Khi muốn cross-browser, tham khảo `testng-crossbrowser.xml` trong slide Module 02 (`parallel="tests"`, `thread-count="3"`).
