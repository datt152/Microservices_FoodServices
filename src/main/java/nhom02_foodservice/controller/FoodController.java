package nhom02_foodservice.controller;

import nhom02_foodservice.entity.Food;
import nhom02_foodservice.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodService service;

    // 1. Lấy danh sách tất cả món ăn
    @GetMapping
    public List<Food> getAllFoods() {
        return service.findAll();
    }

    // 2. Thêm một món ăn mới
    @PostMapping
    public Food createFood(@RequestBody Food food) {
        return service.save(food);
    }

    // 3. Cập nhật thông tin món ăn
    @PutMapping("/{id}")
    public Food updateFood(@PathVariable Long id, @RequestBody Food foodDetails) {
        Food food = service.findById(id);
        food.setName(foodDetails.getName());
        food.setPrice(foodDetails.getPrice());
        return service.save(food);
    }

    // 4. Xóa món ăn
    @DeleteMapping("/{id}")
    public String deleteFood(@PathVariable Long id) {
        service.delete(id);
        return "Đã xóa món ăn có ID: " + id;
    }
    // 5. Tim mon an
    @GetMapping("/{id}")
    public Food getById(@PathVariable Long id) {
        return service.findById(id);
    }
}