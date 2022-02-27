package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "guest")
public class GuestEntity extends BaseEntity {
    private static final long serialVersionUID = 155049212519243044L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "guest_seq", sequenceName = "guest_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "guest_seq")
    private Long id;
    @Column(name = "adult")
    private Integer adult;
    @Column(name = "children")
    private Integer children;

    public GuestEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAdult() {
        return adult;
    }

    public void setAdult(Integer adult) {
        this.adult = adult;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }
}
