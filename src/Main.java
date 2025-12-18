import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

public class Main extends Application {

    private double angle = 0;
    private double moonAngle = 0;
    private double speed = 1.0;
    private double timeScale = 1.0;
    private boolean paused = false;

    private Label infoCard;

    @Override
    public void start(Stage stage) {

        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        double cx = 400;
        double cy = 300;

        /* ================= TIME SCALE SELECTOR ================= */
        ComboBox<String> timeSelector = new ComboBox<>();
        timeSelector.getItems().addAll(
                "1 Day / sec",
                "1 Month / sec",
                "1 Year / sec"
        );
        timeSelector.setValue("1 Day / sec");
        timeSelector.setLayoutX(20);
        timeSelector.setLayoutY(460);

        timeSelector.setOnAction(e -> {
            switch (timeSelector.getValue()) {
                case "1 Month / sec" -> timeScale = 5;
                case "1 Year / sec"  -> timeScale = 20;
                default -> timeScale = 1;
            }
        });

        Label timeLabel = new Label("Time Scale");
        timeLabel.setTextFill(Color.WHITE);
        timeLabel.setLayoutX(20);
        timeLabel.setLayoutY(440);

        /* ================= SPEED SLIDER ================= */
        Slider speedSlider = new Slider(0.1, 5, 1);
        speedSlider.setLayoutX(20);
        speedSlider.setLayoutY(520);
        speedSlider.setPrefWidth(200);

        Label speedLabel = new Label("Speed Control");
        speedLabel.setTextFill(Color.WHITE);
        speedLabel.setLayoutX(20);
        speedLabel.setLayoutY(500);

        speedSlider.valueProperty().addListener((o, a, b) ->
                speed = b.doubleValue()
        );

        /* ================= PAUSE / RESUME ================= */
        Button pauseBtn = new Button("Pause");
        pauseBtn.setLayoutX(240);
        pauseBtn.setLayoutY(520);
        pauseBtn.setOnAction(e -> {
            paused = !paused;
            pauseBtn.setText(paused ? "Resume" : "Pause");
        });

        /* ================= INFO SIDE CARD ================= */
        infoCard = new Label("""
        PLANET INFO

        Click a planet
        """);

        infoCard.setTextFill(Color.WHITE);

        /* 🔹 TOP-RIGHT POSITION */
        infoCard.setLayoutX(560);
        infoCard.setLayoutY(20);

        /* 🔹 BIGGER PANEL */
        infoCard.setPrefWidth(220);
        infoCard.setPrefHeight(180);

        infoCard.setStyle("""
        -fx-background-color: #1c1c1c;
        -fx-padding: 15;
        -fx-border-color: gray;
        -fx-font-size: 13px;
        -fx-border-radius: 5;
        -fx-background-radius: 5;
        """);


        /* ================= SUN ================= */
        Circle sun = new Circle(cx, cy, 30, Color.ORANGE);

        /* ================= ELLIPTICAL ORBITS ================= */
        Ellipse oEarth = orbit(cx, cy, 130, 110);
        Ellipse oMars  = orbit(cx, cy, 170, 145);
        Ellipse oJup   = orbit(cx, cy, 210, 185);
        Ellipse oSat   = orbit(cx, cy, 250, 215);
        Ellipse oUra   = orbit(cx, cy, 290, 245);
        Ellipse oNep   = orbit(cx, cy, 330, 275);
        Ellipse oMer   = orbit(cx, cy, 60, 50);
        Ellipse oVen   = orbit(cx, cy, 90, 75);

        /* ================= PLANETS ================= */
        Circle mercury = planet(5, Color.GRAY,
                "Mercury\nClosest to Sun\nNo moons");

        Circle venus = planet(8, Color.BEIGE,
                "Venus\nHottest planet\nNo moons");

        Circle earth = planet(10, Color.DODGERBLUE,
                "Earth\nSupports life\nMoons: 1");

        Circle moon = planet(4, Color.LIGHTGRAY,
                "Moon\nEarth's satellite");

        Circle mars = planet(7, Color.RED,
                "Mars\nRed Planet\nMoons: 2");

        Circle jupiter = planet(16, Color.SANDYBROWN,
                "Jupiter\nLargest planet\nMoons: 79");

        Circle saturn = planet(12, Color.BURLYWOOD,
                "Saturn\nFamous rings\nMoons: 83");

        Circle uranus = planet(10, Color.LIGHTBLUE,
                "Uranus\nSideways rotation");

        Circle neptune = planet(10, Color.DARKBLUE,
                "Neptune\nFastest winds");

        /* ================= SATURN RINGS ================= */
        Ellipse ringOuter = new Ellipse(22, 10);
        ringOuter.setStroke(Color.LIGHTGRAY);
        ringOuter.setFill(Color.TRANSPARENT);

        Ellipse ringInner = new Ellipse(18, 8);
        ringInner.setStroke(Color.GRAY);
        ringInner.setFill(Color.TRANSPARENT);

        root.getChildren().addAll(
                oMer, oVen, oEarth, oMars, oJup, oSat, oUra, oNep,
                sun,
                mercury, venus, earth, moon, mars,
                jupiter, ringOuter, ringInner, saturn,
                uranus, neptune,
                infoCard,
                timeLabel, timeSelector,
                speedLabel, speedSlider,
                pauseBtn
        );

        /* ================= ANIMATION ================= */
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (paused) return;

                angle += speed * timeScale;
                moonAngle += 3 * speed * timeScale;

                setOnEllipse(mercury, cx, cy, 60, 50, angle * 1.6);
                setOnEllipse(venus,   cx, cy, 90, 75, angle * 1.3);
                setOnEllipse(earth,   cx, cy, 130,110, angle);

                moon.setCenterX(earth.getCenterX() + 25 * Math.cos(Math.toRadians(moonAngle)));
                moon.setCenterY(earth.getCenterY() + 25 * Math.sin(Math.toRadians(moonAngle)));

                setOnEllipse(mars,    cx, cy, 170,145, angle * 0.8);
                setOnEllipse(jupiter, cx, cy, 210,185, angle * 0.5);
                setOnEllipse(saturn,  cx, cy, 250,215, angle * 0.4);

                ringOuter.setCenterX(saturn.getCenterX());
                ringOuter.setCenterY(saturn.getCenterY());
                ringInner.setCenterX(saturn.getCenterX());
                ringInner.setCenterY(saturn.getCenterY());

                setOnEllipse(uranus,  cx, cy, 290,245, angle * 0.3);
                setOnEllipse(neptune, cx, cy, 330,275, angle * 0.25);
            }
        };

        timer.start();

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Solar System Explorer - Final Version");
        stage.setScene(scene);
        stage.show();
    }

    /* ================= HELPERS ================= */

    private Circle planet(double r, Color c, String info) {
        Circle p = new Circle(r, c);
        p.setOnMouseClicked(e -> infoCard.setText("PLANET INFO\n" + info));
        return p;
    }

    private Ellipse orbit(double cx, double cy, double rx, double ry) {
        Ellipse e = new Ellipse(cx, cy, rx, ry);
        e.setStroke(Color.DARKGRAY);
        e.setFill(Color.TRANSPARENT);
        e.getStrokeDashArray().addAll(4.0, 6.0);
        return e;
    }

    private void setOnEllipse(Circle p, double cx, double cy,
                              double rx, double ry, double a) {
        p.setCenterX(cx + rx * Math.cos(Math.toRadians(a)));
        p.setCenterY(cy + ry * Math.sin(Math.toRadians(a)));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
