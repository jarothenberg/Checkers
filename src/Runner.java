package blah;

public class Runner {
	public static void main(String[] args) {
		Gameboard b = new Gameboard();
		b.intro();
		System.out.println(b.toString() + "\n");
		while (!b.isDone()) {
		b.play();
		}
	}
}
