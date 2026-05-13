package nhom02_foodservice.controller;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import nhom02_foodservice.dtos.requests.FoodCreateRequest;
import nhom02_foodservice.dtos.requests.FoodUpdateRequest;
import nhom02_foodservice.entity.Food;
import nhom02_foodservice.service.FoodService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
public class FoodController {
    private final FoodService service;

    // Tên instance cấu hình trong file application.yml (thay thế cho myService ở bước trước)
    private static final String FOOD_SERVICE = "foodService";

    // 1. Lấy danh sách tất cả món ăn
    @GetMapping
    @RateLimiter(name = FOOD_SERVICE, fallbackMethod = "fallbackForRateLimiter")
    @CircuitBreaker(name = FOOD_SERVICE, fallbackMethod = "fallbackForCircuitBreaker")
    @Retry(name = FOOD_SERVICE, fallbackMethod = "fallbackForRetry")
    public List<Food> getAllFoods() {
        System.out.println("⏳ Đang thử truy vấn Database (có thể bị lỗi)...");

        // Cố tình tạo ra lỗi để ép Resilience4j phải Retry
        if (true) {
            throw new RuntimeException("Cố tình làm mất kết nối Database để test Retry!");
        }
        return service.findAll();
    }

    // --- 1. FALLBACK CHỈ DÀNH CHO RATELIMITER ---
    // Chỉ kích hoạt khi bị lỗi RequestNotPermitted (spam quá nhanh)
    public List<Food> fallbackForRateLimiter(RequestNotPermitted e) {
        System.out.println("❌ RateLimiter chặn: Spam request quá nhanh!");
        return Collections.emptyList();
    }

    // --- 2. FALLBACK CHỈ DÀNH CHO CIRCUIT BREAKER ---
    // Chỉ kích hoạt khi bị lỗi CallNotPermittedException (mạch đang ở trạng thái OPEN)
    public List<Food> fallbackForCircuitBreaker(CallNotPermittedException e) {
        System.out.println("⚠️ CircuitBreaker chặn: Hệ thống đang ngắt mạch để tự vệ!");
        return Collections.emptyList();
    }

    // --- 3. FALLBACK DÀNH CHO RETRY VÀ LỖI HỆ THỐNG ---
    // Bắt Exception chung. Khi có lỗi Database, RateLimiter và CB sẽ bỏ qua để lọt ra đây.
    // Thằng Retry sẽ làm việc, thử lại vài lần không được thì mới nhảy vào hàm này.
    public List<Food> fallbackForRetry(Exception e) {
        System.out.println("🔄 Lỗi hệ thống! Đã Retry hết sức nhưng vẫn thất bại: " + e.getMessage());
        return Collections.emptyList();
    }

    // 2. Thêm một món ăn mới
    @PostMapping
    @RateLimiter(name = FOOD_SERVICE) // Chỉ chống spam, không cần fallback
    public Food createFood(@RequestBody FoodCreateRequest request) {
        return service.save(request);
    }

    // 3. Cập nhật thông tin món ăn
    @PutMapping("/{id}")
    public Food updateFood(@PathVariable Long id, @RequestBody FoodUpdateRequest request) {
        return service.updateFood(id, request);
    }

    // 4. Xóa món ăn
    @DeleteMapping("/{id}")
    public String deleteFood(@PathVariable Long id) {
        service.delete(id);
        return "Đã xóa món ăn có ID: " + id;
    }

    // 5. Tìm món ăn
    @GetMapping("/{id}")
    public Food getById(@PathVariable Long id) {
        return service.findById(id);
    }
}