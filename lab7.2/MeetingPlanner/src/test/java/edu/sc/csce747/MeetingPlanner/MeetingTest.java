package edu.sc.csce747.MeetingPlanner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class MeetingTest {

    // ========== CONSTRUCTOR ТЕСТҮҮД ==========

    @Test
    public void testMeeting_defaultConstructor() {
        Meeting meeting = new Meeting();
        assertNotNull(meeting, "Meeting үүсэх ёстой");
    }

    @Test
    public void testMeeting_monthDayConstructor() {
        Meeting meeting = new Meeting(6, 15);
        assertNotNull(meeting, "Meeting үүсэх ёстой");
        assertEquals(6, meeting.getMonth(), "Сар зөв тохируулагдах ёстой");
        assertEquals(15, meeting.getDay(), "Өдөр зөв тохируулагдах ёстой");
        assertEquals(0, meeting.getStartTime(), "Эхлэх цаг 0 байх ёстой");
        assertEquals(23, meeting.getEndTime(), "Дуусах цаг 23 байх ёстой");
    }

    @Test
    public void testMeeting_monthDayDescriptionConstructor() {
        Meeting meeting = new Meeting(7, 20, "Holiday");
        assertNotNull(meeting, "Meeting үүсэх ёстой");
        assertEquals(7, meeting.getMonth(), "Сар зөв тохируулагдах ёстой");
        assertEquals(20, meeting.getDay(), "Өдөр зөв тохируулагдах ёстой");
        assertEquals("Holiday", meeting.getDescription(),
                "Тайлбар зөв тохируулагдах ёстой");
        assertEquals(0, meeting.getStartTime(), "Эхлэх цаг 0 байх ёстой");
        assertEquals(23, meeting.getEndTime(), "Дуусах цаг 23 байх ёстой");
    }

    @Test
    public void testMeeting_basicConstructor() {
        Meeting meeting = new Meeting(5, 15, 10, 12);
        assertNotNull(meeting, "Meeting үүсэх ёстой");
        assertEquals(5, meeting.getMonth(), "Сар зөв тохируулагдах ёстой");
        assertEquals(15, meeting.getDay(), "Өдөр зөв тохируулагдах ёстой");
        assertEquals(10, meeting.getStartTime(), "Эхлэх цаг зөв тохируулагдах ёстой");
        assertEquals(12, meeting.getEndTime(), "Дуусах цаг зөв тохируулагдах ёстой");
    }

    @Test
    public void testMeeting_fullConstructor() {
        Person person1 = new Person("Alice");
        Person person2 = new Person("Bob");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person1);
        attendees.add(person2);

        Room room = new Room("101");

        Meeting meeting = new Meeting(8, 10, 14, 16, attendees, room, "Team meeting");

        assertNotNull(meeting, "Meeting үүсэх ёстой");
        assertEquals(8, meeting.getMonth(), "Сар зөв тохируулагдах ёстой");
        assertEquals(10, meeting.getDay(), "Өдөр зөв тохируулагдах ёстой");
        assertEquals(14, meeting.getStartTime(), "Эхлэх цаг зөв тохируулагдах ёстой");
        assertEquals(16, meeting.getEndTime(), "Дуусах цаг зөв тохируулагдах ёстой");
        assertEquals("Team meeting", meeting.getDescription(),
                "Тайлбар зөв тохируулагдах ёстой");
        assertEquals(room, meeting.getRoom(), "Өрөө зөв тохируулагдах ёстой");
        assertEquals(attendees, meeting.getAttendees(),
                "Attendees зөв тохируулагдах ёстой");
        assertEquals(2, meeting.getAttendees().size(),
                "2 хүн оролцох ёстой");
    }

    // ========== GETTER/SETTER ТЕСТҮҮД ==========

    @Test
    public void testSetMonth() {
        Meeting meeting = new Meeting();
        meeting.setMonth(9);
        assertEquals(9, meeting.getMonth(), "Сар зөв тохируулагдах ёстой");
    }

    @Test
    public void testSetDay() {
        Meeting meeting = new Meeting();
        meeting.setDay(25);
        assertEquals(25, meeting.getDay(), "Өдөр зөв тохируулагдах ёстой");
    }

    @Test
    public void testSetStartTime() {
        Meeting meeting = new Meeting();
        meeting.setStartTime(9);
        assertEquals(9, meeting.getStartTime(),
                "Эхлэх цаг зөв тохируулагдах ёстой");
    }

    @Test
    public void testSetEndTime() {
        Meeting meeting = new Meeting();
        meeting.setEndTime(17);
        assertEquals(17, meeting.getEndTime(),
                "Дуусах цаг зөв тохируулагдах ёстой");
    }

    @Test
    public void testSetDescription() {
        Meeting meeting = new Meeting();
        meeting.setDescription("Important meeting");
        assertEquals("Important meeting", meeting.getDescription(),
                "Тайлбар зөв тохируулагдах ёстой");
    }

    @Test
    public void testSetRoom() {
        Meeting meeting = new Meeting();
        Room room = new Room("202");
        meeting.setRoom(room);
        assertEquals(room, meeting.getRoom(),
                "Өрөө зөв тохируулагдах ёстой");
        assertEquals("202", meeting.getRoom().getID(),
                "Өрөөний ID зөв байх ёстой");
    }

    // ========== ATTENDEE ФУНКЦҮҮД ==========

    @Test
    public void testAddAttendee() {
        Person person1 = new Person("Charlie");
        Person person2 = new Person("Diana");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person1);

        Room room = new Room("303");
        Meeting meeting = new Meeting(10, 5, 11, 13, attendees, room, "Test");

        assertEquals(1, meeting.getAttendees().size(),
                "Эхэндээ 1 хүн байх ёстой");

        meeting.addAttendee(person2);

        assertEquals(2, meeting.getAttendees().size(),
                "Нэмсний дараа 2 хүн байх ёстой");
        assertTrue(meeting.getAttendees().contains(person2),
                "Diana нэмэгдсэн байх ёстой");
    }

    @Test
    public void testRemoveAttendee() {
        Person person1 = new Person("Eve");
        Person person2 = new Person("Frank");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person1);
        attendees.add(person2);

        Room room = new Room("404");
        Meeting meeting = new Meeting(11, 15, 9, 10, attendees, room, "Remove test");

        assertEquals(2, meeting.getAttendees().size(),
                "Эхэндээ 2 хүн байх ёстой");

        meeting.removeAttendee(person1);

        assertEquals(1, meeting.getAttendees().size(),
                "Устгасны дараа 1 хүн байх ёстой");
        assertFalse(meeting.getAttendees().contains(person1),
                "Eve устсан байх ёстой");
        assertTrue(meeting.getAttendees().contains(person2),
                "Frank байх ёстой");
    }

    @Test
    public void testAddMultipleAttendees() {
        Person person1 = new Person("Grace");
        Person person2 = new Person("Henry");
        Person person3 = new Person("Ivy");

        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person1);

        Room room = new Room("505");
        Meeting meeting = new Meeting(12, 20, 10, 11, attendees, room, "Multiple");

        meeting.addAttendee(person2);
        meeting.addAttendee(person3);

        assertEquals(3, meeting.getAttendees().size(),
                "3 хүн байх ёстой");
        assertTrue(meeting.getAttendees().contains(person1));
        assertTrue(meeting.getAttendees().contains(person2));
        assertTrue(meeting.getAttendees().contains(person3));
    }

    // ========== TOSTRING ТЕСТ ==========

    @Test
    public void testToString() {
        Person person1 = new Person("Jack");
        Person person2 = new Person("Kate");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person1);
        attendees.add(person2);

        Room room = new Room("606");
        Meeting meeting = new Meeting(6, 25, 14, 16, attendees, room, "String test");

        String result = meeting.toString();

        assertNotNull(result, "toString() null байх ёсгүй");
        assertTrue(result.contains("6/25"), "Огноо агуулах ёстой");
        assertTrue(result.contains("14"), "Эхлэх цаг агуулах ёстой");
        assertTrue(result.contains("16"), "Дуусах цаг агуулах ёстой");
        assertTrue(result.contains("606"), "Өрөөний ID агуулах ёстой");
        assertTrue(result.contains("String test"), "Тайлбар агуулах ёстой");
        assertTrue(result.contains("Jack"), "Оролцогчийн нэр агуулах ёстой");
        assertTrue(result.contains("Kate"), "Оролцогчийн нэр агуулах ёстой");
    }

    @Test
    public void testToString_singleAttendee() {
        Person person = new Person("Leo");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person);

        Room room = new Room("707");
        Meeting meeting = new Meeting(7, 30, 11, 12, attendees, room, "Solo");

        String result = meeting.toString();

        assertTrue(result.contains("Leo"), "Оролцогчийн нэр агуулах ёстой");
        assertFalse(result.endsWith(","), "Хоёрдогч таслал байх ёсгүй");
    }

    // ========== EDGE CASE ТЕСТҮҮД ==========

    @Test
    public void testMeeting_boundaryTimes() {
        // 0 цагаас эхлэх
        Meeting morning = new Meeting(1, 1, 0, 1);
        assertEquals(0, morning.getStartTime(), "0 цаг зөв");

        // 23 цагт дуусах
        Meeting night = new Meeting(1, 1, 22, 23);
        assertEquals(23, night.getEndTime(), "23 цаг зөв");

        // Бүтэн өдөр
        Meeting allDay = new Meeting(1, 1, 0, 23);
        assertEquals(0, allDay.getStartTime(), "Бүтэн өдөр эхлэл зөв");
        assertEquals(23, allDay.getEndTime(), "Бүтэн өдөр төгсгөл зөв");
    }

    @Test
    public void testMeeting_shortMeeting() {
        // 1 цагийн уулзалт
        Meeting shortMeeting = new Meeting(3, 15, 10, 11);
        assertEquals(10, shortMeeting.getStartTime());
        assertEquals(11, shortMeeting.getEndTime());
    }

    @Test
    public void testMeeting_longMeeting() {
        // Урт уулзалт
        Meeting longMeeting = new Meeting(4, 20, 8, 18);
        assertEquals(8, longMeeting.getStartTime());
        assertEquals(18, longMeeting.getEndTime());
    }

    @Test
    public void testMeeting_allMonths() {
        // Бүх сарыг шалгах
        for(int month = 1; month <= 12; month++) {
            Meeting meeting = new Meeting(month, 15, 10, 12);
            assertEquals(month, meeting.getMonth(),
                    month + "-р сар зөв тохируулагдах ёстой");
        }
    }

    @Test
    public void testMeeting_emptyDescription() {
        Person person = new Person("Mike");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person);
        Room room = new Room("808");

        Meeting meeting = new Meeting(5, 10, 9, 10, attendees, room, "");
        assertEquals("", meeting.getDescription(),
                "Хоосон тайлбар зөвшөөрөгдөх ёстой");
    }

    @Test
    public void testMeeting_longDescription() {
        Person person = new Person("Nancy");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person);
        Room room = new Room("909");

        String longDesc = "This is a very long description for a meeting that contains " +
                "lots of information about what will be discussed and who should " +
                "attend and what preparation is needed for this important meeting";

        Meeting meeting = new Meeting(6, 12, 14, 15, attendees, room, longDesc);
        assertEquals(longDesc, meeting.getDescription(),
                "Урт тайлбар хадгалагдах ёстой");
    }

    @Test
    public void testMeeting_specialCharactersInDescription() {
        Person person = new Person("Oliver");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person);
        Room room = new Room("1010");

        String specialDesc = "Meeting@#$%^&*()_+-=[]{}|;':\",./<>?";

        Meeting meeting = new Meeting(7, 18, 10, 11, attendees, room, specialDesc);
        assertEquals(specialDesc, meeting.getDescription(),
                "Тусгай тэмдэгтүүд хадгалагдах ёстой");
    }

    // ========== NULL HANDLING ТЕСТҮҮД ==========

    @Test
    public void testMeeting_nullDescription() {
        Meeting meeting = new Meeting(8, 5, 10, 12);
        meeting.setDescription(null);
        assertNull(meeting.getDescription(),
                "Null тайлбар зөвшөөрөгдөх ёстой");
    }

    @Test
    public void testMeeting_nullRoom() {
        Meeting meeting = new Meeting(9, 10, 11, 13);
        meeting.setRoom(null);
        assertNull(meeting.getRoom(),
                "Null өрөө зөвшөөрөгдөх ёстой");
    }

    // ========== РАВЬДАЛЫН ТЕСТҮҮД ==========

    @Test
    public void testMeeting_newYearDay() {
        Meeting meeting = new Meeting(1, 1, "New Year");
        assertEquals(1, meeting.getMonth());
        assertEquals(1, meeting.getDay());
        assertEquals("New Year", meeting.getDescription());
    }

    @Test
    public void testMeeting_lastDayOfYear() {
        Meeting meeting = new Meeting(12, 31, "Year End");
        assertEquals(12, meeting.getMonth());
        assertEquals(31, meeting.getDay());
        assertEquals("Year End", meeting.getDescription());
    }

    @Test
    public void testMeeting_leapYearDate() {
        // 2-р сарын 29 (Calendar классад байхгүй гэж тохируулсан)
        Meeting meeting = new Meeting(2, 29, "Leap day");
        assertEquals(2, meeting.getMonth());
        assertEquals(29, meeting.getDay());
    }
}