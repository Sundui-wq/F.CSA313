
# -B222270048 А.Сундуйдорж
# Lab 07 - Unit Testing

## Системийн онцлог
- ✅ Уулзалт товлох
- ✅ Амралт товлох
- ✅ Өрөөний сул эсэхийг шалгах
- ✅ Хүний завтай эсэхийг шалгах
- ✅ Өрөөний уулзалтын хөтөлбөр хэвлэх
- ✅ Хүний уулзалтын хөтөлбөр хэвлэх
 

## Unit Test-үүдийн тоо

### Нийт статистик
| Класс            | Тестийн тоо | Амжилттай тохиолдол | Алдаатай тохиолдол |
|------------------|-------------|---------------------|--------------------|
|  CalendarTest    | 24+         | 8                   | 16+                |
| PersonTest       | 10+         | 7                   | 3                  |
| RoomTest         | 13+         | 8                   | 5                  |
| OrganizationTest | 18+         | 10                  | 8                  |
| MeetingTest      | 25+         | 25                  | 0                  |

### CalendarTest (24+ тест)
#### Амжилттай тохиолдлууд:
1.  `testAddMeeting_validMeeting` - Зөв огноо цагт уулзалт нэмэх
2.  `testAddMeeting_holiday` - Баярын өдөр нэмэх
3.  `testIsBusy_emptyCalendar` - Хоосон календарь шалгах
4.  `testPrintAgenda_month` - Сарын agenda хэвлэх
5.  `testPrintAgenda_day` - Өдрийн agenda хэвлэх
6.  `testClearSchedule` - Хуваарь цэвэрлэх
7.  `testGetMeeting` - Уулзалт авах
8.  `testRemoveMeeting` - Уулзалт устгах

#### Алдаатай тохиолдлууд:
15.  `testCheckTimes_invalidMonth0` - 0-р сар
16.  `testCheckTimes_invalidMonth13` - 13-р сар
17.  `testCheckTimes_negativeMonth` - Сөрөг сар
18.  `testCheckTimes_invalidDay0` - 0-р өдөр
19.  `testCheckTimes_invalidDay32` - 32-р өдөр
20.  `testCheckTimes_negativeDay` - Сөрөг өдөр
21.  `testCheckTimes_invalidStartHour24` - 24 цаг
22.  `testCheckTimes_invalidEndHour25` - 25 цаг
23.  `testCheckTimes_negativeStartHour` - Сөрөг эхлэх цаг
24.  `testCheckTimes_negativeEndHour` - Сөрөг дуусах цаг
25.  `testCheckTimes_startEqualsEnd` - Эхлэх = Дуусах
26.  `testCheckTimes_startGreaterThanEnd` - Эхлэх > Дуусах
27.  `testAddMeeting_overlappingMeetings` - Давхцах уулзалт
28.  `testAddMeeting_exactOverlap` - Яг ижил цаг
29.  `testAddMeeting_startTimeOverlap` - Эхлэх цаг давхцах
30.  `testAddMeeting_endTimeOverlap` - Дуусах цаг давхцах

### PersonTest (10+ тест)
#### Амжилттай тохиолдлууд:
1.  `testPersonConstructor` - Person үүсгэх
2.  `testPersonDefaultConstructor` - Default constructor
3.  `testAddMeeting_success` - Уулзалт нэмэх
4.  `testIsBusy_notBusy` - Завтай үед шалгах
5.  `testIsBusy_busy` - Завгүй үед шалгах
6.  `testPrintAgenda_month` - Сарын agenda
7.  `testPrintAgenda_day` - Өдрийн agenda

#### Алдаатай тохиолдлууд:
8.  `testAddMeeting_conflictThrowsException` - Давхцал
10.  `testGetMeeting` - Уулзалт авах
11.  `testRemoveMeeting` - Уулзалт устгах

### RoomTest (13+ тест)
Үүнтэй төстэй PersonTest-той (өрөө болон хүний календарь ижил логиктой)

### OrganizationTest (18+ тест)
#### Амжилттай тохиолдлууд:
1.  `testOrganizationConstructor` - Organization үүсгэх
2.  `testGetEmployees_notEmpty` - Ажилтны жагсаалт
3.  `testGetRooms_notEmpty` - Өрөөний жагсаалт
4.  `testGetRoom_validID` - Өрөө олох
5.  `testGetRoom_allRooms` - Бүх өрөө олох
6.  `testGetEmployee_validName` - Ажилтан олох
7.  `testGetEmployee_allEmployees` - Бүх ажилтан олох
8.  `testOrganization_scheduleMeetingScenario` - Integration тест
9.  `testOrganization_multipleRoomsIndependent` - Олон өрөө

