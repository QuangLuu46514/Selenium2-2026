# Git Commit Guide — Commands & Message Syntax

> Hướng dẫn đầy đủ quy trình commit code và cách viết commit message chuẩn **Conventional Commits** (quy ước commit được nhiều team dùng).

---

## PHẦN 1 — Các lệnh trong quá trình commit

### 1.1 Quy trình commit chuẩn (5 bước)

```bash
# 1. Check where you are
git remote -v                 # Right repo?
git branch                    # Right branch? (NOT main)

# 2. See what changed
git status                    # List of changed / new / deleted files
git diff                      # Line-by-line changes (not staged yet)

# 3. Stage only what belongs to this commit
git add src/main/java/core/driver/DriverManager.java
git add src/test/java/testcases/LoginTest.java

# 4. Review staged changes one more time
git diff --staged

# 5. Commit
git commit -m "feat(driver): add ThreadLocal DriverManager"
```

### 1.2 Lệnh stage (đưa file vào khu vực chờ commit)

| Lệnh | Ý nghĩa |
|---|---|
| `git add <file>` | Stage 1 file |
| `git add <folder>/` | Stage cả thư mục |
| `git add .` | Stage tất cả trong thư mục hiện tại (cẩn thận!) |
| `git add -u` | Chỉ stage file **đã được theo dõi** (bỏ qua file mới) |
| `git add -p` | Stage **từng đoạn** code — hỏi `y/n` cho mỗi đoạn |
| `git add *.java` | Stage theo pattern |
| `git restore --staged <file>` | Bỏ stage (code vẫn giữ nguyên) |
| `git rm <file>` | Xoá file và stage việc xoá |
| `git rm --cached <file>` | Ngừng theo dõi file nhưng **giữ trên máy** (vd lỡ commit `.idea/`) |
| `git mv old.java new.java` | Đổi tên file và stage |

> `git add -p` rất hữu ích khi 1 file có 2 thay đổi thuộc 2 mục đích khác nhau → tách thành 2 commit.

### 1.3 Lệnh commit

| Lệnh | Ý nghĩa |
|---|---|
| `git commit -m "type: subject"` | Commit 1 dòng |
| `git commit -m "subject" -m "body"` | Commit có phần thân (mỗi `-m` = 1 đoạn) |
| `git commit` | Mở editor để viết message nhiều dòng |
| `git commit -am "fix: ..."` | Stage file đã theo dõi + commit (không gồm file mới) |
| `git commit --amend` | Sửa commit cuối (message + thêm file) — **chỉ khi chưa push** |
| `git commit --amend -m "new message"` | Chỉ sửa message commit cuối |
| `git commit --amend --no-edit` | Thêm file quên vào commit cuối, giữ message |
| `git commit --fixup <hash>` | Tạo commit sửa cho 1 commit cũ (dùng với rebase autosquash) |

### 1.4 Kiểm tra sau khi commit

```bash
git log --oneline -5          # Last 5 commits
git show                      # Details of last commit (message + diff)
git show --stat               # Only list files in last commit
git status                    # Should be "nothing to commit, working tree clean"
```

### 1.5 Sửa sai khi commit

| Tình huống | Lệnh |
|---|---|
| Sai message, **chưa push** | `git commit --amend -m "fix: correct message"` |
| Quên 1 file, **chưa push** | `git add <file>` → `git commit --amend --no-edit` |
| Commit nhầm, muốn làm lại, **chưa push** | `git reset --soft HEAD~1` (code vẫn được stage) |
| Commit nhầm branch (đang ở `main`) | `git branch feature/x` → `git reset --hard origin/main` → `git switch feature/x` |
| Sửa commit cũ hơn (chưa push) | `git commit --fixup <hash>` → `git rebase -i --autosquash origin/main` |
| Đã push rồi | `git revert <hash>` — tạo commit mới để đảo ngược |
| Đã amend sau khi push (branch riêng) | `git push --force-with-lease` |

---

## PHẦN 2 — Cú pháp Commit Message (Conventional Commits)

### 2.1 Cấu trúc

```text
<type>(<scope>)<!>: <subject>
                                      ← blank line
<body>
                                      ← blank line
<footer>
```

