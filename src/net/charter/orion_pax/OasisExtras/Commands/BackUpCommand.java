package net.charter.orion_pax.OasisExtras.Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.minecraft.util.org.apache.commons.io.FileUtils;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.io.Files;

public class BackUpCommand implements CommandExecutor {

	private OasisExtras plugin;
	public BackUpCommand(OasisExtras plugin) {
		this.plugin=plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("kbackup")) {
			plugin.closed = true;
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				player.kickPlayer("Server closed for maintenance!");
			}
		}
		plugin.getServer().savePlayers();
		for(World world:plugin.getServer().getWorlds()){
			world.save();
		}
		
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable(){

			@Override
			public void run() {
				String sourcedir = new File("").getAbsolutePath();
				String match = sourcedir.substring(sourcedir.lastIndexOf("\\")+1);
				String destinationdir = new File(sourcedir.replace(match, match + "_backup")).getAbsolutePath();
				Collection<File> myfiles = FileUtils.listFiles(new File(sourcedir), null, true);
				for(File file:myfiles){
					String myfile = file.getAbsolutePath().replace(sourcedir, destinationdir);
					String mydir = myfile.replace(myfile.substring(myfile.lastIndexOf("\\")),"");
					new File(mydir).mkdirs();
				}
				for(File file:myfiles){
					String newloc = file.getAbsolutePath().replace(sourcedir, destinationdir);
					File newfile = new File(newloc);
					//newfile.mkdirs();
					try {
						Files.copy(file, newfile);
					} catch (IOException e) {
						plugin.getLogger().info("Could not copy " + file.getAbsolutePath());
					}
				}
				
				plugin.getLogger().info("Backup complete!");
				try {
					myfiles = FileUtils.listFiles(new File(sourcedir + "_backup"), null, true);
					byte[] buffer = new byte[1024];
					FileOutputStream fos = new FileOutputStream(sourcedir + "_backup.zip");
					ZipOutputStream zos = new ZipOutputStream(fos);
					for(File file:myfiles){
						ZipEntry ze = new ZipEntry(file.getAbsolutePath());
						zos.putNextEntry(ze);
						FileInputStream in = new FileInputStream(file);
						int len;
						while((len = in.read(buffer))>0){
							zos.write(buffer, 0, len);
						}
						in.close();
					}
					zos.closeEntry();
					zos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					plugin.getLogger().info("File not found");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					plugin.getLogger().info("IO Exception");
				}
				
				plugin.getLogger().info("ZIP COMPLETE");
				plugin.closed=false;
			}
			
		});
		return true;
	}

}
