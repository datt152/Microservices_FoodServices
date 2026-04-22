package nhom02_foodservice.service;

import nhom02_foodservice.entity.Food;
import nhom02_foodservice.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FoodService {
    @Autowired
    private FoodRepository foodRepository;

    public List<Food> findAll() { return foodRepository.findAll(); }

    public Food save(Food food) { return foodRepository.save(food); }

    public Food findById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

    public void delete(Long id) { foodRepository.deleteById(id); }

}