| Phần | Bắt buộc? | Ý nghĩa |
|---|---|---|
| `type` | ✅ | Loại thay đổi (feat, fix, test...) |
| `(scope)` | ❌ | Phạm vi / module bị ảnh hưởng, đặt trong ngoặc |
| `!` | ❌ | Đánh dấu **breaking change** (thay đổi phá vỡ tương thích) |
| `subject` | ✅ | Mô tả ngắn gọn thay đổi |
| `body` | ❌ | Giải thích **tại sao** và **cái gì** (không phải "như thế nào") |
| `footer` | ❌ | Ticket liên quan, breaking change, co-author |

### 2.2 Danh sách `type`

| Type | Khi nào dùng | Ví dụ trong project Selenium |
|---|---|---|
| `feat` | Thêm chức năng mới cho **framework** | `feat(driver): add Firefox support to DriverFactory` |
| `fix` | Sửa lỗi | `fix(wait): handle StaleElementReferenceException in click` |
| `test` | Thêm / sửa **test case** | `test(login): add invalid password test case` |
| `refactor` | Cấu trúc lại code, **không đổi hành vi** | `refactor(page): extract common methods to BasePage` |
| `perf` | Cải thiện hiệu năng | `perf(wait): reduce polling interval to 200ms` |
| `style` | Format, khoảng trắng, dấu `;` — không đổi logic | `style: apply Google Java format` |
| `docs` | Tài liệu, README, Javadoc | `docs: add setup guide to README` |
| `chore` | Bảo trì, việc lặt vặt không thuộc loại khác | `chore: add gitignore for project` |
| `build` | Build system, dependency (`pom.xml`) | `build(deps): bump selenium-java to 4.50.0` |
| `ci` | Cấu hình CI/CD (GitHub Actions, Jenkins) | `ci: add GitHub Actions workflow for smoke tests` |
| `revert` | Đảo ngược 1 commit trước | `revert: feat(driver): add Firefox support` |

> 🤔 **`feat` hay `test`?** Trong project automation:
> - Viết thêm **test case** → `test`
> - Thêm chức năng cho **framework** (wrapper, utility, listener, report) → `feat`

### 2.3 `scope` gợi ý cho project Selenium

| Scope | Phạm vi |
|---|---|
| `driver` | `DriverManager`, `DriverFactory`, `BrowserOptionsFactory` |
| `browser` | `Browser` wrapper |
| `element` | Element wrapper |
| `config` | `config.properties`, `DriverConfig` |
| `wait` | Wait utilities |
| `page` | Page Objects |
| `login`, `cart`, `product`... | Test theo từng module chức năng |
| `report` | ExtentReports, Allure |
| `suite` | File `testng.xml` |
| `deps` | Dependency trong `pom.xml` |

> Scope là tuỳ chọn. Team thống nhất danh sách scope thì dùng, không thì bỏ qua.

### 2.4 Quy tắc viết `subject`

| Quy tắc | ✅ Đúng | ❌ Sai |
|---|---|---|
| Dùng **động từ nguyên mẫu** (imperative — câu mệnh lệnh) | `add login test` | `added login test` / `adds login test` |
| Viết **thường** chữ đầu | `fix null pointer in getDriver` | `Fix null pointer in getDriver` |
| **Không** có dấu `.` cuối câu | `update README` | `update README.` |
| Ngắn gọn, **≤ 50 ký tự** (tối đa 72) | `add retry for flaky click` | `add a new retry mechanism for the click method because it is flaky sometimes` |
| Viết bằng **tiếng Anh** | `add cart checkout test` | `them test checkout` |
| Mô tả **cái gì thay đổi**, cụ thể | `fix timeout on product page load` | `fix bug` / `update code` / `wip` |

> 💡 Mẹo kiểm tra: câu _"If applied, this commit will **\<subject\>**"_ phải đọc trôi chảy.
> → "If applied, this commit will **add login test**" ✅

### 2.5 Quy tắc viết `body`

- Cách `subject` **1 dòng trống**.
- Mỗi dòng **≤ 72 ký tự**.
- Giải thích **WHY** (tại sao cần) và **WHAT** (thay đổi gì) — code đã cho thấy **HOW**.
- Có thể dùng gạch đầu dòng `-`.

### 2.6 Quy tắc viết `footer`

