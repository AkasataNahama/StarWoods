package nahama.starwoods.item;

import nahama.starwoods.StarWoodsCore;
import nahama.starwoods.block.BlockStarWoodsLog;
import nahama.starwoods.core.StarWoodsItemCore;
import nahama.starwoods.manager.StarWoodsTreeManager;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTapper extends Item {

	protected int level;

	public ItemTapper(int level) {
		this.level = level;
		this.setCreativeTab(StarWoodsCore.tabStarWoodsItem);
		this.setFull3D();
		this.setHasSubtypes(false);
		this.setMaxStackSize(1);
	}

	/** ブロックに向かって右クリックした時の処理。 */
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		// 原木(自然)なら、確率で樹液をドロップする。
		Block block = world.getBlock(x, y, z);
		if (!(block instanceof BlockStarWoodsLog))
			return false;
		int meta = world.getBlockMetadata(x, y, z);
		if (meta < 12)
			return false;
		itemStack.damageItem(1, player);
		if (world.isRemote)
			return true;
		world.setBlockMetadataWithNotify(x, y, z, meta % StarWoodsTreeManager.LOG, 2);
		BlockStarWoodsLog log = (BlockStarWoodsLog) block;
		int num = log.getGeneralNum(meta);
		if (!StarWoodsTreeManager.isNumValid(num))
			return true;
		if (world.rand.nextInt(log.getChanceWithBonus((meta % StarWoodsTreeManager.LOG), level)) != 0)
			return true;
		EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(StarWoodsItemCore.sap, 1, num));
		world.spawnEntityInWorld(item);
		return true;
	}

}
