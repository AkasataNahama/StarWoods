package nahama.starwoods.item;

import java.util.ArrayList;
import java.util.Random;

import nahama.starwoods.StarWoodsCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraftforge.common.IShearable;

public class ItemPruningScissors extends Item {

	public ItemPruningScissors() {
		super();
		this.setCreativeTab(StarWoodsCore.tabStarWoodsItem);
		this.setFull3D();
		this.setHasSubtypes(false);
		this.setMaxStackSize(1);
	}

	/** 適正ツールか。 */
	@Override
	public boolean func_150897_b(Block block) {
		return false;
	}

	/** 採掘速度を返す。 */
	@Override
	public float func_150893_a(ItemStack itemStack, Block block) {
		return block.getMaterial() == Material.leaves ? 15.0F : super.func_150893_a(itemStack, block);
	}

	/** ブロックの破壊を始めた時の処理。 */
	@Override
	public boolean onBlockStartBreak(ItemStack itemStack, int x, int y, int z, EntityPlayer player) {
		// ハサミと同様。
		if (player.worldObj.isRemote || player.capabilities.isCreativeMode)
			return false;
		Block block = player.worldObj.getBlock(x, y, z);
		if (!(block instanceof IShearable))
			return false;
		IShearable target = (IShearable) block;
		if (!target.isShearable(itemStack, player.worldObj, x, y, z))
			return false;
		ArrayList<ItemStack> drops = target.onSheared(itemStack, player.worldObj, x, y, z,
				EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemStack));
		Random rand = new Random();
		for (ItemStack itemStack1 : drops) {
			float f = 0.7F;
			double dx = rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double dy = rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double dz = rand.nextFloat() * f + (1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(player.worldObj, x + dx, y + dy, z + dz, itemStack1);
			entityitem.delayBeforeCanPickup = 10;
			player.worldObj.spawnEntityInWorld(entityitem);
		}
		itemStack.damageItem(1, player);
		player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
		return false;
	}

}
