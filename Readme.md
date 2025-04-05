## Entities and Relations


### Entities 
- Hotel, `Relations`:
  - Has multiple `Floors`
  - Has multiple `Rooms`
  - Processes `Reservations`
- Floor, `Relations`:
  - Belongs to a `Hotel`
  - Has multiple `Rooms`

- Room, `Relations`:
  - Belongs to a `Floor`
  - Can be assigned to `Reservation`
  - Can be reviewed by a `Guest`

- Reservation, `Relation`:
  - Belongs to a `Guest`
  - Assigned to a `Room`
  - Handled by an `Operator`
  
- Guest, `Relations`:
  - Makes a `Reservation`
  - Leaves a `Review`
  - Receives a `Lottery Ticket`

- Operator, `Relations`:
  - Manages `Reservations`
  - Assigns `Rooms`
  - Handles `Check-outs`

- Review, `Relations`:
  - Given by a `Guest`
  - Associated with a `Reservation`

- Lottery Ticket, `Relations`:
  - Issued to a `Guest` upon a Check-out, Based on `Review Rating` and `Room Type`

- Discount Code, `Relations`:
  - Generated from a `Lottery Ticket`
  - Used by a `Guest` in future reservation 

### Relations 

- hotel `has multiple` floor
- floor `has multiple` room
- reservation `belongs to` guest
- guest `influences` LotteryTicket
- reservation `is handled by` Operator
- guest `can give a` review
- LotteryTicket `generates` DiscountCode
- Guest `can receive a` Discount,
- Room `can be assigned to` Reservation


