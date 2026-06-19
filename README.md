# Quiz Lập Trình Kotlin

Android quiz app viết bằng Kotlin, XML Layout và mô hình MVVM cơ bản.

## Chức năng đã có

- Màn hình chính với tiêu đề "Quiz Lập Trình Kotlin".
- Chọn chủ đề: Kotlin Basics, Kotlin Nâng Cao hoặc Android UI.
- Nút Bắt đầu và Xem điểm cao nhất.
- 10 câu hỏi mỗi lượt, mỗi câu có 4 lựa chọn và 1 đáp án đúng.
- Random thứ tự câu hỏi trong chủ đề.
- Timer 30 giây cho mỗi câu.
- Feedback ngay sau khi chọn hoặc hết giờ.
- Tính điểm dạng `8/10 - 80%`.
- Tính thời gian hoàn thành.
- Lưu điểm cao nhất bằng SharedPreferences.
- Màn kết quả có nút Làm lại và Về trang chủ.
- RecyclerView review lại từng câu hỏi sau khi làm xong.
- Hỗ trợ Light/Dark mode bằng theme `values` và `values-night`.

## Kiến trúc

- `data`: model câu hỏi, repository dữ liệu tĩnh, SharedPreferences high score.
- `ui/home`: màn hình chính và `HomeViewModel`.
- `ui/quiz`: màn làm bài, timer, scoring và `QuizViewModel`.
- `ui/result`: màn kết quả, `ResultViewModel`, `RecyclerView.Adapter`.

## Cách chạy

1. Mở thư mục này bằng Android Studio.
2. Chọn Sync Gradle.
3. Chạy app trên emulator hoặc thiết bị Android.

Nếu Android Studio hỏi tải Gradle/Android Gradle Plugin lần đầu, chọn đồng ý để IDE tải dependency.
