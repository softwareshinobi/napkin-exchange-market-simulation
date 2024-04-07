package digital.valorantsoftware.workspace.project;

import jakarta.transaction.Transactional;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = {SQLException.class})
public class ProjectService {

    ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {

        this.projectRepository = projectRepository;

    }

    public Project save(Project project) {

        return this.projectRepository.save(project);

    }

    public List<Project> findAll() {

        return this.projectRepository.findAll();

    }

    public Project findById(Long id) {

        return this.projectRepository.findById(id).orElse(null);

    }

    public void delete(Long id) {

        this.projectRepository.deleteById(id);

    }

}
