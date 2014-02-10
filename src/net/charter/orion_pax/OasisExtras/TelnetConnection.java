package net.charter.orion_pax.OasisExtras;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.bukkit.ChatColor;

public class TelnetConnection implements Runnable{

	private OasisExtras plugin;
	private Socket incoming = null;
	private InputStream inps;
	private OutputStream outs;
	private Scanner in;
	public PrintStream out;

	final PrintStream oldOut;
	public TelnetConnection(OasisExtras plugin, Socket incoming,PrintStream oldOut) {
		this.plugin = plugin;
		this.incoming = incoming;
		this.oldOut = oldOut;
	}
	@Override
	public void run() {
		while (true) {
			try {

				inps = incoming.getInputStream();
				outs = incoming.getOutputStream();

				in = new Scanner(inps);
				out = new PrintStream(outs, true);

				out.println("Server running...");

				for(TelnetConnection client:plugin.threads){
					client.redirectSystemStreams();
				}

				boolean done = false;
				while (!done && in.hasNextLine()) {

					// read data from Socket
					String line = in.nextLine();

					Util.bCast(line);
					if(line.startsWith("/")  && !line.equals("/exit") && !line.startsWith("/a ")){
						plugin.getServer().dispatchCommand(plugin.console, line.substring(1));
					} else if(line.startsWith("/a ")){
						plugin.getServer().broadcast(ChatColor.translateAlternateColorCodes('&', "&b{&4OasisNet&b} " + line.substring(3)), "oasischat.staff.a");
					} else {
						Util.bCast("&7[&4OasisNet&7]&1 " + line);
					}
					if (line.equalsIgnoreCase("/exit")) {
						done = true;
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				incoming.close();
				incoming = null;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	
	public void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(final int b) throws IOException {
				for(TelnetConnection client:plugin.threads){
					client.outs.write(b);
				}
				oldOut.write(b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				for(TelnetConnection client:plugin.threads){
					client.outs.write(b, off, len);
				}
				oldOut.write(b, off, len);
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};
		
		System.setOut(new PrintStream(out));
	}

}
