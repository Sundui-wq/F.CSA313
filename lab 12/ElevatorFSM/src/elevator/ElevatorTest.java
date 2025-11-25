package elevator;
public class ElevatorTest {
    public static void main(String[] args) {
        testBasicMovement();
        testCallFromFloor();
        testMultiFloorJourney();
        testSameFloorRequest();
        testDoorOperations();
        testDoorSafety();
        testInvalidOperations();
        testDoorObstruction();
        testSafetyProperties();
    }
    //ТЕСТ 1: Үндсэн хөдөлгөөн - Зоорьноос 2-р давхар (лифт дотроос)
    private static void testBasicMovement() {
        printTestHeader("ТЕСТ 1: Лифт дотроос товч дарах (Зоорь → 2-р давхар)");

        ElevatorStateMachine elevator = new ElevatorStateMachine();

        // Эхлэлийн төлөв шалгах
        assertEquals(ElevatorStateMachine.State.IDLE, elevator.getCurrentState(),
                "Эхлэлд IDLE төлөвт байх ёстой");
        assertEquals(ElevatorStateMachine.Floor.GROUND, elevator.getCurrentFloor(),
                "Эхлэлд зоорьт байх ёстой");

        // Лифт дотроос 2-р давхарын товч дарах
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_SECOND);
        assertEquals(ElevatorStateMachine.State.MOVING_UP, elevator.getCurrentState(),
                "Дээшээ хөдөлж эхлэх ёстой");

        // Давхар дээр ирэх
        elevator.processEvent(ElevatorStateMachine.Event.ARRIVED);
        assertEquals(ElevatorStateMachine.Floor.SECOND, elevator.getCurrentFloor(),
                "2-р давхарт ирсэн байх ёстой");
        assertEquals(ElevatorStateMachine.State.DOOR_OPENING, elevator.getCurrentState(),
                "Хаалга нээгдэж эхлэх ёстой");

