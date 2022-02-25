package hotel.booking.controller;

import hotel.booking.domain.ResponseByName;
import hotel.booking.domain.RoomDomain;
import hotel.booking.domain.request.RoomRequest;
import hotel.booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getListRooms(@RequestParam(value = "hotelId", required = false) Long hotelId,
                                                            @RequestParam(value = "offset", required = false) Integer offset,
                                                            @RequestParam(value = "limit", required = false) Integer limit,
                                                            @RequestParam(value = "typeSort", required = false) String typeSort,
                                                            @RequestParam(value = "columnSort", required = false) String columnSort,
                                                            @RequestParam(value = "name", required = false) String search) {
        return ResponseEntity.ok(roomService.getListRooms(hotelId, offset, limit, typeSort, columnSort, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDomain> getRoomById(@PathVariable("id") Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @PostMapping
    public ResponseEntity<RoomDomain> createRoom(@RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.editRoom(null, roomRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDomain> updateRoom(@RequestBody RoomRequest roomRequest, @PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.editRoom(id, roomRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseByName<String, Object>> deleteRoom(@PathVariable("id") Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(ResponseByName.Builder("id", id).build());
    }
}
