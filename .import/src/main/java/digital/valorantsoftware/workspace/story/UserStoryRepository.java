package digital.valorantsoftware.workspace.story;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {

    List<UserStory> findByIdAllIgnoreCase(Long id);

    //List<UserStory> findByNameIgnoreCase(String name);
    List<UserStory> findByStatus(String status);

}
