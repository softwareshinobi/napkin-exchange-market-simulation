package digital.valorantsoftware.workspace.story;

import jakarta.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = {SQLException.class})
public class UserStoryService {

    UserStoryRepository userStoryRepository;

    @Autowired
    public UserStoryService(UserStoryRepository userStoryRepository) {

        this.userStoryRepository = userStoryRepository;

    }

    public UserStory save(UserStory project) {

        return this.userStoryRepository.save(project);

    }

    public List< UserStory> findAll() {

        return this.userStoryRepository.findAll();

    }

    public UserStory findById(Long id) {

        return this.userStoryRepository.findById(id).orElse(null);

    }

    public List<UserStory> findByStatus(String status) {

        return this.userStoryRepository.findByStatus(status);

    }

    public void delete(Long id) {

        this.userStoryRepository.deleteById(id);

    }

}
