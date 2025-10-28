package edu.sc.csce747.MeetingPlanner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class CalendarTest {

	private Calendar calendar;

	@BeforeEach
	public void setUp() {
		calendar = new Calendar();
	}

	// ========== АМЖИЛТТАЙ ТОХИОЛДЛУУД ==========

	@Test
	public void testAddMeeting_validMeeting() {
		try {
			Meeting meeting = new Meeting(3, 15, 9, 12);
			meeting.setDescription("Team meeting");
			calendar.addMeeting(meeting);

			assertTrue(calendar.isBusy(3, 15, 9, 12),
					"Уулзалт амжилттай нэмэгдсэн байх ёстой");
		} catch(TimeConflictException e) {
			fail("Зөв огноо цагт exception шидэх ёсгүй: " + e.getMessage());
		}
	}

	@Test
	public void testAddMeeting_holiday() {
		try {
			Meeting midsommar = new Meeting(6, 26, "Midsommar");
			calendar.addMeeting(midsommar);

			assertTrue(calendar.isBusy(6, 26, 0, 23),
					"Баярын өдөр календарьт тэмдэглэгдсэн байх ёстой");
		} catch(TimeConflictException e) {
			fail("Exception шидэх ёсгүй: " + e.getMessage());
		}
	}

	@Test
	public void testIsBusy_emptyCalendar() {
		try {
			assertFalse(calendar.isBusy(5, 10, 14, 16),
					"Хоосон календарь busy байх ёсгүй");
		} catch(TimeConflictException e) {
			fail("Exception шидэх ёсгүй: " + e.getMessage());
		}
	}

	@Test
	public void testPrintAgenda_month() {
		String agenda = calendar.printAgenda(3);
		assertNotNull(agenda, "Agenda null байх ёсгүй");
		assertTrue(agenda.contains("Agenda for 3"),
				"Agenda сарын мэдээлэл агуулах ёстой");
	}

	@Test
	public void testPrintAgenda_day() {
		String agenda = calendar.printAgenda(3, 15);
		assertNotNull(agenda, "Agenda null байх ёсгүй");
		assertTrue(agenda.contains("Agenda for 3/15"),
				"Agenda өдрийн мэдээлэл агуулах ёстой");
	}

	// ========== БУРУУ ОГНОО ТОХИОЛДЛУУД ==========

	@Test
	public void testAddMeeting_february30() {
		Meeting meeting = new Meeting(2, 30, 10, 12);
		meeting.setDescription("Invalid date");

		TimeConflictException exception = assertThrows(
				TimeConflictException.class,
				() -> calendar.addMeeting(meeting),
				"2-р сарын 30-ны өдөр байхгүй, exception шидэх ёстой"
		);

		assertTrue(exception.getMessage().contains("Overlap") ||
						exception.getMessage().contains("Day does not exist"),
				"Алдааны мессеж буруу өдрийн тухай байх ёстой");
	}

	@Test
	public void testAddMeeting_february31() {
		Meeting meeting = new Meeting(2, 31, 10, 12);
		meeting.setDescription("Invalid date");

		assertThrows(TimeConflictException.class,
				() -> calendar.addMeeting(meeting),
				"2-р сарын 31-ны өдөр байхгүй, exception шидэх ёстой");
	}

	@Test
	public void testAddMeeting_april31() {
		Meeting meeting = new Meeting(4, 31, 10, 12);
		meeting.setDescription("Invalid date");

		assertThrows(TimeConflictException.class,
				() -> calendar.addMeeting(meeting),
				"4-р сарын 31-ны өдөр байхгүй, exception шидэх ёстой");
	}

	@Test
	public void testAddMeeting_june31() {
		Meeting meeting = new Meeting(6, 31, 10, 12);
		meeting.setDescription("Invalid date");

		assertThrows(TimeConflictException.class,
				() -> calendar.addMeeting(meeting),
				"6-р сарын 31-ны өдөр байхгүй, exception шидэх ёстой");
	}

	@Test
	public void testAddMeeting_september31() {
		Meeting meeting = new Meeting(9, 31, 10, 12);
		meeting.setDescription("Invalid date");

		assertThrows(TimeConflictException.class,
				() -> calendar.addMeeting(meeting),
				"9-р сарын 31-ны өдөр байхгүй, exception шидэх ёстой");
	}

	@Test
	public void testAddMeeting_november31() {
		Meeting meeting = new Meeting(11, 31, 10, 12);
		meeting.setDescription("Invalid date");

		assertThrows(TimeConflictException.class,
				() -> calendar.addMeeting(meeting),
				"11-р сарын 31-ны өдөр байхгүй, exception шидэх ёстой");
	}

	// ========== БУРУУ САР ТОХИОЛДЛУУД ==========

	@Test
	public void testCheckTimes_invalidMonth0() {
		TimeConflictException exception = assertThrows(
				TimeConflictException.class,
				() -> Calendar.checkTimes(0, 15, 9, 12),
				"0-р сар байхгүй, exception шидэх ёстой"
		);
		assertTrue(exception.getMessage().contains("Month does not exist"));
	}

	@Test
	public void testCheckTimes_invalidMonth13() {
		TimeConflictException exception = assertThrows(
				TimeConflictException.class,
				() -> Calendar.checkTimes(13, 15, 9, 12),
				"13-р сар байхгүй, exception шидэх ёстой"
		);
		assertTrue(exception.getMessage().contains("Month does not exist"));
	}

	@Test
	public void testCheckTimes_negativeMonth() {
		assertThrows(TimeConflictException.class,
				() -> Calendar.checkTimes(-1, 15, 9, 12),
				"Сөрөг сар буруу, exception шидэх ёстой");
	}

	// ========== БУРУУ ӨДӨР ТОХИОЛДЛУУД ==========

	@Test
	public void testCheckTimes_invalidDay0() {
		TimeConflictException exception = assertThrows(
				TimeConflictException.class,
				() -> Calendar.checkTimes(3, 0, 9, 12),
				"0-р өдөр байхгүй, exception шидэх ёстой"
		);
		assertTrue(exception.getMessage().contains("Day does not exist"));
	}

	@Test
	public void testCheckTimes_invalidDay32() {
		TimeConflictException exception = assertThrows(
				TimeConflictException.class,
				() -> Calendar.checkTimes(3, 32, 9, 12),
				"32-р өдөр байхгүй, exception шидэх ёстой"
		);
		assertTrue(exception.getMessage().contains("Day does not exist"));
	}

	@Test
	public void testCheckTimes_negativeDay() {
		assertThrows(TimeConflictException.class,
				() -> Calendar.checkTimes(3, -5, 9, 12),
				"Сөрөг өдөр буруу, exception шидэх ёстой");
	}

	// ========== БУРУУ ЦАГ ТОХИОЛДЛУУД ==========

	@Test
	public void testCheckTimes_invalidStartHour24() {
		TimeConflictException exception = assertThrows(
				TimeConflictException.class,
				() -> Calendar.checkTimes(3, 15, 24, 25),
				"24 цаг буруу, exception шидэх ёстой"
		);
		assertTrue(exception.getMessage().contains("Illegal hour"));
	}

	@Test
	public void testCheckTimes_invalidEndHour25() {
		TimeConflictException exception = assertThrows(
				TimeConflictException.class,
				() -> Calendar.checkTimes(3, 15, 10, 25),
				"25 цаг буруу, exception шидэх ёстой"
		);
		assertTrue(exception.getMessage().contains("Illegal hour"));
	}

	@Test
	public void testCheckTimes_negativeStartHour() {
		assertThrows(TimeConflictException.class,
				() -> Calendar.checkTimes(3, 15, -1, 12),
				"Сөрөг цаг буруу, exception шидэх ёстой");
	}

	@Test
	public void testCheckTimes_negativeEndHour() {
		assertThrows(TimeConflictException.class,
				() -> Calendar.checkTimes(3, 15, 9, -1),
				"Сөрөг цаг буруу, exception шидэх ёстой");
	}

	@Test
	public void testCheckTimes_startEqualsEnd() {
		TimeConflictException exception = assertThrows(
				TimeConflictException.class,
				() -> Calendar.checkTimes(3, 15, 12, 12),
				"Эхлэх ба дуусах цаг тэнцүү байж болохгүй"
		);
		assertTrue(exception.getMessage().contains("starts before it ends"));
	}

	@Test
	public void testCheckTimes_startGreaterThanEnd() {
		TimeConflictException exception = assertThrows(
				TimeConflictException.class,
				() -> Calendar.checkTimes(3, 15, 15, 12),
				"Эхлэх цаг дуусах цагаас их байж болохгүй"
		);
		assertTrue(exception.getMessage().contains("starts before it ends"));
	}

	// ========== ДАВХЦАХ УУЛЗАЛТ ТОХИОЛДЛУУД ==========

	@Test
	public void testAddMeeting_overlappingMeetings() {
		try {
			Meeting meeting1 = new Meeting(5, 20, 10, 12);
			meeting1.setDescription("First meeting");
			calendar.addMeeting(meeting1);

			Meeting meeting2 = new Meeting(5, 20, 11, 13);
			meeting2.setDescription("Overlapping meeting");

			TimeConflictException exception = assertThrows(
					TimeConflictException.class,
					() -> calendar.addMeeting(meeting2),
					"Давхцах уулзалт нэмэхэд exception шидэх ёстой"
			);

			assertTrue(exception.getMessage().contains("Overlap"),
					"Алдааны мессеж давхцлын тухай байх ёстой");

		} catch(TimeConflictException e) {
			fail("Эхний уулзалт нэмэхэд алдаа гарах ёсгүй: " + e.getMessage());
		}
	}

	@Test
	public void testAddMeeting_exactOverlap() {
		try {
			Meeting meeting1 = new Meeting(7, 10, 14, 16);
			meeting1.setDescription("Meeting 1");
			calendar.addMeeting(meeting1);

			Meeting meeting2 = new Meeting(7, 10, 14, 16);
			meeting2.setDescription("Meeting 2 - Same time");

			assertThrows(TimeConflictException.class,
					() -> calendar.addMeeting(meeting2),
					"Яг ижил цагт уулзалт нэмэхэд exception шидэх ёстой");

		} catch(TimeConflictException e) {
			fail("Эхний уулзалт нэмэхэд алдаа гарах ёсгүй: " + e.getMessage());
		}
	}

	@Test
	public void testAddMeeting_startTimeOverlap() {
		try {
			Meeting meeting1 = new Meeting(8, 5, 9, 11);
			meeting1.setDescription("Morning meeting");
			calendar.addMeeting(meeting1);

			Meeting meeting2 = new Meeting(8, 5, 10, 12);
			meeting2.setDescription("Overlapping at start");

			assertThrows(TimeConflictException.class,
					() -> calendar.addMeeting(meeting2),
					"Эхлэх цаг давхцах тохиолдолд exception шидэх ёстой");

		} catch(TimeConflictException e) {
			fail("Эхний уулзалт нэмэхэд алдаа гарах ёсгүй: " + e.getMessage());
		}
	}

	@Test
	public void testAddMeeting_endTimeOverlap() {
		try {
			Meeting meeting1 = new Meeting(8, 5, 14, 16);
			meeting1.setDescription("Afternoon meeting");
			calendar.addMeeting(meeting1);

			Meeting meeting2 = new Meeting(8, 5, 13, 15);
			meeting2.setDescription("Overlapping at end");

			assertThrows(TimeConflictException.class,
					() -> calendar.addMeeting(meeting2),
					"Дуусах цаг давхцах тохиолдолд exception шидэх ёстой");

		} catch(TimeConflictException e) {
			fail("Эхний уулзалт нэмэхэд алдаа гарах ёсгүй: " + e.getMessage());
		}
	}

	// ========== БУСАД ФУНКЦ ТОХИОЛДЛУУД ==========

	@Test
	public void testClearSchedule() {
		try {
			Meeting meeting = new Meeting(4, 12, 10, 12);
			meeting.setDescription("Meeting to clear");
			calendar.addMeeting(meeting);

			assertTrue(calendar.isBusy(4, 12, 10, 12),
					"Уулзалт нэмэгдсэн байх ёстой");

			calendar.clearSchedule(4, 12);

			assertFalse(calendar.isBusy(4, 12, 10, 12),
					"Цэвэрлэсний дараа busy байх ёсгүй");

		} catch(TimeConflictException e) {
			fail("Exception шидэх ёсгүй: " + e.getMessage());
		}
	}

	@Test
	public void testGetMeeting() {
		try {
			Meeting meeting = new Meeting(5, 15, 9, 11);
			meeting.setDescription("Test meeting");
			calendar.addMeeting(meeting);

			Meeting retrieved = calendar.getMeeting(5, 15, 0);
			assertNotNull(retrieved, "Уулзалт олдох ёстой");
			assertEquals("Test meeting", retrieved.getDescription(),
					"Уулзалтын тайлбар таарах ёстой");

		} catch(TimeConflictException e) {
			fail("Exception шидэх ёсгүй: " + e.getMessage());
		}
	}

	@Test
	public void testRemoveMeeting() {
		try {
			Meeting meeting = new Meeting(6, 20, 14, 16);
			meeting.setDescription("Meeting to remove");
			calendar.addMeeting(meeting);

			assertTrue(calendar.isBusy(6, 20, 14, 16),
					"Уулзалт нэмэгдсэн байх ёстой");

			calendar.removeMeeting(6, 20, 0);

			assertFalse(calendar.isBusy(6, 20, 14, 16),
					"Устгасны дараа busy байх ёсгүй");

		} catch(TimeConflictException e) {
			fail("Exception шидэх ёсгүй: " + e.getMessage());
		}
	}
}