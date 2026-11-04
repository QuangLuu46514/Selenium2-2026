# Git Commands Cheat Sheet

> Bảng tra cứu nhanh các lệnh Git hay dùng trong project Selenium thực tế.
> ⚠️ = lệnh có thể làm mất code, đọc kỹ trước khi chạy.

---

## 0. Kiến thức nền

```
Working directory  --git add-->  Staging area  --git commit-->  Local repo  --git push-->  Remote (GitHub)
(thư mục đang code)              (khu vực chờ commit)           (kho trên máy)              (kho trên GitHub)
```

- **Repo** (kho chứa code) = 1 thư mục có folder ẩn `.git`. Đổi repo = đổi thư mục (`cd`).
- **Branch** (nhánh) = một dòng phát triển riêng trong cùng repo. Đổi branch = `git switch`.
- **Remote** (kho từ xa) — tên mặc định là `origin`.
- **HEAD** = commit/branch bạn đang đứng.

---

## 1. Cấu hình (config)

| Lệnh | Ý nghĩa |
|---|---|
| `git --version` | Kiểm tra phiên bản Git |
| `git config --global user.name "Your Name"` | Đặt tên tác giả commit (mọi repo) |
| `git config --global user.email "you@mail.com"` | Đặt email (nên trùng email GitHub) |
| `git config user.email "you@company.com"` | Config **local** — chỉ repo hiện tại, ưu tiên hơn global |
| `git config --list --show-origin` | Xem toàn bộ config và nằm ở file nào |
| `git config --global --unset user.name` | Xoá 1 giá trị config |
| `git config --global --edit` | Mở file config để sửa tay |
| `git config --global init.defaultBranch main` | Branch mặc định khi `git init` là `main` |

> Đổi **tài khoản đăng nhập** GitHub trên Windows: Credential Manager → Windows Credentials → xoá `git:https://github.com`.

---

## 2. Khởi tạo & lấy code

| Lệnh | Ý nghĩa |
|---|---|
| `git init` | Tạo repo mới trong thư mục hiện tại |
| `git clone <url>` | Tải 1 repo từ GitHub về (tạo thư mục mới) |
| `git clone <url> <folder-name>` | Clone vào thư mục với tên tuỳ chọn |
| `git remote -v` | Xem repo đang trỏ tới remote nào |
| `git remote add origin <url>` | Liên kết repo local với GitHub |
| `git remote set-url origin <url>` | Đổi URL remote (chuyển sang repo khác) |
| `git remote remove origin` | Xoá liên kết remote |
| `git remote add <name> <url>` | Thêm remote thứ 2 (push 1 code lên nhiều repo) |

Tạo repo bằng terminal (không cần vào web) với GitHub CLI:

```bash
gh auth login
gh repo create my-repo --private --source=. --remote=origin --push
```

---

## 3. Xem trạng thái & lịch sử

| Lệnh | Ý nghĩa |
|---|---|
| `git status` | File nào đã sửa / mới / đã stage |
| `git diff` | Thay đổi **chưa** stage |
| `git diff --staged` | Thay đổi **đã** stage, sắp commit |
| `git diff main..feature/x` | So sánh 2 branch |
| `git log --oneline --graph -10` | 10 commit gần nhất, dạng gọn + sơ đồ nhánh |
| `git log --oneline --graph --all` | Lịch sử tất cả branch |
| `git log -p <file>` | Lịch sử thay đổi của 1 file |
| `git show <hash>` | Xem chi tiết 1 commit |
| `git blame <file>` | Ai sửa từng dòng |

---

## 4. Stage & Commit

| Lệnh | Ý nghĩa |
|---|---|
| `git add <file>` | Stage 1 file |
| `git add .` | Stage tất cả thay đổi trong thư mục hiện tại |
| `git add -p` | Stage từng đoạn code (chọn lọc) |
| `git commit -m "feat: add login test"` | Commit với message |
| `git commit --amend` | Sửa commit cuối (message / thêm file) — **chỉ khi chưa push** |
| `git commit --amend --no-edit` | Thêm file vào commit cuối, giữ nguyên message |

### Quy ước commit message (Conventional Commits)

| Type | Ý nghĩa | Ví dụ |
|---|---|---|
| `feat:` | Thêm chức năng mới | `feat: add Browser wrapper` |
| `fix:` | Sửa lỗi | `fix: handle stale element exception` |
| `test:` | Thêm / sửa test case | `test: add product review test` |
| `refactor:` | Cấu trúc lại code, không đổi hành vi | `refactor: extract wait helpers` |
| `chore:` | Bảo trì, config, dependency | `chore: add gitignore for project` |
| `docs:` | Tài liệu | `docs: update project setup guide` |

> Một commit = một mục đích.

---

## 5. Branch

| Lệnh | Ý nghĩa |
|---|---|
| `git branch` | Liệt kê branch local (`*` = đang đứng) |
| `git branch -a` | Bao gồm cả branch trên remote |
| `git branch -vv` | Xem branch local đang track branch remote nào |
| `git switch <branch>` | Chuyển sang branch có sẵn |
| `git switch -c feature/login-page` | Tạo branch mới và chuyển sang |
| `git switch -c feature/x origin/feature/x` | Lấy branch của người khác trên remote về làm |
| `git checkout -b feature/x` | Cách cũ, tương đương `switch -c` |
| `git branch -M main` | Đổi tên branch hiện tại thành `main` |
| `git branch -d feature/x` | Xoá branch đã merge |
| `git branch -D feature/x` | ⚠️ Xoá branch kể cả chưa merge |
| `git push origin --delete feature/x` | Xoá branch trên GitHub |

