package me.chris.RegionCheck;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RegionCheckMain extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		new Vars(this);
    	
    	try
		{
			firstRun();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    	
    	loadYamls();
    	
    	if (!setupPermissions())
		{
			Vars.log.log(Level.SEVERE, "[RegionCheck] No Permission found! Disabling plugin!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		RegionCheckCommandHandler commandHandler = new RegionCheckCommandHandler();
		getCommand("regioncheck").setExecutor(commandHandler);
		getCommand("rgc").setExecutor(commandHandler);
				
		Vars.log.log(Level.INFO, "[RegionCheck] Version " + Vars.version.substring(10));
		Vars.log.log(Level.INFO, "[RegionCheck] Started successfully.");		
	}
	
	@Override
	public void onDisable()
	{
		Vars.log.log(Level.INFO, "[RegionCheck] Stopped.");	
	}
	
	private void firstRun() throws Exception
	{
		if (!Vars.configFile.exists())
		{
			Vars.log.log(Level.INFO, "[RegionCheck] No config.yml file found. Attempting to make one. ");
			Vars.configFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), Vars.configFile);
			Vars.log.log(Level.INFO, "[RegionCheck] File Made Successfully ");
		}
		else
		{
			Vars.log.log(Level.INFO, "[RegionCheck] Config Found. Using it.  ");
		}
		
		
		
	}
	
	private void copy(InputStream in, File file)
	{
		try
		{
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadYamls()
	{
		try
		{
			Vars.config.load(Vars.configFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public void saveYamls()
	{
		Vars.exportVariables();
		
		try
		{
			Vars.config.save(Vars.configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		
	}
	
	private Boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null)
		{
			Vars.perms = permissionProvider.getProvider();
		}
		return (Vars.perms != null);
	}	
	
	public WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
}
