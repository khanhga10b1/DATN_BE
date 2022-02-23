package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "room_image")
public class RoomImageEntity extends BaseEntity {
    private static final long serialVersionUID = -5942612259595519841L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "room_image_seq", sequenceName = "room_image_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "room_image_seq")
    private Long id;
    @Column(name = "image_link")
    private String imageLink;
    @Column(name = "room_id")
    private Long roomId;
    @ManyToOne
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomEntity roomEntity;

    public RoomImageEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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
