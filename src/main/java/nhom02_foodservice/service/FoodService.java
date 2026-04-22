package nhom02_foodservice.service;

import lombok.AllArgsConstructor;
import nhom02_foodservice.dtos.requests.FoodCreateRequest;
import nhom02_foodservice.dtos.requests.FoodUpdateRequest;
import nhom02_foodservice.entity.Food;
import nhom02_foodservice.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;

    public List<Food> findAll() { return foodRepository.findAll(); }

    public Food save(FoodCreateRequest request) {
        Food food = Food.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();
        return foodRepository.save(food);
    }
    public Food updateFood(Long id, FoodUpdateRequest request) {
        Food food = findById(id);
        if(food == null)
            throw new RuntimeException("Food not found");
        food.setName(request.getName());
        food.setPrice(request.getPrice());

        foodRepository.save(food);

        return foodRepository.save(food);
    }

    public Food findById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

    public void delete(Long id) { foodRepository.deleteById(id); }

}
