package TRAFFICSIM;

import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
public class Student extends Circle {
    private boolean isMale;
    private double walkingSpeed;
    private Room[] schedule;
    private int currentPeriod;
    private Transition currentAnimation;
    private boolean hasColorsOn;
    private Room currentClass;
    public Student(boolean isMale, double radius, double walkingSpeed, Room[] schedule, int currentPeriod) {
        super(radius);
        this.isMale = isMale;
        this.walkingSpeed = walkingSpeed;
        this.schedule = schedule;
        this.currentPeriod = currentPeriod;
        currentClass = schedule[currentPeriod];
        int[] start = currentClass == null ? new int[]{-1, -1} : currentClass.randSpotInside();
        setCenterX(start[0]);
        setCenterY(start[1]);
    }
    void nextPeriod() {
        try {
            goToRoom(schedule[currentPeriod + 1]);
            currentPeriod++;
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }
    void previousPeriod() {
        try {
            goToRoom(schedule[currentPeriod - 1]);
            currentPeriod--;
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }
    void goToRoom(Room nextClass) {
        if (nextClass == null) {
            if (currentClass != null) {
                Path p = new Path();
                PathTransition transition = new PathTransition();
                int[] start = new int[]{(int) (getCenterX() + getTranslateX()), (int) (getCenterY() + getTranslateY())};
                int[] end = Database.getRandRoom("ENTRANCE").randSpotInside();
                p.getElements().add(new MoveTo(start[0], start[1]));
                if (!currentClass.isUpstairs())
                    p.getElements().add(new LineTo(end[0], end[1]));
                else {
                    Staircase nearestStaircase = Database.getNearestStaircase(currentClass);
                    int[] bottom = Room.randSpotInside(nearestStaircase.getBottomX(), nearestStaircase.getBottomY(), Data.stairRadius);
                    int[] top = Room.randSpotInside(nearestStaircase.getTopX(), nearestStaircase.getTopY(), Data.stairRadius);
                    p.getElements().add(new LineTo(top[0], top[1]));
                    p.getElements().add(new LineTo(bottom[0], bottom[1]));
                    p.getElements().add(new LineTo(end[0], end[1]));
                }
                double time = Database.distBtwn(start[0], start[1], end[0], end[1]) * Data.avgTime / (500. * walkingSpeed);
                currentAnimation = transition;
                transition.setPath(p);
                transition.setNode(this);
                transition.setDuration(Duration.seconds(time));
                transition.setOnFinished(event -> {
                    currentAnimation = null;
                    currentClass = nextClass;
                    setTranslateX(-1 - getCenterX());
                    setTranslateY(-1 - getCenterY());

                });
                transition.play();
            }
        } else {
            Path p = new Path();
            PathTransition transition = new PathTransition();
            int[] start, end;
            if (currentClass == null) {
                start = Database.getRandRoom("ENTRANCE").randSpotInside();
                end = nextClass.randSpotInside();
                p.getElements().add(new MoveTo(start[0], start[1]));
                if (!nextClass.isUpstairs()) {
                    p.getElements().add(new LineTo(end[0], end[1]));
                } else {
                    Staircase nearestStaircase = Database.getNearestStaircase(nextClass);
                    int[] bottom = Room.randSpotInside(nearestStaircase.getBottomX(), nearestStaircase.getBottomY(), Data.stairRadius);
                    int[] top = Room.randSpotInside(nearestStaircase.getTopX(), nearestStaircase.getTopY(), Data.stairRadius);
                    p.getElements().add(new LineTo(bottom[0], bottom[1]));
                    p.getElements().add(new LineTo(top[0], top[1]));
                    p.getElements().add(new LineTo(end[0], end[1]));
                }
            } else {
                start = new int[]{(int) (getCenterX() + getTranslateX()), (int) (getCenterY() + getTranslateY())};
                end = nextClass.randSpotInside();
                p.getElements().add(new MoveTo(start[0], start[1]));
                if (currentClass.getBuilding().equals(nextClass.getBuilding())) {
                    if (currentClass.isUpstairs() == nextClass.isUpstairs()) {
                    } else if (currentClass.isUpstairs() && !nextClass.isUpstairs()) {
                        Staircase nearestStaircse = Database.getNearestStaircase(currentClass);
                        int[] top = Room.randSpotInside(nearestStaircse.getTopX(),nearestStaircse.getTopY(),Data.stairRadius);
                        int[] bottom = Room.randSpotInside(nearestStaircse.getBottomX(),nearestStaircse.getBottomY(),Data.stairRadius);
                        p.getElements().add(new LineTo(top[0],top[1]));
                        p.getElements().add(new LineTo(bottom[0],bottom[1]));
                    } else if (!currentClass.isUpstairs() && nextClass.isUpstairs()) {
                        Staircase nearestStaircse = Database.getNearestStaircase(nextClass);
                        int[] top = Room.randSpotInside(nearestStaircse.getTopX(),nearestStaircse.getTopY(),Data.stairRadius);
                        int[] bottom = Room.randSpotInside(nearestStaircse.getBottomX(),nearestStaircse.getBottomY(),Data.stairRadius);
                        p.getElements().add(new LineTo(bottom[0],bottom[1]));
                        p.getElements().add(new LineTo(top[0],top[1]));
                    } else
                        System.out.println("???");
                } else {
                    if (!currentClass.isUpstairs() && !nextClass.isUpstairs()) {
                    } else if (currentClass.isUpstairs() && !nextClass.isUpstairs()) {
                        Staircase nearestStaircase = Database.getNearestStaircase(currentClass);
                        int[] top = Room.randSpotInside(nearestStaircase.getTopX(),nearestStaircase.getTopY(),Data.stairRadius);
                        int[] bottom = Room.randSpotInside(nearestStaircase.getBottomX(),nearestStaircase.getBottomY(),Data.stairRadius);
                        p.getElements().add(new LineTo(top[0],top[1]));
                        p.getElements().add(new LineTo(bottom[0],bottom[1]));
                    } else if (!currentClass.isUpstairs() && nextClass.isUpstairs()) {
                        Staircase nearestStaircase = Database.getNearestStaircase(nextClass);
                        int[] top = Room.randSpotInside(nearestStaircase.getTopX(),nearestStaircase.getTopY(),Data.stairRadius);
                        int[] bottom = Room.randSpotInside(nearestStaircase.getBottomX(),nearestStaircase.getBottomY(),Data.stairRadius);
                        p.getElements().add(new LineTo(bottom[0],bottom[1]));
                        p.getElements().add(new LineTo(top[0],top[1]));
                    } else if (currentClass.isUpstairs() && nextClass.isUpstairs()) {
                        Staircase nearestStaircase = Database.getNearestStaircase(currentClass);
                        int[] top1 = Room.randSpotInside(nearestStaircase.getTopX(),nearestStaircase.getTopY(),Data.stairRadius);
                        int[] bottom1 = Room.randSpotInside(nearestStaircase.getBottomX(),nearestStaircase.getBottomY(),Data.stairRadius);
                        p.getElements().add(new LineTo(top1[0],top1[1]));
                        p.getElements().add(new LineTo(bottom1[0],bottom1[1]));
                        Staircase stair2 = Database.getNearestStaircase(nextClass);
                        int[] top2 = Room.randSpotInside(stair2.getTopX(),stair2.getTopY(),Data.stairRadius);
                        int[] bot2 = Room.randSpotInside(stair2.getBottomX(),stair2.getBottomY(),Data.stairRadius);
                        p.getElements().add(new LineTo(bot2[0],bot2[1]));
                        p.getElements().add(new LineTo(top2[0],top2[1]));
                    } else
                        System.out.println("???");
                }
                p.getElements().add(new LineTo(end[0], end[1]));
            }
            double time = Database.distBtwn(start[0],start[1],end[0],end[1])*Data.avgTime /(500.*walkingSpeed);
            time += ((Data.avgTime - time)*Math.abs(Data.avgTime - time)/Data.avgTime);
            currentAnimation = transition;
            transition.setPath(p);
            transition.setNode(this);
            transition.setDuration(Duration.seconds(time));
            transition.setOnFinished(event -> {
                currentAnimation = null;
                currentClass = nextClass;
            });
            transition.play();
        }
    }
    public void setGenderColorEnabled(boolean a) {
        if (!hasColorsOn && a) {
            hasColorsOn = true;
            setFill(isMale ? Color.BLUE : Color.RED);
        } else if (hasColorsOn && !a) {
            hasColorsOn = false;
            setFill(Color.BLACK);
        }
    }
    public boolean isMoving() {
        return currentAnimation != null;
    }
    public Transition getCurrentAnimation() {
        return currentAnimation;
    }
    public Room getCurrentClass() {
        return currentClass;
    }
    public int getCurrentPeriod() {
        return currentPeriod;
    }
}
