package digital.valorantsoftware.workspace.project;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    public ProjectController() {

        System.out.println("## ");

        System.out.println("## init > ProjectController");

        System.out.println("## ");

    }

    @GetMapping("/health")
    public String health() {

        return "OK";

    }

    @GetMapping("")
    public String hello() {

        return "ProjectController";

    }

    @GetMapping("/")
    public List<Project> findAll() {

        System.out.println("enter > findAllProjects");

        List<Project> projectList = this.projectService.findAll();

        System.out.println("projectList / ");

        System.out.println(projectList);

        System.out.println("exit < findAllProjects");

        return projectList;

    }

    @PutMapping("/")
    Project createProject(@RequestBody Project project) {

        System.out.println("enter > createProject");

        System.out.println("project / " + project);

        return this.projectService.save(project);

    }

    @GetMapping("/{id}")
    Project findById(@PathVariable Long id) {

        return this.projectService.findById(id);

    }

    @PostMapping("/update")
    public Project updateProject(@RequestBody Project project) {

        System.out.println("enter > updateProject");

        System.out.println("project / " + project);

        Project responseProject = this.projectService.save(project);

        System.out.println("responseProject / " + responseProject);

        System.out.println("exit < updateProject");

        return responseProject;

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {

        this.projectService.delete(id);

    }

}
