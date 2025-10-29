package edu.sc.csce747.MeetingPlanner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class PersonTest {

    private Person person;

    @BeforeEach
    public void setUp() {
        person = new Person("John Doe");
    }

    // ========== ҮНДСЭН ФУНКЦҮҮД ==========

    @Test
    public void testPersonConstructor() {
        assertNotNull(person, "Person үүсэх ёстой");
        assertEquals("John Doe", person.getName(),
                "Нэр зөв тохируулагдах ёстой");
    }

    @Test
    public void testPersonDefaultConstructor() {
        Person emptyPerson = new Person();
        assertNotNull(emptyPerson, "Default constructor ажиллах ёстой");
        assertEquals("", emptyPerson.getName(),
                "Default нэр хоосон байх ёстой");
    }

    // ========== УУЛЗАЛТ НЭМЭХ ==========

    @Test
    public void testAddMeeting_success() {
        try {
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);
            Room room = new Room("101");

            Meeting meeting = new Meeting(5, 15, 10, 12, attendees, room, "Team meeting");
            person.addMeeting(meeting);

            assertTrue(person.isBusy(5, 15, 10, 12),
                    "Хүн уулзалтын цагт завгүй байх ёстой");
        } catch(TimeConflictException e) {
            fail("Зөв уулзалт нэмэхэд алдаа гарах ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testAddMeeting_conflictThrowsException() {
        try {
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);
            Room room = new Room("101");

            Meeting meeting1 = new Meeting(6, 10, 9, 11, attendees, room, "First meeting");
            person.addMeeting(meeting1);

            Meeting meeting2 = new Meeting(6, 10, 10, 12, attendees, room, "Conflicting meeting");

            TimeConflictException exception = assertThrows(
                    TimeConflictException.class,
                    () -> person.addMeeting(meeting2),
                    "Давхцах уулзалт нэмэхэд exception шидэх ёстой"
            );

            assertTrue(exception.getMessage().contains("Conflict for attendee"),
                    "Exception message нь хүний нэр агуулах ёстой");
            assertTrue(exception.getMessage().contains("John Doe"),
                    "Exception message нь хүний нэр агуулах ёстой");

        } catch(TimeConflictException e) {
            fail("Эхний уулзалт нэмэхэд алдаа гарах ёсгүй: " + e.getMessage());
        }
    }

    // ========== ЗАВТАЙ ЭСЭХ ШАЛГАХ ==========

    @Test
    public void testIsBusy_notBusy() {
        try {
            assertFalse(person.isBusy(7, 20, 14, 16),
                    "Уулзалтгүй үед завтай байх ёстой");
        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testIsBusy_busy() {
        try {
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);
            Room room = new Room("102");

            Meeting meeting = new Meeting(8, 5, 13, 15, attendees, room, "Busy time");
            person.addMeeting(meeting);

            assertTrue(person.isBusy(8, 5, 13, 15),
                    "Уулзалттай цагт завгүй байх ёстой");
        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }


    // ========== AGENDA ХЭВЛЭХ ==========

    @Test
    public void testPrintAgenda_month() {
        String agenda = person.printAgenda(5);
        assertNotNull(agenda, "Agenda null байх ёсгүй");
        assertTrue(agenda.contains("Agenda for 5"),
                "Agenda сарын мэдээлэл агуулах ёстой");
    }

    @Test
    public void testPrintAgenda_day() {
        String agenda = person.printAgenda(5, 15);
        assertNotNull(agenda, "Agenda null байх ёсгүй");
        assertTrue(agenda.contains("Agenda for 5/15"),
                "Agenda өдрийн мэдээлэл агуулах ёстой");
    }

    // ========== УУЛЗАЛТ АВАХ/УСТГАХ ==========

    @Test
    public void testGetMeeting() {
        try {
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);
            Room room = new Room("103");

            Meeting meeting = new Meeting(9, 12, 10, 11, attendees, room, "Get test");
            person.addMeeting(meeting);

            Meeting retrieved = person.getMeeting(9, 12, 0);
            assertNotNull(retrieved, "Уулзалт олдох ёстой");
            assertEquals("Get test", retrieved.getDescription(),
                    "Уулзалтын тайлбар таарах ёстой");

        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveMeeting() {
        try {
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);
            Room room = new Room("104");

            Meeting meeting = new Meeting(10, 8, 15, 17, attendees, room, "Remove test");
            person.addMeeting(meeting);

            assertTrue(person.isBusy(10, 8, 15, 17),
                    "Уулзалт нэмэгдсэн байх ёстой");

            person.removeMeeting(10, 8, 0);

            assertFalse(person.isBusy(10, 8, 15, 17),
                    "Устгасны дараа busy байх ёсгүй");

        } catch(TimeConflictException e) {
            fail("Exception шидэх ёсгүй: " + e.getMessage());
        }
    }
}