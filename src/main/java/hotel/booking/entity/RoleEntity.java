package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class RoleEntity extends BaseEntity {


    private static final long serialVersionUID = -3135548806843941451L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "role_seq")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;

    public RoleEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