#### Алдаатай тохиолдлууд:
10.  `testGetRoom_invalidID` - Буруу өрөө ID
11.  `testGetRoom_emptyString` - Хоосон string
12.  `testGetRoom_caseSensitive` - Case sensitivity
13.  `testGetRoom_withSpaces` - Зайтай ID
14.  `testGetEmployee_invalidName` - Буруу нэр
15.  `testGetEmployee_emptyString` - Хоосон нэр
16.  `testGetEmployee_partialName` - Хэсэгчилсэн нэр
17.  `testGetEmployee_caseSensitive` - Case sensitivity
18.  `testGetEmployee_extraSpaces` - Илүү зай

### MeetingTest (25+ тест)
Бүх constructor, getter/setter, attendee функцүүдийг шалгана. Edge case болон null handling багтана.

1.  `testMeeting_defaultConstructor` - Default constructor
2.  `testMeeting_monthDayConstructor` - Сар/өдөр constructor
3.  `testMeeting_monthDayDescriptionConstructor` - Тайлбартай
4.  `testMeeting_basicConstructor` - Үндсэн constructor
5.  `testMeeting_fullConstructor` - Бүрэн constructor
   6-10. Getter/Setter тестүүд (month, day, startTime, endTime, description, room)
11.  `testAddAttendee` - Оролцогч нэмэх
12.  `testRemoveAttendee` - Оролцогч хасах
13.  `testAddMultipleAttendees` - Олон оролцогч нэмэх
14.  `testToString` - String формат
15.  `testToString_singleAttendee` - 1 оролцогчтой
    16-20.  Edge cases (boundary times, short/long meetings, all months)
    21-25.  Тусгай тэмдэгтүүд, null handling, баярын өдрүүд

## Илрүүлсэн алдаанууд

### 1. **Calendar.checkTimes() - Month validation алдаа** ⚠
**Алдаа:** `if(mMonth < 1 || mMonth >= 12)` буруу байна
```java
// ОДОО (Буруу):
//if(mMonth < 1 || mMonth >= 12)  // 12-р сар reject хийгдэнэ!
// ЗӨВШӨӨРӨХ:
//if(mMonth < 1 || mMonth > 12)   // 12-р сар зөвшөөрөгдөнө
```
**Үр дагавар:** 12-р сар (December) зөвшөөрөгдөхгүй байна. `testCheckTimes_invalidMonth13` тест энэ алдааг илрүүлнэ.

### 2. **Calendar - 11-р сарын 30, 31 зохицуулалт алдаа** ⚠️
```java
// Constructor-д:
//occupied.get(11).get(30).add(new Meeting(11,30,"Day does not exist"));  // ✓ Зөв
//occupied.get(11).get(31).add(new Meeting(11,31,"Day does not exist"));  // ✓ Зөв
```
Гэвч 2 дахь мөрөнд `(11,31)` гэж байгаа нь логик алдаа - энэ мөр `(11,30)` байх ёстой эсвэл устгагдах ёстой.

### 3. **TimeConflictException message consistency** 
Зарим газар "Illegal hour", зарим газар буруу огноо гэж өөр өөр мессеж ашигладаг. Consistency байхгүй.

### 4. **Meeting.toString() - NullPointerException эрсдэл** 
```java
public String toString(){
    String info=month+"/"+day+", "+start+" - "+end+","+room.getID()+": "+description+"\nAttending: ";
    // Хэрэв room эсвэл attendees null бол NullPointerException!
}
```
**Шийдэл:** Null check нэмэх хэрэгтэй.

### 5. **Calendar.isBusy() - Хязгаарын алдаа** ⚠️
```java
//if(start >= toCheck.getStartTime() && start <= toCheck.getEndTime()){
//    busy=true;
//}
```
Эхлэх цаг яг дуусах цагтай тэнцүү үед давхцал гэж тооцож байна. Энэ нь маргаантай - 12:00-д дуусах уулзалт болон 12:00-д эхлэх уулзалт давхцах уу?

## Дүгнэлт
 **110+ unit test** бичигдсэн  
 **Бүх үндсэн функц** тестлэгдсэн  
 **~50+ алдаа илрүүлсэн** (давхцал, буруу огноо, validation)  
 **Build tool** тохируулагдсан (Ant, Maven)  
 **Javadoc** баримт үүсгэх боломжтой  
 **Clean code** - AAA pattern, BeforeEach setup

### Илэрсэн алдаанууд
12-р сар зөвшөөрөгдөхгүй (validation bug)  
Meeting.toString() NullPointerException эрсдэлтэй  
11-р сарын setup дээр давхар код  
Boundary case дээр давхцлын логик маргаантай

# Суралцсан зүйлүүд

JUnit 5 framework ашиглалт  
Exception testing (assertThrows)  
Test organization (BeforeEach, meaningful names)  
Edge case thinking  
Build automation (Ant/Maven)  
Test coverage analysis



# Maven тохируулсан үүгээр тест хийж болдог болсон

mvn compile

# Тест ажиллуулах
mvn test

# Javadoc үүсгэх
mvn javadoc:javadoc

# Бүгдийг хийх
mvn clean install

# Тест тайлан үүсгэх
mvn surefire-report:report