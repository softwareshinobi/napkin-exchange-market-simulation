package digital.softwareshinobi.napkinexchange.notification.controller;

import digital.softwareshinobi.napkinexchange.leaderboard.service.LeaderboardService;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import java.util.List;
import org.slf4j.LoggerFactory;
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

        private final org.slf4j.Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    NotificationService notificationService;

    public NotificationController() {

        logger.debug("## ");
        logger.debug("## init > System Notification Controller");
        logger.debug("## ");

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
//        logger.debug("enter > findAll");
//
        List<Notification> notificationList = this.notificationService.findAll();
//
//        logger.debug("projectList / ");
//
//        logger.debug(userStoryList);
//
//        logger.debug("exit < findAll");
//
        return notificationList;
//
    }

    @GetMapping("/{username}")
    List<Notification> findByUsername(@PathVariable String username) {

        return this.notificationService.findByTrader(username);

    }

//    @PutMapping("/")
//    Notification createUserStory(@RequestBody Notification userStory) {
//
//        logger.debug("enter > createUserStory");
//
//        logger.debug("userStory / " + userStory);
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
//        logger.debug("enter > updateUserStory");
//
//        logger.debug("userStory / " + project);
//
//        Notification responseUserStory = this.notificationService.save(project);
//
//        logger.debug("responseUserStory / " + responseUserStory);
//
//        logger.debug("exit < updateUserStory");
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
