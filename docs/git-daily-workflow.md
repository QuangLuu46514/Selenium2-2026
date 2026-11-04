# Git Daily Workflow — Best Practices

> Quy trình làm việc hằng ngày với Git trong team (áp dụng cho repo nhóm `Selenium_Level2`).
> Mục tiêu: **luôn code trên nền mới nhất, ít conflict, không mất code, PR sạch.**

---

## Tổng quan 1 ngày

```
🌅 Đầu ngày        →  💻 Trong ngày          →  🌙 Cuối ngày        →  🚀 Khi xong task
fetch, pull main      commit nhỏ, thường xuyên    commit + push branch    rebase, test, tạo PR
cập nhật branch       sync main nếu cần           ghi chú việc dở         dọn branch sau merge
```

---

## 🌅 1. Đầu ngày — Đồng bộ trước khi code

### Bước 1: Đứng đúng repo

```bash
cd D:/projects/Selenium_Level2
git remote -v          # Confirm: ankhuong3107/Selenium_Level2 (team) — not personal repo
git status             # Must be clean, or know what's pending
```

> Có code dở từ hôm qua chưa commit? → `git stash -u` trước, xong bước 3 thì `git stash pop`.

### Bước 2: Cập nhật `main` mới nhất

```bash
git fetch --prune      # Get latest info + remove deleted remote branches
git switch main
git pull               # main is now identical to GitHub
```

> Không bao giờ code trên `main`. `main` local chỉ dùng để **lấy code mới**.

### Bước 3a: Tiếp tục task cũ — đưa code mới của `main` vào branch của bạn

```bash
git switch feature/login-test
git rebase main        # Put your commits on top of latest main
# or: git merge main   # If team prefers merge, or branch is shared with others
```

- Có conflict → sửa file → `git add <file>` → `git rebase --continue`.
- Đã rebase một branch **đã push** → lần push sau dùng `git push --force-with-lease`.

### Bước 3b: Bắt đầu task mới — tạo branch từ `main` mới nhất

```bash
git switch main
git pull
git switch -c feature/product-review-test
```

### Bước 4: Kiểm tra project vẫn chạy

```bash
mvn clean compile      # Or: mvn clean test -Dtest=SmokeTest
```

> Nếu `main` mới kéo về đã lỗi build → báo team ngay, đừng code chồng lên.

---

## 💻 2. Trong ngày — Code & commit

### Commit nhỏ, thường xuyên

```bash
git status
git diff                         # Review what you changed
git add src/test/java/.../LoginTest.java
git commit -m "test: add valid login test case"
```

| ✅ Nên | ❌ Không nên |
|---|---|
| Mỗi commit = 1 mục đích rõ ràng | 1 commit "update code" gộp cả ngày |
| Message theo `type: description` | Message `fix`, `abc`, `update` |
| `git diff` trước khi `add` | `git add .` mù quáng |
| Chạy test liên quan trước khi commit | Commit code không compile |
| Push branch ít nhất 1 lần/ngày | Giữ code chỉ trên máy cả tuần |

### Cần sửa gấp việc khác giữa chừng?

```bash
git stash -u -m "wip: login test"
git switch main && git pull
git switch -c bugfix/urgent-issue
# ... fix, commit, push, PR ...
git switch feature/login-test
git stash pop
```

### Task kéo dài nhiều ngày?

Sync `main` vào branch **ít nhất 1 lần/ngày** (bước 3a) → conflict nhỏ, dễ xử lý thay vì dồn 1 lần lớn.

---

## 🌙 3. Cuối ngày — Không để code chỉ nằm trên máy

### Bước 1: Commit hết những gì đang làm

```bash
git status
git add .
git commit -m "wip: login test - pending negative cases"
```

> Commit `wip:` (work in progress — đang làm dở) là OK trên branch riêng. Trước khi tạo PR có thể gộp lại (xem mục 4).

### Bước 2: Push branch lên GitHub (backup)

```bash
git push               # First time: git push -u origin feature/login-test
```

> Máy hỏng / mất máy / nghỉ ốm → code vẫn còn trên GitHub, người khác có thể tiếp tục.

### Bước 3: Kiểm tra lại

```bash
git status             # "nothing to commit, working tree clean"
git log --oneline -3   # Today's commits
git branch -vv         # Branch is in sync with origin (no "ahead")
```

### Bước 4: Ghi chú cho ngày mai (tuỳ chọn)

- Cập nhật task trên Jira / board nhóm.
- Hoặc comment trong PR draft: _"Done: valid login. TODO: invalid password, locked user."_

---

## 🚀 4. Khi xong task — Tạo Pull Request

### Bước 1: Cập nhật với `main` mới nhất

```bash
git fetch
git rebase origin/main
```

### Bước 2 (tuỳ chọn): Dọn commit `wip:` cho gọn

```bash
git reset --soft origin/main     # Keep all changes, remove your commits
git commit -m "test: add login test cases (valid, invalid, locked user)"
```

> Hoặc chọn **Squash and merge** khi merge PR trên GitHub — cách đơn giản hơn.

### Bước 3: Tự kiểm tra trước khi gửi

```bash
mvn clean test -Dtest=LoginTest    # Tests pass locally
git diff origin/main --stat        # Only files related to this task
```

Checklist:
- [ ] Không có `System.out.println`, `Thread.sleep` debug, code comment thừa
- [ ] Không commit `target/`, `.idea/`, `reports/`, file log, screenshot
- [ ] Không hard-code password / URL môi trường cá nhân
- [ ] Tên class, method, locator rõ nghĩa

### Bước 4: Push & tạo PR

```bash
git push --force-with-lease      # Needed if you rebased/squashed after a previous push
```

Trên GitHub → **Compare & pull request**:

```markdown
**Title:** [Test] Add login test cases

**What:** Add 3 login test cases: valid, invalid password, locked user.
**Why:** Cover login module requirements from TC-LOGIN-01..03.
**Test:** `mvn clean test -Dtest=LoginTest` → 3/3 passed on Chrome.
**Evidence:** (screenshot of report)
```

- Tự xem tab **Files changed** trước khi gửi reviewer.
- Reviewer comment → sửa trên **cùng branch** → commit → push → PR tự cập nhật.

### Bước 5: Sau khi PR được merge — Dọn dẹp

```bash
git switch main
git pull
git branch -d feature/login-test   # Delete local branch
git fetch --prune                  # Clean up deleted remote branches
```

---

## 📌 Quy tắc vàng

1. **Fetch & pull trước khi switch / tạo branch.**
2. **Không code, không push trực tiếp lên `main`.**
3. **Một branch = một task. Một commit = một mục đích.**
4. **Push branch mỗi ngày** — GitHub là bản backup.
5. **Sync `main` vào branch thường xuyên** để conflict nhỏ.
6. **`git remote -v` + `git branch` trước khi push** — nhất là khi có nhiều repo.
7. **Chỉ `--force-with-lease` trên branch của riêng mình**, không bao giờ trên `main` / branch chung.
8. **Tự review `Files changed` trước khi gửi PR.**

---

## ⚡ Cheat sheet siêu ngắn

```bash
# Start of day
git fetch --prune && git switch main && git pull
git switch feature/my-task && git rebase main

# During the day
git add <files> && git commit -m "type: message"

# End of day
git add . && git commit -m "wip: ..." && git push

# Finish task
git fetch && git rebase origin/main
mvn clean test
git push --force-with-lease   # → open PR on GitHub

# After merge
git switch main && git pull && git branch -d feature/my-task
```
