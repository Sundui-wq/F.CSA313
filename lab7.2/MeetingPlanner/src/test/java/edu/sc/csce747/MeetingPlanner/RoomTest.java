package edu.sc.csce747.MeetingPlanner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class RoomTest {

    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room("2A01");
    }

    // ========== ҮНДСЭН ФУНКЦҮҮД ==========

    @Test
    public void testRoomConstructor() {
        assertNotNull(room, "Room үүсэх ёстой");
        assertEquals("2A01", room.getID(),
                "Room ID зөв тохируулагдах ёстой");
    }

    @Test
    public void testRoomDefaultConstructor() {
        Room emptyRoom = new Room();
        assertNotNull(emptyRoom, "Default constructor ажиллах ёстой");
        assertEquals("", emptyRoom.getID(),
                "Default ID хоосон байх ёстой");
    }

    // ========== УУЛЗАЛТ НЭМЭХ ==========

    @Test
    public void testAddMeeting_success() {
        try {
            Person person = new Person("Alice");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting meeting = new Meeting(5, 20, 9, 11, attendees, room, "Board meeting");
            room.addMeeting(meeting);

            assertTrue(room.isBusy(5, 20, 9, 11),
                    "Өрөө уулзалтын цагт завгүй байх ёстой");
        } catch(TimeConflictException e) {
            fail("Зөв уулзалт нэмэхэд алдаа гарах ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testAddMeeting_conflictThrowsException() {
        try {
            Person person = new Person("Bob");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting meeting1 = new Meeting(6, 15, 10, 12, attendees, room, "First booking");
            room.addMeeting(meeting1);

            Meeting meeting2 = new Meeting(6, 15, 11, 13, attendees, room, "Conflicting booking");

            TimeConflictException exception = assertThrows(
                    TimeConflictException.class,
                    () -> room.addMeeting(meeting2),
                    "Давхцах уулзалт нэмэхэд exception шидэх ёстой"
            );

            assertTrue(exception.getMessage().contains("Conflict for room"),
                    "Exception message нь өрөөний ID агуулах ёстой");
            assertTrue(exception.getMessage().contains("2A01"),
                    "Exception message нь өрөөний ID агуулах ёстой");

        } catch(TimeConflictException e) {
            fail("Эхний уулзалт нэмэхэд алдаа гарах ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testAddMeeting_multipleNonConflictingMeetings() {
        try {
            Person person = new Person("Charlie");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting morning = new Meeting(7, 10, 9, 11, attendees, room, "Morning meeting");
            Meeting afternoon = new Meeting(7, 10, 14, 16, attendees, room, "Afternoon meeting");

            room.addMeeting(morning);
            room.addMeeting(afternoon);

            assertTrue(room.isBusy(7, 10, 9, 11),
                    "Өглөөний уулзалт бүртгэгдсэн байх ёстой");
            assertTrue(room.isBusy(7, 10, 14, 16),
                    "Өдрийн уулзалт бүртгэгдсэн байх ёстой");
            assertFalse(room.isBusy(7, 10, 12, 13),
                    "Уулзалтын хоорондох цаг завтай байх ёстой");

        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }

    // ========== ЗАВТАЙ ЭСЭХ ШАЛГАХ ==========

    @Test
    public void testIsBusy_notBusy() {
        try {
            assertFalse(room.isBusy(8, 25, 10, 12),
                    "Уулзалтгүй үед завтай байх ёстой");
        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testIsBusy_busy() {
        try {
            Person person = new Person("David");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting meeting = new Meeting(9, 5, 15, 17, attendees, room, "Occupied");
            room.addMeeting(meeting);

            assertTrue(room.isBusy(9, 5, 15, 17),
                    "Уулзалттай цагт завгүй байх ёстой");
        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testIsBusy_partialOverlap() {
        try {
            Person person = new Person("Eve");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting meeting = new Meeting(10, 12, 10, 13, attendees, room, "Long meeting");
            room.addMeeting(meeting);

            assertTrue(room.isBusy(10, 12, 11, 12),
                    "Хэсэгчлэн давхцах цагт busy байх ёстой");
            assertTrue(room.isBusy(10, 12, 12, 14),
                    "Дуусах цаг давхцахад busy байх ёстой");

        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testIsBusy_invalidDate() {
        assertThrows(TimeConflictException.class,
                () -> room.isBusy(13, 15, 10, 12),
                "Буруу огноотой шалгахад exception шидэх ёстой");
    }

    // ========== AGENDA ХЭВЛЭХ ==========


    @Test
    public void testPrintAgenda_day() {
        String agenda = room.printAgenda(6, 20);
        assertNotNull(agenda, "Agenda null байх ёсгүй");
        assertTrue(agenda.contains("Agenda for 6/20"),
                "Agenda өдрийн мэдээлэл агуулах ёстой");
    }

    @Test
    public void testPrintAgenda_withMeetings() {
        try {
            Person person = new Person("Frank");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting meeting = new Meeting(11, 8, 10, 12, attendees, room, "Print test meeting");
            room.addMeeting(meeting);

            String agenda = room.printAgenda(11, 8);
            assertTrue(agenda.contains("Print test meeting"),
                    "Agenda уулзалтын тайлбар агуулах ёстой");

        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }

    // ========== УУЛЗАЛТ АВАХ/УСТГАХ ==========

    @Test
    public void testRemoveMeeting() {
        try {
            Person person = new Person("Henry");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting meeting = new Meeting(1, 15, 14, 16, attendees, room, "Delete test");
            room.addMeeting(meeting);

            assertTrue(room.isBusy(1, 15, 14, 16),
                    "Уулзалт нэмэгдсэн байх ёстой");

            room.removeMeeting(1, 15, 0);

            assertFalse(room.isBusy(1, 15, 14, 16),
                    "Устгасны дараа busy байх ёсгүй");

        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleRooms_independentSchedules() {
        try {
            Room room2 = new Room("2A02");

            Person person = new Person("Ivy");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting meeting1 = new Meeting(3, 10, 10, 12, attendees, room, "Room 1 meeting");
            Meeting meeting2 = new Meeting(3, 10, 10, 12, attendees, room2, "Room 2 meeting");

            room.addMeeting(meeting1);
            room2.addMeeting(meeting2);

            assertTrue(room.isBusy(3, 10, 10, 12),
                    "Room 1 busy байх ёстой");
            assertTrue(room2.isBusy(3, 10, 10, 12),
                    "Room 2 busy байх ёстой");

        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }
    @Test
    public void testGetMeeting() {
        try {
            Person person = new Person("Test Person");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting meeting = new Meeting(5, 15, 9, 11, attendees, room, "Test meeting");
            room.addMeeting(meeting);

            Meeting retrieved = room.getMeeting(5, 15, 0);
            assertNotNull(retrieved, "Уулзалт олдох ёстой");
            assertEquals("Test meeting", retrieved.getDescription(),
                    "Уулзалтын тайлбар таарах ёстой");

        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testPrintAgenda_month() {
        try {
            Person person = new Person("Test Person");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            Meeting meeting = new Meeting(6, 15, 10, 12, attendees, room, "Monthly test");
            room.addMeeting(meeting);

            String agenda = room.printAgenda(6);
            assertNotNull(agenda, "Agenda null байх ёсгүй");
            assertTrue(agenda.contains("Agenda for 6"),
                    "Agenda сарын мэдээлэл агуулах ёстой");
            assertTrue(agenda.contains("Monthly test"),
                    "Уулзалтын тайлбар agenda-д байх ёстой");

        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }
}