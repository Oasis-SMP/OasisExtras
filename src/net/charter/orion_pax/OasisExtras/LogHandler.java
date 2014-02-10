package net.charter.orion_pax.OasisExtras;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.bukkit.entity.Player;

public class LogHandler extends Handler {
	private final OasisExtras plugin;

	public LogHandler(final OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public void close() throws SecurityException {}

	@Override
	public void flush() {}

	@Override
	public void publish(final LogRecord record) {
		plugin.telnet.sendMsg(record.getMessage());
	}
}
