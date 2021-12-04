package nure.com.InbinFilter.models.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nure.com.InbinFilter.models.HouseComplex;
import nure.com.InbinFilter.models.user.User;

import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cleaners")
public class Cleaner {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @ManyToMany(mappedBy = "cleaners", fetch = FetchType.LAZY)
    private List<HouseComplex> complexes;
}
