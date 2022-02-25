package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "rule")
public class RuleEntity extends BaseEntity {

    private static final long serialVersionUID = 8441579541097369715L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "rule_seq", sequenceName = "rule_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "rule_seq")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "room_id")
    private Long roomId;
    @ManyToOne
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomEntity roomEntity;

    public RuleEntity() {
    }

    public RuleEntity(Long roomId, String name) {
        this.roomId = roomId;
        this.name = name;
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
