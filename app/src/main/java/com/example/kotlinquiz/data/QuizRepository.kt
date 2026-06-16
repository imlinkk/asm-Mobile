package com.example.kotlinquiz.data

object QuizRepository {
    private val questions = listOf(
        QuizQuestion(
            id = 1001,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "Trong Kotlin, khai báo nào tạo biến chỉ được gán một lần?",
            options = listOf("var", "val", "lateinit", "fun"),
            correctIndex = 1,
            explanation = "val tạo biến chỉ gán một lần, phù hợp với dữ liệu không đổi."
        ),
        QuizQuestion(
            id = 1002,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "data class thường tự sinh nhóm hàm nào?",
            options = listOf(
                "equals, hashCode, toString và copy",
                "onCreate, onStart và onStop",
                "inflate, bind và recycle",
                "insert, update và delete"
            ),
            correctIndex = 0,
            explanation = "data class giúp giảm boilerplate bằng cách tự sinh các hàm hay dùng cho model dữ liệu."
        ),
        QuizQuestion(
            id = 1003,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "Toán tử safe-call ?. dùng để làm gì?",
            options = listOf(
                "Ép kiểu dữ liệu",
                "Chạy vòng lặp an toàn",
                "Gọi thuộc tính/hàm và trả về null nếu đối tượng đang null",
                "Tạo biến toàn cục"
            ),
            correctIndex = 2,
            explanation = "Safe-call giúp tránh NullPointerException khi receiver có thể null."
        ),
        QuizQuestion(
            id = 1004,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "Toán tử Elvis ?: thường dùng trong tình huống nào?",
            options = listOf(
                "Đổi tên package",
                "Cung cấp giá trị mặc định khi biểu thức bên trái là null",
                "Khai báo coroutine scope",
                "Tạo RecyclerView.Adapter"
            ),
            correctIndex = 1,
            explanation = "a ?: b nghĩa là dùng a nếu a không null, ngược lại dùng b."
        ),
        QuizQuestion(
            id = 1005,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "Hàm nào tạo một List chỉ đọc trong Kotlin?",
            options = listOf("arrayListOf", "mutableListOf", "hashMapOf", "listOf"),
            correctIndex = 3,
            explanation = "listOf trả về List chỉ đọc; nếu cần thêm/xóa phần tử, dùng mutableListOf."
        ),
        QuizQuestion(
            id = 1006,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "when trong Kotlin thường thay thế cấu trúc nào của Java?",
            options = listOf("switch", "try-with-resources", "synchronized", "volatile"),
            correctIndex = 0,
            explanation = "when linh hoạt hơn switch và có thể dùng như một expression trả về giá trị."
        ),
        QuizQuestion(
            id = 1007,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "lateinit var phù hợp nhất với trường hợp nào?",
            options = listOf(
                "Biến primitive như Int",
                "Biến val không đổi",
                "Biến non-null sẽ được gán sau, trước khi sử dụng",
                "Biến local trong hàm lambda"
            ),
            correctIndex = 2,
            explanation = "lateinit dùng cho var non-null được khởi tạo muộn, thường gặp khi setup dependency hoặc view."
        ),
        QuizQuestion(
            id = 1008,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "sealed class hữu ích nhất khi cần biểu diễn điều gì?",
            options = listOf(
                "Một tập trạng thái hữu hạn và kiểm soát được",
                "Một database table",
                "Một layout responsive",
                "Một file resource ảnh"
            ),
            correctIndex = 0,
            explanation = "sealed class giúp mô hình hóa các trạng thái như Loading, Success, Error rõ ràng và an toàn."
        ),
        QuizQuestion(
            id = 1009,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "Hàm map trên collection dùng để làm gì?",
            options = listOf(
                "Lọc phần tử theo điều kiện",
                "Biến đổi từng phần tử thành giá trị mới",
                "Sắp xếp danh sách",
                "Xóa phần tử trùng"
            ),
            correctIndex = 1,
            explanation = "map trả về collection mới sau khi áp dụng phép biến đổi cho từng phần tử."
        ),
        QuizQuestion(
            id = 1010,
            topic = QuizTopic.KOTLIN_BASICS,
            question = "Từ khóa suspend trong Kotlin liên quan nhiều nhất đến khái niệm nào?",
            options = listOf("Extension function", "Coroutine", "Companion object", "Enum"),
            correctIndex = 1,
            explanation = "suspend đánh dấu hàm có thể tạm dừng và tiếp tục trong coroutine mà không chặn thread."
        ),
        QuizQuestion(
            id = 2001,
            topic = QuizTopic.ANDROID_UI,
            question = "Activity trong Android thường đại diện cho thành phần nào?",
            options = listOf(
                "Một màn hình/tác vụ giao diện",
                "Một dòng dữ liệu trong SQLite",
                "Một file drawable",
                "Một dependency Gradle"
            ),
            correctIndex = 0,
            explanation = "Activity là điểm vào cho một màn hình tương tác với người dùng."
        ),
        QuizQuestion(
            id = 2002,
            topic = QuizTopic.ANDROID_UI,
            question = "Intent thường dùng để làm gì?",
            options = listOf(
                "Tự động tạo layout XML",
                "Chuyển màn hình hoặc gửi dữ liệu giữa các component",
                "Tối ưu bộ nhớ RecyclerView",
                "Đổi màu theme ban đêm"
            ),
            correctIndex = 1,
            explanation = "Intent mô tả hành động cần thực hiện, ví dụ mở Activity và truyền extra."
        ),
        QuizQuestion(
            id = 2003,
            topic = QuizTopic.ANDROID_UI,
            question = "RecyclerView cần thành phần nào để hiển thị danh sách?",
            options = listOf("Adapter và ViewHolder", "Service và BroadcastReceiver", "Manifest và Gradle", "Timer và Thread"),
            correctIndex = 0,
            explanation = "Adapter tạo/bind item view; ViewHolder giữ reference view để tái sử dụng hiệu quả."
        ),
        QuizQuestion(
            id = 2004,
            topic = QuizTopic.ANDROID_UI,
            question = "ConstraintLayout bố trí view chủ yếu dựa vào điều gì?",
            options = listOf("SQL query", "Constraint giữa các view/parent", "Kotlin sealed class", "SharedPreferences key"),
            correctIndex = 1,
            explanation = "ConstraintLayout dùng ràng buộc top, bottom, start, end... để tạo layout linh hoạt."
        ),
        QuizQuestion(
            id = 2005,
            topic = QuizTopic.ANDROID_UI,
            question = "SharedPreferences phù hợp để lưu dữ liệu nào?",
            options = listOf(
                "Video dung lượng lớn",
                "Dữ liệu key-value nhỏ như điểm cao nhất",
                "Danh sách hàng nghìn bản ghi quan hệ",
                "File APK"
            ),
            correctIndex = 1,
            explanation = "SharedPreferences phù hợp cho cấu hình và dữ liệu nhỏ, ví dụ high score."
        ),
        QuizQuestion(
            id = 2006,
            topic = QuizTopic.ANDROID_UI,
            question = "ViewModel trong MVVM giúp ích chính ở điểm nào?",
            options = listOf(
                "Tạo icon launcher",
                "Giữ và xử lý state UI tách khỏi Activity",
                "Thay thế hoàn toàn layout XML",
                "Nén ảnh trong drawable"
            ),
            correctIndex = 1,
            explanation = "ViewModel giữ logic/state và sống qua thay đổi cấu hình như xoay màn hình."
        ),
        QuizQuestion(
            id = 2007,
            topic = QuizTopic.ANDROID_UI,
            question = "LiveData có đặc điểm nào quan trọng?",
            options = listOf(
                "Không thể observe từ Activity",
                "Tự động lifecycle-aware khi observe với LifecycleOwner",
                "Chỉ dùng được trong Java",
                "Luôn lưu dữ liệu vào disk"
            ),
            correctIndex = 1,
            explanation = "LiveData chỉ cập nhật observer khi LifecycleOwner ở trạng thái phù hợp."
        ),
        QuizQuestion(
            id = 2008,
            topic = QuizTopic.ANDROID_UI,
            question = "onCreate của Activity thường dùng để làm gì?",
            options = listOf(
                "Khởi tạo giao diện và state ban đầu",
                "Xóa app khỏi thiết bị",
                "Build Gradle project",
                "Publish app lên Play Store"
            ),
            correctIndex = 0,
            explanation = "onCreate là nơi thường gọi setContentView, bind view và thiết lập observer/listener."
        ),
        QuizQuestion(
            id = 2009,
            topic = QuizTopic.ANDROID_UI,
            question = "Để hỗ trợ màu riêng cho dark mode bằng resource XML, thư mục nào thường được dùng?",
            options = listOf("values-night", "drawable-large", "mipmap-anydpi", "raw-debug"),
            correctIndex = 0,
            explanation = "Android tự chọn resource trong values-night khi app chạy ở chế độ tối."
        ),
        QuizQuestion(
            id = 2010,
            topic = QuizTopic.ANDROID_UI,
            question = "Trong RecyclerView, ListAdapter + DiffUtil giúp gì?",
            options = listOf(
                "Tắt lifecycle",
                "Cập nhật danh sách hiệu quả hơn bằng cách so sánh item",
                "Tạo Intent implicit",
                "Lưu theme bằng SharedPreferences"
            ),
            correctIndex = 1,
            explanation = "DiffUtil tính thay đổi giữa danh sách cũ và mới để RecyclerView cập nhật mượt hơn."
        )
    )

    private val questionById = questions.associateBy { it.id }

    fun getTopics(): List<QuizTopic> = QuizTopic.values().toList()

    fun getQuizQuestions(topicId: String, randomize: Boolean = true): List<QuizQuestion> {
        val topic = QuizTopic.fromId(topicId)
        val topicQuestions = questions.filter { it.topic == topic }
        return if (randomize) topicQuestions.shuffled().take(10) else topicQuestions.take(10)
    }

    fun getQuestionsByIds(ids: IntArray): List<QuizQuestion> {
        return ids.map { questionById[it] }.filterNotNull()
    }
}
