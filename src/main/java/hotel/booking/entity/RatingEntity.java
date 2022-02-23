package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table
public class RatingEntity extends BaseEntity {


    private static final long serialVersionUID = 155049212519243044L;

    @Id
    @Column
    @SequenceGenerator(name = "rating_seq", sequenceName = "rating_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "rating_seq")
    private Long id;
}
