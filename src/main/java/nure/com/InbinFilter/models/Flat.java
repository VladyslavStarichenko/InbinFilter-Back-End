package nure.com.InbinFilter.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nure.com.InbinFilter.models.user.Resident;

import java.util.List;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flat")
public class Flat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="complex_id", nullable = false)
    private HouseComplex complex;

    @OneToMany(mappedBy="flat")
    private List<Resident> residents;

    @OneToMany(mappedBy = "flat")
    private List<Bin> bins;

    private String address;

}
