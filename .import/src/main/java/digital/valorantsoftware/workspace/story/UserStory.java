package digital.valorantsoftware.workspace.story;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "userstory")
public class UserStory {

    private static final Logger log = LoggerFactory.getLogger(UserStory.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //   @NonNull
    @Column(name = "project")
    private String project;

    //   @NonNull
    @Column(name = "name")
    private String name;

    //  @NonNull
    @Column(name = "description")
    private String description;

    //  @NonNull
    @Column(name = "intention")
    private String intention;

//    //  @NonNull
//    @Column(name = "category")
//    private String category;
    //   @NonNull
    @Column(name = "status")
    private String status;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserStory{");
        sb.append("id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", intention=").append(intention);
        sb.append(", project=").append(project);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIntention() {
        return intention;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