**Đặt tên branch:** `type/task-name` → `feature/login-api`, `bugfix/payment-error`, `test/product-review`.

---

## 6. Đồng bộ với remote

| Lệnh | Ý nghĩa |
|---|---|
| `git fetch` | Tải thông tin mới từ remote, **không** đụng vào code của bạn (an toàn) |
| `git fetch --prune` | Fetch + dọn các branch remote đã bị xoá |
| `git pull` | `fetch` + `merge` vào branch hiện tại |
| `git pull --rebase` | `fetch` + `rebase` (lịch sử thẳng, gọn hơn) |
| `git push -u origin feature/x` | Push lần đầu, `-u` = liên kết upstream |
| `git push` | Các lần push sau |
| `git push --force-with-lease` | ⚠️ Force push an toàn (sau khi rebase branch **của riêng bạn**) |

> Không bao giờ `push --force` lên `main` hoặc branch dùng chung.

---

## 7. Merge & Rebase

| Lệnh | Ý nghĩa |
|---|---|
| `git merge main` | Gộp `main` vào branch hiện tại (tạo merge commit) |
| `git rebase main` | Đặt lại commit của bạn lên đỉnh `main` (lịch sử thẳng) |
| `git rebase origin/main` | Rebase lên `main` mới nhất trên remote (sau `fetch`) |
| `git rebase --continue` | Tiếp tục sau khi giải quyết conflict |
| `git rebase --abort` | Huỷ rebase, quay về như cũ |
| `git merge --abort` | Huỷ merge |

### Xử lý conflict (xung đột)

```text
<<<<<<< HEAD
your code
=======
their code
>>>>>>> main
```

1. Mở file, giữ phần đúng, xoá các dấu `<<<<<<<`, `=======`, `>>>>>>>`.
2. `git add <file>`
3. `git commit` (nếu đang merge) hoặc `git rebase --continue` (nếu đang rebase).

> IntelliJ: **Git → Resolve Conflicts** có giao diện 3 cột rất dễ dùng.

---

## 8. Stash (cất tạm code đang làm dở)

| Lệnh | Ý nghĩa |
|---|---|
| `git stash` | Cất tạm thay đổi chưa commit |
| `git stash -u` | Cất cả file mới (untracked) |
| `git stash push -m "wip login"` | Cất tạm có ghi chú |
| `git stash list` | Xem danh sách stash |
| `git stash pop` | Lấy lại stash mới nhất và xoá khỏi danh sách |
| `git stash apply stash@{1}` | Lấy lại stash cụ thể, vẫn giữ trong danh sách |
| `git stash drop stash@{0}` | Xoá 1 stash |

---

## 9. Hoàn tác (undo)

| Tình huống | Lệnh |
|---|---|
| Bỏ thay đổi chưa stage của 1 file | `git restore <file>` ⚠️ |
| Bỏ stage (vẫn giữ code) | `git restore --staged <file>` |
| Huỷ commit cuối, giữ code đã stage | `git reset --soft HEAD~1` |
| Huỷ commit cuối, giữ code chưa stage | `git reset HEAD~1` |
| Huỷ commit cuối **và xoá code** | `git reset --hard HEAD~1` ⚠️ |
| Huỷ 1 commit **đã push** (an toàn) | `git revert <hash>` |
| Lỡ commit `target/`, `.idea/` | `git rm -r --cached target .idea` rồi commit lại |
| Tìm lại commit "đã mất" | `git reflog` |

> **Chưa push** → có thể dùng `reset`. **Đã push** → dùng `revert`.

---

## 10. Lệnh hữu ích khác

| Lệnh | Ý nghĩa |
|---|---|
| `git cherry-pick <hash>` | Copy 1 commit cụ thể vào branch hiện tại |
| `git tag v1.0.0` + `git push --tags` | Đánh dấu phiên bản release |
| `git clean -fd` | ⚠️ Xoá file/folder chưa được Git theo dõi |
| `git clean -nd` | Xem trước `clean` sẽ xoá gì (không xoá thật) |

---

## 11. `.gitignore` mẫu cho project Selenium Maven

```gitignore
# Build output
target/
*.class

# Test results - generated on every run, never commit
reports/
test-output/
allure-results/
*.log
screenshots/

# IDE
.idea/
*.iml
.vscode/
.settings/
.project
.classpath

# OS
.DS_Store
Thumbs.db
```

---

## 12. Lỗi hay gặp

| Lỗi | Nguyên nhân & cách xử lý |
|---|---|
| `Repository not found` | Chưa accept lời mời / sai URL / sai tài khoản đăng nhập |
| `remote origin already exists` | Dùng `git remote set-url origin <url>` |
| `rejected ... fetch first` | Remote có commit mới → `git pull --rebase` rồi push lại |
| `refusing to merge unrelated histories` | Repo GitHub tạo kèm README → `git pull origin main --allow-unrelated-histories` |
| `protected branch hook declined` | Đang push thẳng lên `main` → tạo branch riêng + Pull Request |
| `not a git repository` | Đứng sai thư mục → `cd` vào đúng thư mục repo |
