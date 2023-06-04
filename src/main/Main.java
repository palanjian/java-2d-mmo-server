package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Main {
	
	static int port = 4000;
	
	public static void main(String[] args) {
		Server server = new Server(port);
	}
}
