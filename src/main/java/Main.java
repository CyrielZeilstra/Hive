import core.components.Model;
import core.controllers.Controller;

public class Main {
    public static void main(String[] args) {
        Controller main = new Controller(new Model());
        main.init();
        main.newGame();
    }
}
