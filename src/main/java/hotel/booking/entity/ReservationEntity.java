package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table
public class ReservationEntity extends BaseEntity {


    private static final long serialVersionUID = 155049212519243044L;

    @Id
    @Column
    @SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "reservation_seq")
    private Long id;
}
