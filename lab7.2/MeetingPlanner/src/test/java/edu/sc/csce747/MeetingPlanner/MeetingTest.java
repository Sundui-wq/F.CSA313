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
}