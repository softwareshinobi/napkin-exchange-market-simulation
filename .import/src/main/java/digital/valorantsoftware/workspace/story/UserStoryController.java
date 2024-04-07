package digital.valorantsoftware.workspace.story;

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
@RequestMapping("story")
public class UserStoryController {

    @Autowired
    UserStoryService userStoryService;

    public UserStoryController() {

        System.out.println("## ");

        System.out.println("## init > UserStoryController");

        System.out.println("## ");

    }

    @GetMapping("")
    public String hello() {

        return "User Story API";

    }

    @GetMapping("/health")
    public String health() {

        return "OK";

    }

    @GetMapping("/")
    public List<UserStory> findAll() {

        System.out.println("enter > findAll");

        List<UserStory> userStoryList = this.userStoryService.findAll();

        System.out.println("projectList / ");

        System.out.println(userStoryList);

        System.out.println("exit < findAll");

        return userStoryList;

    }

    @PutMapping("/")
    UserStory createUserStory(@RequestBody UserStory userStory) {

        System.out.println("enter > createUserStory");

        System.out.println("userStory / " + userStory);

        return this.userStoryService.save(userStory);

    }

    @GetMapping("/{id}")
    UserStory findById(@PathVariable Long id) {

        return this.userStoryService.findById(id);

    }

    @GetMapping("/byStatus/{status}")
    List<UserStory> findByStatus(@PathVariable String status) {

        return this.userStoryService.findByStatus(status);

    }

    @PostMapping("/update")
    public UserStory updateUserStory(@RequestBody UserStory project) {

        System.out.println("enter > updateUserStory");

        System.out.println("userStory / " + project);

        UserStory responseUserStory = this.userStoryService.save(project);

        System.out.println("responseUserStory / " + responseUserStory);

        System.out.println("exit < updateUserStory");

        return responseUserStory;

    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {

        this.userStoryService.delete(id);

    }

}
