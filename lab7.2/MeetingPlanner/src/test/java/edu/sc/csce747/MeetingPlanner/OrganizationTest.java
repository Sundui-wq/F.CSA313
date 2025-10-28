package edu.sc.csce747.MeetingPlanner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class OrganizationTest {

    private Organization org;

    @BeforeEach
    public void setUp() {
        org = new Organization();
    }

    // ========== ҮНДСЭН ФУНКЦҮҮД ==========

    @Test
    public void testOrganizationConstructor() {
        assertNotNull(org, "Organization үүсэх ёстой");
        assertNotNull(org.getEmployees(), "Employees list null байх ёсгүй");
        assertNotNull(org.getRooms(), "Rooms list null байх ёсгүй");
    }

    @Test
    public void testGetEmployees_notEmpty() {
        ArrayList<Person> employees = org.getEmployees();
        assertFalse(employees.isEmpty(),
                "Employees list хоосон байх ёсгүй");
        assertEquals(5, employees.size(),
                "5 ажилтан байх ёстой");
    }

    @Test
    public void testGetRooms_notEmpty() {
        ArrayList<Room> rooms = org.getRooms();
        assertFalse(rooms.isEmpty(),
                "Rooms list хоосон байх ёсгүй");
        assertEquals(5, rooms.size(),
                "5 өрөө байх ёстой");
    }

    // ========== ӨРӨӨ ХАЙХ ==========

    @Test
    public void testGetRoom_validID() {
        try {
            Room room = org.getRoom("2A01");
            assertNotNull(room, "Өрөө олдох ёстой");
            assertEquals("2A01", room.getID(),
                    "Өрөөний ID таарах ёстой");
        } catch(Exception e) {
            fail("Байгаа өрөө хайхад алдаа гарах ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testGetRoom_allRooms() {
        try {
            Room room1 = org.getRoom("2A01");
            Room room2 = org.getRoom("2A02");
            Room room3 = org.getRoom("2A03");
            Room room4 = org.getRoom("2A04");
            Room room5 = org.getRoom("2A05");

            assertNotNull(room1, "2A01 олдох ёстой");
            assertNotNull(room2, "2A02 олдох ёстой");
            assertNotNull(room3, "2A03 олдох ёстой");
            assertNotNull(room4, "2A04 олдох ёстой");
            assertNotNull(room5, "2A05 олдох ёстой");

        } catch(Exception e) {
            fail("Бүх өрөө олдох ёстой: " + e.getMessage());
        }
    }

    @Test
    public void testGetRoom_invalidID() {
        Exception exception = assertThrows(
                Exception.class,
                () -> org.getRoom("NonExistentRoom"),
                "Байхгүй өрөө хайхад exception шидэх ёстой"
        );

        assertTrue(exception.getMessage().contains("does not exist"),
                "Exception message нь 'does not exist' агуулах ёстой");
    }

    @Test
    public void testGetRoom_emptyString() {
        Exception exception = assertThrows(
                Exception.class,
                () -> org.getRoom(""),
                "Хоосон string-ээр хайхад exception шидэх ёстой"
        );

        assertTrue(exception.getMessage().contains("does not exist"),
                "Exception message нь 'does not exist' агуулах ёстой");
    }

    @Test
    public void testGetRoom_caseSensitive() {
        Exception exception = assertThrows(
                Exception.class,
                () -> org.getRoom("2a01"),
                "Case sensitive байх ёстой (lowercase 'a' буруу)"
        );
    }

    @Test
    public void testGetRoom_withSpaces() {
        Exception exception = assertThrows(
                Exception.class,
                () -> org.getRoom("2A01 "),
                "Зайтай ID олдох ёсгүй"
        );
    }

    // ========== АЖИЛТАН ХАЙХ ==========

    @Test
    public void testGetEmployee_validName() {
        try {
            Person employee = org.getEmployee("Greg Gay");
            assertNotNull(employee, "Ажилтан олдох ёстой");
            assertEquals("Greg Gay", employee.getName(),
                    "Ажилтны нэр таарах ёстой");
        } catch(Exception e) {
            fail("Байгаа ажилтан хайхад алдаа гарах ёсгүй: " + e.getMessage());
        }
    }

    @Test
    public void testGetEmployee_allEmployees() {
        try {
            Person emp1 = org.getEmployee("Greg Gay");
            Person emp2 = org.getEmployee("Manton Matthews");
            Person emp3 = org.getEmployee("John Rose");
            Person emp4 = org.getEmployee("Ryan Austin");
            Person emp5 = org.getEmployee("Csilla Farkas");

            assertNotNull(emp1, "Greg Gay олдох ёстой");
            assertNotNull(emp2, "Manton Matthews олдох ёстой");
            assertNotNull(emp3, "John Rose олдох ёстой");
            assertNotNull(emp4, "Ryan Austin олдох ёстой");
            assertNotNull(emp5, "Csilla Farkas олдох ёстой");

            assertEquals("Greg Gay", emp1.getName());
            assertEquals("Manton Matthews", emp2.getName());
            assertEquals("John Rose", emp3.getName());
            assertEquals("Ryan Austin", emp4.getName());
            assertEquals("Csilla Farkas", emp5.getName());

        } catch(Exception e) {
            fail("Бүх ажилтан олдох ёстой: " + e.getMessage());
        }
    }

    @Test
    public void testGetEmployee_invalidName() {
        Exception exception = assertThrows(
                Exception.class,
                () -> org.getEmployee("Non Existent Person"),
                "Байхгүй ажилтан хайхад exception шидэх ёстой"
        );

        assertTrue(exception.getMessage().contains("does not exist"),
                "Exception message нь 'does not exist' агуулах ёстой");
    }

    @Test
    public void testGetEmployee_emptyString() {
        Exception exception = assertThrows(
                Exception.class,
                () -> org.getEmployee(""),
                "Хоосон string-ээр хайхад exception шидэх ёстой"
        );

        assertTrue(exception.getMessage().contains("does not exist"),
                "Exception message нь 'does not exist' агуулах ёстой");
    }

    @Test
    public void testGetEmployee_partialName() {
        Exception exception = assertThrows(
                Exception.class,
                () -> org.getEmployee("Greg"),
                "Хэсэгчилсэн нэрээр олдох ёсгүй"
        );
    }

    @Test
    public void testGetEmployee_caseSensitive() {
        Exception exception = assertThrows(
                Exception.class,
                () -> org.getEmployee("greg gay"),
                "Case sensitive байх ёстой (lowercase буруу)"
        );
    }

    @Test
    public void testGetEmployee_extraSpaces() {
        Exception exception = assertThrows(
                Exception.class,
                () -> org.getEmployee("Greg  Gay"),
                "Илүү зайтай нэр буруу"
        );
    }

    // ========== INTEGRATION ТЕСТҮҮД ==========

    @Test
    public void testOrganization_scheduleMeetingScenario() {
        try {
            // Өрөө болон ажилтан авах
            Room room = org.getRoom("2A03");
            Person person1 = org.getEmployee("Greg Gay");
            Person person2 = org.getEmployee("John Rose");

            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person1);
            attendees.add(person2);

            // Уулзалт үүсгэх
            Meeting meeting = new Meeting(5, 20, 10, 12, attendees, room, "Team sync");

            // Өрөө болон хүмүүст уулзалт нэмэх
            room.addMeeting(meeting);
            person1.addMeeting(meeting);
            person2.addMeeting(meeting);

            // Шалгах
            assertTrue(room.isBusy(5, 20, 10, 12),
                    "Өрөө busy байх ёстой");
            assertTrue(person1.isBusy(5, 20, 10, 12),
                    "Person1 busy байх ёстой");
            assertTrue(person2.isBusy(5, 20, 10, 12),
                    "Person2 busy байх ёстой");

        } catch(Exception e) {
            fail("Уулзалт товлох сценари амжилттай байх ёстой: " + e.getMessage());
        }
    }

    @Test
    public void testOrganization_multipleRoomsIndependent() {
        try {
            Room room1 = org.getRoom("2A01");
            Room room2 = org.getRoom("2A02");

            Person person = org.getEmployee("Manton Matthews");
            ArrayList<Person> attendees = new ArrayList<>();
            attendees.add(person);

            // Өрөө 1-д уулзалт
            Meeting meeting1 = new Meeting(6, 15, 9, 11, attendees, room1, "Room 1 meeting");
            room1.addMeeting(meeting1);

            // Өрөө 2 ижил цагт сул байх ёстой
            assertFalse(room2.isBusy(6, 15, 9, 11),
                    "Room 2 сул байх ёстой");

            // Өрөө 2-т ижил цагт уулзалт нэмж болно
            Meeting meeting2 = new Meeting(6, 15, 9, 11, attendees, room2, "Room 2 meeting");
            room2.addMeeting(meeting2);

            assertTrue(room1.isBusy(6, 15, 9, 11),
                    "Room 1 busy байх ёстой");
            assertTrue(room2.isBusy(6, 15, 9, 11),
                    "Room 2 busy байх ёстой");

        } catch(Exception e) {
            fail("Өрөөнүүд бие даасан байх ёстой: " + e.getMessage());
        }
    }
}