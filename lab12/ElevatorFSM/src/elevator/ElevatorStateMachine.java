package elevator;

public class ElevatorStateMachine {

    public enum State {
        IDLE("Сул зогсож байна"),
        DOOR_OPENING("Хаалга нээгдэж байна"),
        DOOR_OPEN("Хаалга нээлттэй"),
        DOOR_CLOSING("Хаалга хаагдаж байна"),
        MOVING_UP("Дээшээ явж байна"),
        MOVING_DOWN("Доошоо явж байна");

        private final String description;

        State(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public enum Event {
        // Лифт дотроос дарах товчнууд
        INSIDE_BUTTON_GROUND("Лифт дотроос: Зоорь руу"),
        INSIDE_BUTTON_FIRST("Лифт дотроос: 1-р давхар руу"),
        INSIDE_BUTTON_SECOND("Лифт дотроос: 2-р давхар руу"),

        // Давхар дээрээс дуудах товчнууд
        CALL_FROM_GROUND("Зоорьноос лифт дуудах"),
        CALL_FROM_FIRST("1-р давхараас лифт дуудах"),
        CALL_FROM_SECOND("2-р давхараас лифт дуудах"),

        // Систем үзэгдлүүд
        ARRIVED("Давхар дээр ирлээ"),
        DOOR_OPEN_COMPLETE("Хаалга бүрэн нээгдсэн"),
        DOOR_CLOSE_COMPLETE("Хаалга бүрэн хаагдсан"),
        DOOR_TIMEOUT("Хаалга автоматаар хаагдах");

        private final String description;

        Event(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    // Давхар
    public enum Floor {
        GROUND(0, "Зоорь"),
        FIRST(1, "1-р давхар"),
        SECOND(2, "2-р давхар");

        private final int level;
        private final String name;

        Floor(int level, String name) {
            this.level = level;
            this.name = name;
        }

        public int getLevel() {
            return level;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Instance variables
    private State currentState;
    private Floor currentFloor;
    private Floor targetFloor;
    private boolean doorObstruction;

    public ElevatorStateMachine() {
        this.currentState = State.IDLE;
        this.currentFloor = Floor.GROUND;
        this.targetFloor = null;
        this.doorObstruction = false;
    }

    /**
     * Үзэгдлийг боловсруулж, төлөв шилжүүлэх
     * @param event Үзэгдэл
     * @return Амжилттай эсэх
     */
    public boolean processEvent(Event event) {
        System.out.println("\n>>> Үзэгдэл: " + event);
        System.out.println("    Одоогийн: " + currentState + " @ " + currentFloor);

        State previousState = currentState;
        boolean success = handleStateTransition(event);

        if (success && previousState != currentState) {
            System.out.println("    Шинэ төлөв: " + currentState);
        } else if (!success) {
            System.out.println("    ⚠ Үзэгдэл зөвшөөрөгдөөгүй");
        }

        return success;
    }

    //Төлөв шилжилтийг боловсруулах

    private boolean handleStateTransition(Event event) {
        switch (currentState) {
            case IDLE:
                return handleIdleState(event);
            case DOOR_OPENING:
                return handleDoorOpeningState(event);
            case DOOR_OPEN:
                return handleDoorOpenState(event);
            case DOOR_CLOSING:
                return handleDoorClosingState(event);
            case MOVING_UP:
            case MOVING_DOWN:
                return handleMovingState(event);
            default:
                return false;
        }
    }

    private boolean handleIdleState(Event event) {
        Floor requestedFloor = getRequestedFloor(event);
        if (requestedFloor == null) {
            return false;
        }

        if (currentFloor == requestedFloor) {
            // Одоогийн давхар - хаалга нээх
            transitionToDoorOpening();
            return true;
        }

        // Өөр давхар руу хөдлөх
        targetFloor = requestedFloor;
        transitionToMoving();
        return true;
    }

    private boolean handleDoorOpeningState(Event event) {
        if (event == Event.DOOR_OPEN_COMPLETE) {
            currentState = State.DOOR_OPEN;
            System.out.println("    ✓ Хаалга бүрэн нээгдлээ");
            return true;
        }
        return false;
    }

    private boolean handleDoorOpenState(Event event) {
        if (event == Event.DOOR_TIMEOUT || event == Event.DOOR_CLOSE_COMPLETE) {
            if (doorObstruction) {
                System.out.println("    ⚠ Хаалганд саад байна - хаагдахгүй");
                return false;
            }
            transitionToDoorClosing();
            return true;
        }

        // Хаалга нээлттэй байхад товч дарвал хугацаа сунгана
        if (getRequestedFloor(event) != null) {
            System.out.println("    ⏰ Хаалга хаагдах хугацаа сунгагдлаа");
            return true;
        }

        return false;
    }

    private boolean handleDoorClosingState(Event event) {
        if (event == Event.DOOR_CLOSE_COMPLETE) {
            currentState = State.IDLE;
            targetFloor = null;
            System.out.println("    ✓ Хаалга бүрэн хаагдлаа");
            return true;
        }

        // Safety: Хаалга хаагдаж байхад товч дарвал дахин нээгдэнэ
        if (getRequestedFloor(event) != null) {
            System.out.println("    🛡 Аюулгүй байдал: Хаалга дахин нээгдэж байна");
            transitionToDoorOpening();
            return true;
        }

        return false;
    }

    private boolean handleMovingState(Event event) {
        if (event == Event.ARRIVED) {
            currentFloor = targetFloor;
            System.out.println("    ✓ " + currentFloor + " дээр ирлээ");
            transitionToDoorOpening();
            return true;
        }
        return false;
    }
    //Үзэгдлээс хүссэн давхарыг олох
    private Floor getRequestedFloor(Event event) {
        switch (event) {
            case INSIDE_BUTTON_GROUND:
            case CALL_FROM_GROUND:
                return Floor.GROUND;
            case INSIDE_BUTTON_FIRST:
            case CALL_FROM_FIRST:
                return Floor.FIRST;
            case INSIDE_BUTTON_SECOND:
            case CALL_FROM_SECOND:
                return Floor.SECOND;
            default:
                return null;
        }
    }

    /**
     * Төлөв шилжилтийн методууд
     */
    private void transitionToDoorOpening() {
        currentState = State.DOOR_OPENING;
        System.out.println("    🚪 Хаалга нээгдэж эхэллээ...");
    }

    private void transitionToDoorClosing() {
        currentState = State.DOOR_CLOSING;
        System.out.println("    🚪 Хаалга хаагдаж эхэллээ...");
    }

    private void transitionToMoving() {
        if (targetFloor.getLevel() > currentFloor.getLevel()) {
            currentState = State.MOVING_UP;
            System.out.println("    ⬆ Дээшээ хөдөлж байна → " + targetFloor);
        } else {
            currentState = State.MOVING_DOWN;
            System.out.println("    ⬇ Доошоо хөдөлж байна → " + targetFloor);
        }
    }

    /**
     * Safety Property шалгалтууд
     */
    public boolean isDoorSafetyMaintained() {
        // Хаалга зөвхөн зогссон үед нээгдэх
        if (currentState == State.DOOR_OPEN || currentState == State.DOOR_OPENING) {
            return !isMoving();
        }
        return true;
    }

    public boolean isMovementSafe() {
        // Хөдөлж байхад хаалга хаалттай байх
        if (isMoving()) {
            return currentState != State.DOOR_OPEN &&
                    currentState != State.DOOR_OPENING;
        }
        return true;
    }

    public boolean isMoving() {
        return currentState == State.MOVING_UP || currentState == State.MOVING_DOWN;
    }

    public boolean isValidState() {
        return currentState != null && currentFloor != null;
    }

    /**
     * Getters
     */
    public State getCurrentState() {
        return currentState;
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public Floor getTargetFloor() {
        return targetFloor;
    }

    public boolean hasDoorObstruction() {
        return doorObstruction;
    }

    public void setDoorObstruction(boolean obstruction) {
        this.doorObstruction = obstruction;
        if (obstruction) {
            System.out.println("    ⚠ Хаалганд саад илэрлээ!");
        } else {
            System.out.println("    ✓ Хаалганы саад арилсан");
        }
    }
}