package secom.server.main;
import static spark.Spark.*;

public class MainTestCertificado {
	public static void main(String[] args) {
		secure("C:\\Users\\C�sar\\Documents\\certs\\server.jks", "/852789@", null, null);
		get("/hello", (req, res) -> "Hello World");
	}
}
