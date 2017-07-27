package me.kg.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Tools
{
	public static final ItemStack setItem(ItemStack item, String title, List<String> lore)
	{
		ItemMeta itemMeta = item.getItemMeta();
		if (title != null)
			itemMeta.setDisplayName(title);
		if (lore != null)
			itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);

		return item;
	}

	public static final ItemStack addItemLore(ItemStack item, String... str)
	{
		ItemMeta itemMeta = item.getItemMeta();

		List<String> lore;
		if (itemMeta.hasLore())
			lore = itemMeta.getLore();
		else
			lore = new ArrayList<>();

		lore.addAll(Arrays.asList(str));
		itemMeta.setLore(lore);

		item.setItemMeta(itemMeta);

		return item;
	}

	public static String toInfo(ItemStack stack)
	{
		return stack.getAmount() + " X " + (stack.getItemMeta().hasDisplayName() ? stack.getItemMeta().getDisplayName() : stack.getType().toString());
	}

	//	public static ItemStack addGlow(ItemStack item)
	//	{
	//		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
	//		NBTTagCompound tag = null;
	//		if (!nmsStack.hasTag())
	//		{
	//			tag = new NBTTagCompound();
	//			nmsStack.setTag(tag);
	//		}
	//		if (tag == null)
	//			tag = nmsStack.getTag();
	//		NBTTagList ench = new NBTTagList();
	//		tag.set("ench", ench);
	//		nmsStack.setTag(tag);
	//		return CraftItemStack.asCraftMirror(nmsStack);
	//	}
}
