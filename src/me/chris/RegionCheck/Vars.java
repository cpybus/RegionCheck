/**
 * 
 */
package me.chris.RegionCheck;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Christopher Pybus
 * @date Mar 25, 2012
 * @file SimpleChatVariables.java
 * @package me.chris.SimpleChat
 * 
 * @purpose
 */

public class Vars
{
	public static FileConfiguration config;
	public static FileConfiguration extra;
	public static Permission perms;
	public static Economy eco;
	public static Logger log;
	public static RegionCheckMain plugin;

	public static File configFile;
	public static File extraFile;

	public static final String version = "PluginTemplate 1.0";

	public Vars(RegionCheckMain plugin)
	{
		Vars.plugin = plugin;
		log = Logger.getLogger("Minecraft");

		configFile = new File(plugin.getDataFolder(), "config.yml");
		extraFile = new File(plugin.getDataFolder(), "extra.yml");

		config = new YamlConfiguration();
		extra = new YamlConfiguration();
	}

	public static void importVariables()
	{

	}

	public static void exportVariables()
	{

	}
}
