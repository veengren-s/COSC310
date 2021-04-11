import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("localhost", 55690);
		DataOutputStream out = new DataOutputStream(s.getOutputStream());
		DataInputStream in = new DataInputStream(s.getInputStream());
		Scanner scan = new Scanner(System.in);
		String str = "";
		while(!str.equals("q")) {
		str = scan.nextLine();
		out.writeUTF(str);
		System.out.println(in.readUTF());
		}
	}

}
