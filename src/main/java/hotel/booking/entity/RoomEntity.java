package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table
public class RoomEntity extends BaseEntity {


    private static final long serialVersionUID = 8441579541097369715L;

    @Id
    @Column
    @SequenceGenerator(name = "room_seq", sequenceName = "room_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "room_seq")
    private Long id;
}
