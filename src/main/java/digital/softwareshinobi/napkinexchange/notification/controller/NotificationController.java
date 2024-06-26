package digital.softwareshinobi.napkinexchange.notification.controller;

import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    public NotificationController() {

        System.out.println("## ");
        System.out.println("## init > System Notification Controller");
        System.out.println("## ");

    }

    @GetMapping("")
    public String root() {

        return "NotificationController";

    }
//    @GetMapping("/")
//    public String rootSlash() {
//
//        return "NotificationController";
//
//    }

    @GetMapping("/health")
    public String health() {

        return "OK";

    }

    @GetMapping("/")
    public List<Notification> findAll() {
//
//        System.out.println("enter > findAll");
//
        List<Notification> notificationList = this.notificationService.findAll();
//
//        System.out.println("projectList / ");
//
//        System.out.println(userStoryList);
//
//        System.out.println("exit < findAll");
//
        return notificationList;
//
    }

    @GetMapping("/{username}")
    List<Notification> findByUsername(@PathVariable String username) {

        return this.notificationService.findByUsername(username);

    }

//    @PutMapping("/")
//    Notification createUserStory(@RequestBody Notification userStory) {
//
//        System.out.println("enter > createUserStory");
//
//        System.out.println("userStory / " + userStory);
//
//        return this.notificationService.save(userStory);
//
//    }
//    @GetMapping("/{id}")
//    Notification findById(@PathVariable Long id) {
//
//        return this.notificationService.findById(id);
//
//    }
//    @GetMapping("/byStatus/{status}")
//    List<Notification> findByStatus(@PathVariable String status) {
//
//        return this.notificationService.findByStatus(status);
//
//    }
//    @PostMapping("/update")
//    public Notification updateUserStory(@RequestBody Notification project) {
//
//        System.out.println("enter > updateUserStory");
//
//        System.out.println("userStory / " + project);
//
//        Notification responseUserStory = this.notificationService.save(project);
//
//        System.out.println("responseUserStory / " + responseUserStory);
//
//        System.out.println("exit < updateUserStory");
//
//        return responseUserStory;
//
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteById(@PathVariable Long id) {
//
//        this.notificationService.delete(id);
//
//    }
}
