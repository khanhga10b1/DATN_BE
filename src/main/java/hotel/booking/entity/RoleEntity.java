package hotel.booking.entity;

import javax.persistence.*;

@Table
@Entity
public class RoleEntity extends BaseEntity {


    private static final long serialVersionUID = -3135548806843941451L;

    @Id
    @Column
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "role_seq")
    private Long id;
}
