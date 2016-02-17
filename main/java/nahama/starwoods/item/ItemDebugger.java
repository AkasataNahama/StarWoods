package nahama.starwoods.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemDebugger extends Item {

	/** ブロックに向かって右クリックした時の処理。 */
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		int meta = world.getBlockMetadata(x, y, z);
		player.addChatMessage(new ChatComponentText(new ItemStack(world.getBlock(x, y, z), 1, meta).getDisplayName() + " : " + meta));
		return true;
	}

}
