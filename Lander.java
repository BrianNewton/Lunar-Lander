import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Lander {

    public double y;
    public double x;
    public double vy;
    public double vx;

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getVy() {
        return vy;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVx() {
        return vx;
    }

    Polygon lander = new Polygon();

    Lander(){
        //lander = new Rectangle(x,y,42,47);
        lander.getPoints().addAll(new Double[]{
            479.,10.,
            521.,10.,
            521.,57.,
            479.,57.,
        });
    }
}
