package nahama.starwoods.item;

import java.util.ArrayList;
import java.util.Random;

import nahama.starwoods.block.StarWoodsLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class PruningScissors extends Item {

	public static boolean isHarvest;

	public PruningScissors() {
		super();
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setFull3D();
		this.setHasSubtypes(false);
		this.setMaxStackSize(1);
	}

	/**ブロックを破壊した時の処理*/
	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
		//ブロックがこのMODで追加した葉なら、
		if (block instanceof StarWoodsLeaves) {
			//ダメージを与えて
			itemStack.damageItem(1, entity);
			//回収できるようにする
			isHarvest = true;
		} else {
			super.onBlockDestroyed(itemStack, world, block, x, y, z, entity);
		}
		return true;
	}

	/**適正ツールかどうか*/
	@Override
	public boolean func_150897_b(Block block) {
		return false;
	}

	/**採掘速度を返す*/
	@Override
	public float func_150893_a(ItemStack itemStack, Block block) {
		return block.getMaterial() == Material.leaves ? 15.0F : super.func_150893_a(itemStack, block);
	}

	/**ブロックを破壊する直前に呼ばれる。ハサミと同様*/
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player) {
		if (player.worldObj.isRemote || player.capabilities.isCreativeMode) {
			return false;
		}
		Block block = player.worldObj.getBlock(x, y, z);
		if (block instanceof IShearable) {
			IShearable target = (IShearable)block;
			if (target.isShearable(itemstack, player.worldObj, x, y, z)) {
				ArrayList<ItemStack> drops = target.onSheared(itemstack, player.worldObj, x, y, z,
						EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
				Random rand = new Random();

				for(ItemStack stack : drops) {
					float f = 0.7F;
					double d  = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
					double d1 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
					double d2 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
					EntityItem entityitem = new EntityItem(player.worldObj, (double)x + d, (double)y + d1, (double)z + d2, stack);
					entityitem.delayBeforeCanPickup = 10;
					player.worldObj.spawnEntityInWorld(entityitem);
				}

				itemstack.damageItem(1, player);
				player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
			}
		}
		return false;
	}

}
