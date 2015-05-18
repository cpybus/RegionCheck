package me.chris.RegionCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/**
 * @author Chris
 * 
 */
public class RegionCheckCommandHandler implements CommandExecutor
{

	public boolean onCommand(CommandSender sender, Command cmd, String idk, String[] args)
	{
		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("regioncheck") || cmd.getName().equalsIgnoreCase("rgc"))
		{
			if (args.length == 0)
			{
				p.sendMessage("§5=====================================================");
				p.sendMessage("§a Welcome to §cRegionCheck §aPlugin §9(" + Vars.version + ")");
				p.sendMessage("§a Designed and Programmed by §9Hotshot2162");
				p.sendMessage("§5=====================================================");
				return true;
			}
			else if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("regions"))
				{
					if (p.isOp())
					{
						WorldGuardPlugin worldGuard = Vars.plugin.getWorldGuard();

						HashMap<String, Integer> counter = new HashMap<String, Integer>();

						RegionManager regionManager = worldGuard.getRegionManager(p.getLocation().getWorld());
						Map<String, ProtectedRegion> regions = regionManager.getRegions();

						for (Entry<String, ProtectedRegion> entry : regions.entrySet())
						{
							ProtectedRegion rg = entry.getValue();
							DefaultDomain ownersDefaultDomain = rg.getOwners();
							Set<String> owners = ownersDefaultDomain.getPlayers();
							for (String owner : owners)
							{
								if (counter.containsKey(owner))
								{
									int value = counter.get(owner) + 1;
									counter.put(owner, value);
								}
								else
								{
									counter.put(owner, 1);
								}
							}
						}

						p.sendMessage("§5=====================================================");
						p.sendMessage("§aThe following have more than 4 plots under their name:");
						for (Entry<String, Integer> entry : counter.entrySet())
						{
							if (entry.getValue() > 4)
							{
								p.sendMessage("§e " + entry.getKey() + " §2(" + entry.getValue() + ")");
							}
						}
						p.sendMessage("§5=====================================================");
					}
					else
					{
						p.sendMessage("§a[RegionCheck]§4 You do not have permission to check region listings");
					}

				}
				else
				{
					String name = args[0];
					if (p.isOp() || p.getName().equalsIgnoreCase(name))
					{

						WorldGuardPlugin worldGuard = Vars.plugin.getWorldGuard();

						ArrayList<ProtectedRegion> regions = new ArrayList<ProtectedRegion>();

						RegionManager regionManager = worldGuard.getRegionManager(p.getLocation().getWorld());
						Map<String, ProtectedRegion> allRegions = regionManager.getRegions();

						for (Entry<String, ProtectedRegion> entry : allRegions.entrySet())
						{
							ProtectedRegion rg = entry.getValue();
							DefaultDomain ownersDefaultDomain = rg.getOwners();
							Set<String> owners = ownersDefaultDomain.getPlayers();
							for (String owner : owners)
							{
								if (owner.equalsIgnoreCase(name))
								{
									regions.add(rg);
								}

							}
						}

						p.sendMessage("§5=====================================================");
						p.sendMessage("§c" + name + " §ais listed as owner on the following regions:");
						for (ProtectedRegion rg : regions)
						{
							p.sendMessage("§c - §e" + rg.getId());
						}
						p.sendMessage("§5=====================================================");
					}
					else
					{
						p.sendMessage("§a[RegionCheck]§4 You can only check your own region status");
					}
				}
			}
			else if (args.length == 2)
			{
				if(args[0].equalsIgnoreCase("tp"))
				{
					String regionName = args[1];
					WorldGuardPlugin worldGuard = Vars.plugin.getWorldGuard();
					
					ArrayList<ProtectedRegion> regions = new ArrayList<ProtectedRegion>();

					RegionManager regionManager = worldGuard.getRegionManager(p.getLocation().getWorld());
					Map<String, ProtectedRegion> allRegions = regionManager.getRegions();

					for (Entry<String, ProtectedRegion> entry : allRegions.entrySet())
					{
						ProtectedRegion rg = entry.getValue();
						if(rg.getId().equalsIgnoreCase(regionName))
						{
							BlockVector maxPoint = rg.getMaximumPoint();
							BlockVector minPoint = rg.getMinimumPoint();
							double avgX = (maxPoint.getX()+minPoint.getX())/2;
							double avgY = (maxPoint.getY()+minPoint.getY())/2;
							double avgZ = (maxPoint.getZ()+minPoint.getZ())/2;
							
							Location plotLoc = new Location(p.getWorld(), avgX, avgY, avgZ);
							p.teleport(plotLoc);
							return true;
						}
					}
					
					p.sendMessage("§a[RegionCheck]§4 Could not find region");
				}
				else
				{
					p.sendMessage("§a[RegionCheck]§4 Invalid command");
				}
			}

		}
		return true;
	}
}
