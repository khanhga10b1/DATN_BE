package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "room_amenities")
public class RoomAmenitiesEntity extends BaseEntity {

    private static final long serialVersionUID = -771926412599360789L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "room_amenities_seq", sequenceName = "room_amenities_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "room_amenities_seq")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "room_id")
    private Long roomId;
    @ManyToOne
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomEntity roomEntity;

    public RoomAmenitiesEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }
}
