package nure.com.InbinFilter.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nure.com.InbinFilter.models.user.Cleaner;
import nure.com.InbinFilter.models.user.User;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "complex")
public class HouseComplex extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User admin;


    @OneToMany(mappedBy = "complex", fetch = FetchType.LAZY)
    private List<Flat> flats;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cleaner_complex",
            joinColumns = {@JoinColumn(name = "cleaner_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "complex_id", referencedColumnName = "id")})
    private List<Cleaner> cleaners;


}
