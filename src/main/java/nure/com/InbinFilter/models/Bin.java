package nure.com.InbinFilter.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bin")
public class Bin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double fill;

    private Double capacity;

    @Enumerated(EnumType.STRING)
    private LitterType litterType;

    @Column(name = "is_full")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean full;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="flat_id", nullable = false)
    private Flat flat;

    @OneToMany(mappedBy="bin")
    private List<Waste> wastes;

}