        // Хаалга нээгдэх
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);
        assertEquals(ElevatorStateMachine.State.DOOR_OPEN, elevator.getCurrentState(),
                "Хаалга нээлттэй байх ёстой");

        // Хаалга хаагдах
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_CLOSE_COMPLETE);
        assertEquals(ElevatorStateMachine.State.IDLE, elevator.getCurrentState(),
                "IDLE төлөвт буцах ёстой");

        printTestResult("Тест 1 - Лифт дотроос товч");
    }
    //ТЕСТ 2: Давхар дээрээс лифт дуудах
    private static void testCallFromFloor() {
        printTestHeader("ТЕСТ 2: Давхар дээрээс лифт дуудах");

        ElevatorStateMachine elevator = new ElevatorStateMachine();

        // 1-р давхараас лифт дуудах (лифт зоорьт байна)
        System.out.println("\n--- Тохиолдол 1: 1-р давхараас дуудах ---");
        elevator.processEvent(ElevatorStateMachine.Event.CALL_FROM_FIRST);
        assertEquals(ElevatorStateMachine.State.MOVING_UP, elevator.getCurrentState(),
                "Лифт 1-р давхар руу дээшээ явах ёстой");
        assertEquals(ElevatorStateMachine.Floor.FIRST, elevator.getTargetFloor(),
                "Зорилтот давхар 1 байх ёстой");

        // Лифт ирж хаалга нээх
        elevator.processEvent(ElevatorStateMachine.Event.ARRIVED);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);
        assertEquals(ElevatorStateMachine.Floor.FIRST, elevator.getCurrentFloor(),
                "1-р давхарт ирсэн байх ёстой");

        // Хаалга хаах
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_CLOSE_COMPLETE);

        // Одоо 2-р давхар руу явж, дараа нь зоорьноос дуудах
        System.out.println("\n--- Тохиолдол 2: Зоорьноос дуудах (лифт 1-р давхарт байна) ---");
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_SECOND);
        elevator.processEvent(ElevatorStateMachine.Event.ARRIVED);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_CLOSE_COMPLETE);

        // Одоо лифт 2-р давхарт байна, зоорьноос дуудах
        elevator.processEvent(ElevatorStateMachine.Event.CALL_FROM_GROUND);
        assertEquals(ElevatorStateMachine.State.MOVING_DOWN, elevator.getCurrentState(),
                "Лифт зоорь руу доошоо явах ёстой");

        elevator.processEvent(ElevatorStateMachine.Event.ARRIVED);
        assertEquals(ElevatorStateMachine.Floor.GROUND, elevator.getCurrentFloor(),
                "Зоорьт ирсэн байх ёстой");

        printTestResult("Тест 2 - Давхараас дуудах");
    }
    //ТЕСТ 3: Олон давхрын аялал
    private static void testMultiFloorJourney() {
        printTestHeader("ТЕСТ 3: Олон давхрын аялал (Зоорь → 2 → 1 → Зоорь)");

        ElevatorStateMachine elevator = new ElevatorStateMachine();

        // Зоорь → 2-р давхар
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_SECOND);
        assertEquals(ElevatorStateMachine.Floor.SECOND, elevator.getTargetFloor(),
                "Зорилтот давхар 2 байх ёстой");

        moveToFloor(elevator, ElevatorStateMachine.Event.INSIDE_BUTTON_SECOND);
        assertEquals(ElevatorStateMachine.Floor.SECOND, elevator.getCurrentFloor(),
                "2-р давхарт байх ёстой");

        // 2-р → 1-р давхар
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_FIRST);
        assertEquals(ElevatorStateMachine.Floor.FIRST, elevator.getTargetFloor(),
                "Зорилтот давхар 1 байх ёстой");

        moveToFloor(elevator, ElevatorStateMachine.Event.INSIDE_BUTTON_FIRST);
        assertEquals(ElevatorStateMachine.Floor.FIRST, elevator.getCurrentFloor(),
                "1-р давхарт байх ёстой");

        // 1-р → Зоорь
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_GROUND);
        assertEquals(ElevatorStateMachine.Floor.GROUND, elevator.getTargetFloor(),
                "Зорилтот давхар зоорь байх ёстой");

        moveToFloor(elevator, ElevatorStateMachine.Event.INSIDE_BUTTON_GROUND);
        assertEquals(ElevatorStateMachine.Floor.GROUND, elevator.getCurrentFloor(),
                "Зоорьт буцаж ирсэн байх ёстой");

        printTestResult("Тест 3 - Олон давхрын аялал");
    }
    //ТЕСТ 4: Одоогийн давхарын товчийг дарах
    private static void testSameFloorRequest() {
        printTestHeader("ТЕСТ 4: Одоогийн давхарын товчийг дарах");

        ElevatorStateMachine elevator = new ElevatorStateMachine();

        // Зоорьт байхад зоорьны товчийг дарах (лифт дотроос)
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_GROUND);
        assertEquals(ElevatorStateMachine.State.DOOR_OPENING, elevator.getCurrentState(),
                "Хаалга шууд нээгдэх ёстой");
        assertEquals(ElevatorStateMachine.Floor.GROUND, elevator.getCurrentFloor(),
                "Зоорьт үлдэх ёстой");

        elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);
        assertEquals(ElevatorStateMachine.State.DOOR_OPEN, elevator.getCurrentState(),
                "Хаалга нээлттэй байх ёстой");

        // Хаалга хаах
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_CLOSE_COMPLETE);

        // Зоорьноос лифт дуудах (лифт зоорьт байна)
        elevator.processEvent(ElevatorStateMachine.Event.CALL_FROM_GROUND);
        assertEquals(ElevatorStateMachine.State.DOOR_OPENING, elevator.getCurrentState(),
                "Одоогийн давхараас дуудахад хаалга шууд нээгдэх ёстой");

        printTestResult("Тест 4 - Одоогийн давхарын товч");
    }
    //ТЕСТ 5: Хаалганы үйл ажиллагаа
    private static void testDoorOperations() {
        printTestHeader("ТЕСТ 5: Хаалганы үйл ажиллагаа");

        ElevatorStateMachine elevator = new ElevatorStateMachine();

        // Эхлэлд зорилтот давхар null байх ёстой
        assertEquals(null, elevator.getTargetFloor(),
                "Эхлэлд зорилтот давхар null байх ёстой");

        // Хаалга нээх
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_GROUND);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);
        assertEquals(ElevatorStateMachine.State.DOOR_OPEN, elevator.getCurrentState(),
                "Хаалга нээлттэй байх ёстой");

        // Хаалга нээлттэй байхад товч дарах - хугацаа сунгагдана
        boolean result = elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_GROUND);
        assertTrue(result, "Товч дарагдах ёстой");
        assertEquals(ElevatorStateMachine.State.DOOR_OPEN, elevator.getCurrentState(),
                "Хаалга нээлттэй үлдэх ёстой");

        // Хаалга хаагдсаны дараа зорилтот давхар null болох
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_CLOSE_COMPLETE);
        assertEquals(null, elevator.getTargetFloor(),
                "Хаалга хаагдсаны дараа зорилтот давхар null болох ёстой");

        printTestResult("Тест 5 - Хаалганы үйл ажиллагаа");
    }
    //ТЕСТ 6: Хаалганы аюулгүй байдал
    private static void testDoorSafety() {
        printTestHeader("ТЕСТ 6: Хаалганы аюулгүй байдлын онцлог");

        ElevatorStateMachine elevator = new ElevatorStateMachine();

        // Хаалга нээх
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_GROUND);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);

        // Хаалга хаагдаж эхлэх
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        assertEquals(ElevatorStateMachine.State.DOOR_CLOSING, elevator.getCurrentState(),
                "Хаалга хаагдаж эхлэх ёстой");

        // Хаалга хаагдаж байхад товч дарах - дахин нээгдэнэ (Safety)
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_GROUND);
        assertEquals(ElevatorStateMachine.State.DOOR_OPENING, elevator.getCurrentState(),
                "Аюулгүй байдлын үүднээс хаалга дахин нээгдэх ёстой");

        printTestResult("Тест 6 - Хаалганы аюулгүй байдал");
    }
    //ТЕСТ 7: Буруу үйлдлүүд
    private static void testInvalidOperations() {
        printTestHeader("ТЕСТ 7: Буруу үйлдлүүд");

        ElevatorStateMachine elevator = new ElevatorStateMachine();

        // IDLE төлөвт ARRIVED үзэгдэл илгээх
        boolean result = elevator.processEvent(ElevatorStateMachine.Event.ARRIVED);
        assertFalse(result, "IDLE төлөвт ARRIVED хүлээн авагдахгүй байх ёстой");

        // Хөдөлж байхад хаалга нээх оролдлого
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_SECOND);
        assertTrue(elevator.isMoving(), "Хөдөлж байх ёстой");

        boolean doorResult = elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);
        assertFalse(doorResult, "Хөдөлж байхад хаалга нээгдэхгүй байх ёстой");

        printTestResult("Тест 7 - Буруу үйлдлүүд");
    }
    //ТЕСТ 8: Хаалганы саад

    private static void testDoorObstruction() {
        printTestHeader("ТЕСТ 8: Хаалганы саадтай нөхцөл");

        ElevatorStateMachine elevator = new ElevatorStateMachine();

        // Хаалга нээх
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_GROUND);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);

        // Саад тохиолдох
        elevator.setDoorObstruction(true);
        assertTrue(elevator.hasDoorObstruction(),
                "Саад тохиолдсон байх ёстой");

        // Хаалга хаагдахыг оролдох
        boolean result = elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        assertFalse(result, "Саадтай үед хаалга хаагдахгүй байх ёстой");
        assertEquals(ElevatorStateMachine.State.DOOR_OPEN, elevator.getCurrentState(),
                "Хаалга нээлттэй үлдэх ёстой");

        // Саадыг арилгах
        elevator.setDoorObstruction(false);
        assertFalse(elevator.hasDoorObstruction(),
                "Саад арилсан байх ёстой");

        result = elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        assertTrue(result, "Саадгүй болсны дараа хаалга хаагдах ёстой");

        printTestResult("Тест 8 - Хаалганы саад");
    }
    //ТЕСТ 9: Шинж төлөвийн шалгалтууд
    private static void testSafetyProperties() {
        printTestHeader("ТЕСТ 9: Шинж төлөвийн шалгалтууд");

        ElevatorStateMachine elevator = new ElevatorStateMachine();

        System.out.println("\n  1️⃣ Хаалганы аюулгүй байдал:");

        // Хөдөлж байхад хаалга хаалттай
        elevator.processEvent(ElevatorStateMachine.Event.INSIDE_BUTTON_SECOND);
        assertTrue(elevator.isMovementSafe(),
                "Хөдөлж байхад хаалга хаалттай байх ёстой");
        assertTrue(elevator.isDoorSafetyMaintained(),
                "Хаалганы аюулгүй байдал хадгалагдсан");

        // Зогссон үед хаалга нээгдэх
        elevator.processEvent(ElevatorStateMachine.Event.ARRIVED);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);
        assertTrue(elevator.isDoorSafetyMaintained(),
                "Зогссон үед хаалга нээгдэж болно");
        assertFalse(elevator.isMoving(),
                "Хаалга нээлттэй үед хөдлөхгүй байх ёстой");

        // Хаалга хаагдсаны дараа targetFloor null болох
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_CLOSE_COMPLETE);
        assertEquals(null, elevator.getTargetFloor(),
                "Хаалга хаагдсаны дараа зорилтот давхар null болох ёстой");

        System.out.println("\n  2️⃣ Төлөвийн хүчинтэй байдал:");
        assertTrue(elevator.isValidState(),
                "Төлөв үргэлж хүчинтэй байх ёстой");

        System.out.println("\n  3️⃣ Давхарын хязгаар:");
        int level = elevator.getCurrentFloor().getLevel();
        assertTrue(level >= 0 && level <= 2,
                "Давхар 0-2 хооронд байх ёстой");

        System.out.println("\n  4️⃣ Хаалганы саадны статус:");
        assertFalse(elevator.hasDoorObstruction(),
                "Саадгүй төлөвт байх ёстой");

        printTestResult("Тест 9 - Шинж төлөвийн шалгалт");
    }
    // Helper методууд Лифтийг давхар руу шилжүүлэх бүрэн процесс
    private static void moveToFloor(ElevatorStateMachine elevator,
                                    ElevatorStateMachine.Event floorButton) {
        elevator.processEvent(floorButton);
        if (elevator.isMoving()) {
            elevator.processEvent(ElevatorStateMachine.Event.ARRIVED);
        }
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_OPEN_COMPLETE);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_TIMEOUT);
        elevator.processEvent(ElevatorStateMachine.Event.DOOR_CLOSE_COMPLETE);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        boolean isEqual = (expected == null && actual == null) ||
                (expected != null && expected.equals(actual));

        if (isEqual) {
            System.out.println("  ✓ " + message);
        } else {
            System.out.println("  ✗ " + message);
            System.out.println("    Хүлээгдсэн: " + expected + ", Гарсан: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("  ✓ " + message);
        } else {
            System.out.println("  ✗ " + message + " - Буруу утга!");
        }
    }

    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }

    private static void printTestHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(title);
        System.out.println("=".repeat(60));
    }

    private static void printTestResult(String testName) {
        System.out.println("\n✅ " + testName + " дууслаа\n");
    }
}