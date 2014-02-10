package net.charter.orion_pax.OasisExtras;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.ChatColor;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;

public class TelnetServer {
	private OasisExtras plugin;
	private InputStream inps;
	private OutputStream outs;
	private Scanner in;
	private PrintStream out;
	public static ServerSocket s;

	public static Socket incoming = null;

	final PrintStream oldOut = System.out;
	
	public List<Thread> threads = new ArrayList<Thread>();

	private boolean chat=false;
	
	private TelnetConnection client;
	
	private static final Map<ChatColor, String> ansicolors = new EnumMap<ChatColor, String>(ChatColor.class);
	private static final ChatColor[] colors = ChatColor.values();

	public TelnetServer(final OasisExtras plugin){

		this.plugin=plugin;
		this.setupmap();
		try {
			s = new ServerSocket(4444);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable(){

			@Override
			public void run() {
				while (true) {
					try {
						incoming = s.accept();
						Thread thread = new Thread(client = new TelnetConnection(plugin,incoming,oldOut));
						thread.start();
						plugin.threads.add(client);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				}

			}

		});
	}
	
	private void setupmap(){
		ansicolors.put(ChatColor.BLACK, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
		ansicolors.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
		ansicolors.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
		ansicolors.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
		ansicolors.put(ChatColor.DARK_RED, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
		ansicolors.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
		ansicolors.put(ChatColor.GOLD, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
		ansicolors.put(ChatColor.GRAY, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
		ansicolors.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
		ansicolors.put(ChatColor.BLUE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
		ansicolors.put(ChatColor.GREEN, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
		ansicolors.put(ChatColor.AQUA, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
		ansicolors.put(ChatColor.RED, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
		ansicolors.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
		ansicolors.put(ChatColor.YELLOW, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
		ansicolors.put(ChatColor.WHITE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
		ansicolors.put(ChatColor.MAGIC, Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
		ansicolors.put(ChatColor.BOLD, Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString());
		ansicolors.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
		ansicolors.put(ChatColor.UNDERLINE, Ansi.ansi().a(Attribute.UNDERLINE).toString());
		ansicolors.put(ChatColor.ITALIC, Ansi.ansi().a(Attribute.ITALIC).toString());
		ansicolors.put(ChatColor.RESET, Ansi.ansi().a(Attribute.RESET).toString());
	}
	
	public static String colorize(String msg) {

		for (ChatColor c : colors) {
			if (!ansicolors.containsKey(c)) {
				msg = msg.replaceAll(c.toString(), "");
			} else {
				msg = msg.replaceAll(c.toString(), ansicolors.get(c));
			}
		}
		return msg;
	}

	public void sendMsg(String msg){
		for(TelnetConnection client: plugin.threads){
			if(client.out!=null){
				client.out.println(colorize(ChatColor.translateAlternateColorCodes('&', msg)));
			}
		}
	}
}