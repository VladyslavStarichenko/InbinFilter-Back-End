package nure.com.InbinFilter.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nure.com.InbinFilter.models.user.Status;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    @CreationTimestamp
    @Column(name = "created")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated")
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;


}
