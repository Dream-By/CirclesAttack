package by.dream.rgbcircles;

import java.util.ArrayList;

public class GameManager {
    public static final int MAX_CIRCLES = 10;
    private MainCirlce mainCirlce;
    private ArrayList<EnemyCircle> circles;
    private CanvasView canvasView;
    private static int width;
    private static int height;


    public GameManager(CanvasView canvasView, int w, int h) {
        this.canvasView = canvasView;
        width = w;
        height = h;
        initMainCircle();
        initEnemyCircles();
    }

    private void initEnemyCircles() {
        SimpleCircle mainCircleArea = mainCirlce.getCircleArea();
        circles = new ArrayList<EnemyCircle>();
        for (int i=0; i<MAX_CIRCLES; i++) {
            EnemyCircle circle;
            do {
                circle = EnemyCircle.getRandomCircle();
            } while (circle.isIntersect(mainCircleArea));
            circles.add(circle);
            calculateAndSetCirclesColor();
        }
    }

    private void calculateAndSetCirclesColor() {
        for (EnemyCircle circle:circles){
            circle.setEnemyOrFoodColorDependsOn(mainCirlce);
        }
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    private void initMainCircle() {
        mainCirlce = new MainCirlce(width / 2, height / 2);
    }


    public void onDraw() {

        canvasView.drawCircle(mainCirlce);
        for (EnemyCircle circle:circles) {
            canvasView.drawCircle(circle);
        }
        }

    public void onTouchEvent(int x, int y) {

        mainCirlce.moveMainCircleWhenTouchAt(x,y);
        moveCircles();
        checkCollision();
    }

    private void checkCollision() {
        SimpleCircle circleForDel = null;
        for (EnemyCircle circle:circles) {
            if (mainCirlce.isIntersect(circle)) {
                if (circle.isSmallerThan(mainCirlce)) {
                    mainCirlce.growRadius(circle);
                    circleForDel = circle;
                    calculateAndSetCirclesColor();
                    break;
                } else {
                    gameEnd("YOU LOSE!");
                    return;
                }
            }
        }
            if (circleForDel != null){
                circles.remove(circleForDel);
            }
            if (circles.isEmpty()) {
                gameEnd("YOU WIN!");
            }
        }



    private void gameEnd(String s) {
        canvasView.showMessage(s);
        mainCirlce.initRadius();
        initEnemyCircles();
        canvasView.redraw();
    }

    private void moveCircles() {
        for (EnemyCircle circle:circles) {
            circle.moveOneStep();
        }
    }
}
