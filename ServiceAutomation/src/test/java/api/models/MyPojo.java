package api.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Juan Krzemien on 7/14/2016.
 */
@Entity
@Table(name = "POJOS")
public class MyPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE")
    private Date date;

    public MyPojo(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }
}
