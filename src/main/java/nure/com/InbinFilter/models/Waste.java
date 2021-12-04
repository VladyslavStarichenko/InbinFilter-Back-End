package nure.com.InbinFilter.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nure.com.InbinFilter.models.Bin;
import nure.com.InbinFilter.models.Litter;
import nure.com.InbinFilter.models.user.Resident;
import nure.com.InbinFilter.models.user.User;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "waste")
public class Waste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @ManyToOne
    @JoinColumn(name = "bin_id", nullable = false)
    private Bin bin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "litter_id", referencedColumnName = "id")
    private Litter litter;

    private Integer amount;


}