| Footer | Ý nghĩa |
|---|---|
| `Refs: #12` / `Refs: JIRA-123` | Liên quan tới issue / ticket |
| `Closes #12` / `Fixes #12` | GitHub **tự đóng** issue #12 khi merge |
| `BREAKING CHANGE: <mô tả>` | Thay đổi làm code cũ không chạy được nữa |
| `Co-authored-by: Name <email>` | Commit làm chung với người khác |

---

## PHẦN 3 — Ví dụ thực tế

### 3.1 Commit 1 dòng (đa số trường hợp)

```bash
git commit -m "test(login): add locked user test case"
git commit -m "fix(browser): wait for page load after refresh"
git commit -m "build(deps): add lombok 1.18.34"
git commit -m "chore: remove unused imports"
git commit -m "docs: add git daily workflow guide"
```

### 3.2 Commit có body (bằng nhiều `-m`)

```bash
git commit -m "fix(driver): always remove ThreadLocal in quitDriver" \
           -m "If driver.quit() throws, ThreadLocal.remove() was skipped, causing memory leaks and stale drivers in parallel runs. Moved remove() into a finally block."
```

### 3.3 Commit đầy đủ (mở editor bằng `git commit`)

```text
feat(driver): support remote execution on Selenium Grid

- Add run.mode and run.remote.url to config.properties
- DriverFactory creates RemoteWebDriver when run.mode=remote
- Local mode remains the default

Allows the suite to run in parallel on Grid 4 instead of
only on the local machine.

Refs: #8
```

### 3.4 Breaking change

```text
refactor(driver)!: rename initDriver() to startDriver()

BREAKING CHANGE: all test classes calling DriverManager.initDriver()
must be updated to DriverManager.startDriver().
```

### 3.5 So sánh tốt / xấu

| ❌ Xấu | ✅ Tốt |
|---|---|
| `update` | `refactor(page): move locators to LoginPage` |
| `fix bug` | `fix(element): retry click on ElementClickInterceptedException` |
| `add test` | `test(cart): add remove-item-from-cart test` |
| `WIP` | `wip(login): draft negative test cases` _(chỉ trên branch riêng)_ |
| `Added new feature for login page and fixed some bugs.` | Tách thành 2 commit: `test(login): ...` + `fix(login): ...` |
| `feat: Add Firefox.` | `feat(driver): add Firefox support` |

---

## PHẦN 4 — Thiết lập hỗ trợ

### 4.1 Đặt editor cho commit nhiều dòng

```bash
git config --global core.editor "code --wait"      # VS Code
git config --global core.editor "notepad"          # Windows Notepad
```

### 4.2 Commit template — nhắc cú pháp mỗi lần commit

Tạo file `~/.gitmessage.txt`:

```text
# <type>(<scope>): <subject>   (max 50 chars, imperative, lowercase, no period)
#
# Types: feat | fix | test | refactor | perf | style | docs | chore | build | ci | revert
#
# Body: WHY and WHAT (wrap at 72 chars)
#
# Footer: Refs: #123 | Closes #123 | BREAKING CHANGE: ...
```

```bash
git config --global commit.template ~/.gitmessage.txt
```

> Dòng bắt đầu bằng `#` sẽ bị Git bỏ qua, chỉ dùng để nhắc.

### 4.3 Commit trong IntelliJ IDEA

- Mở tab **Commit** (`Ctrl + K`) → tick file cần commit → xem diff → viết message → **Commit** hoặc **Commit and Push**.
- Click phải 1 file → **Rollback** để bỏ thay đổi.
- Plugin gợi ý: **Conventional Commit** — tự gợi ý type/scope khi viết message.
- Settings → Version Control → Commit → tick **Analyze code** / **Reformat code** trước khi commit.

---

## ✅ Checklist trước mỗi commit

- [ ] Đúng repo (`git remote -v`) và đúng branch (`git branch`) — không phải `main`
- [ ] `git diff --staged` — chỉ chứa thay đổi của **1 mục đích**
- [ ] Code compile, test liên quan chạy pass
- [ ] Không có `System.out.println`, `Thread.sleep` debug, code comment thừa
- [ ] Không có file `target/`, `.idea/`, `reports/`, `*.log`, screenshot
- [ ] Không có password / token / URL cá nhân
- [ ] Message đúng `type(scope): subject` — động từ nguyên mẫu, chữ thường, không dấu chấm
