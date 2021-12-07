package nure.com.InbinFilter.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nure.com.InbinFilter.models.Flat;
import nure.com.InbinFilter.models.Notification;
import nure.com.InbinFilter.models.Waste;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "residents")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "bill")
    private Double bill;

    @ManyToOne
    @JoinColumn(name="flat_id", nullable=false)
    private Flat flat;

    @OneToMany(mappedBy="resident")
    private List<Waste> wastes;

    @OneToMany(mappedBy="resident")
    private List<Notification> notifications;


}
