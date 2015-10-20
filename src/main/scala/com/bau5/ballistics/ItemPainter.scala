package com.bau5.ballistics

import com.bau5.lib.RichNBTTagCompound._
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{BlockPos, ChatComponentText, EnumFacing}
import net.minecraft.world.World

/**
 * Created by bau5 on 9/25/15.
 */
class ItemPainter extends Item {
  setCreativeTab(CreativeTabs.tabMisc)

  override def onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer): ItemStack = {
    if (!worldIn.isRemote) {
      Option(itemStackIn.getTagCompound) match {
        case Some(tag) if tag.hasKey("launcher") =>
          Option(worldIn.getTileEntity(tag.readBlockPos("launcher").get)) match {
            case Some(launcher) if launcher.isInstanceOf[TileEntityLauncher] =>
            case _ =>
              println("Not linked to a valid launcher.")
              tag.removeTag("launcher")
          }
        case _ =>
          playerIn.addChatComponentMessage(new ChatComponentText("Not linked to a launcher."))
          if (!itemStackIn.hasTagCompound) {
            itemStackIn.setTagCompound(new NBTTagCompound)
          }
      }
    }
    super.onItemRightClick(itemStackIn, worldIn, playerIn)
  }

  override def onItemUse(stack: ItemStack, playerIn: EntityPlayer, worldIn: World, pos: BlockPos, side: EnumFacing,
                         hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (!stack.hasTagCompound) {
      stack.setTagCompound(new NBTTagCompound)
    }

    worldIn.getBlockState(pos).getBlock match {
      case l if l.isInstanceOf[BlockLauncher] =>
        val tile = worldIn.getTileEntity(pos)
        if (tile != null) {
          stack.getTagCompound.writeBlockPos("launcher", tile.getPos)
          playerIn.addChatComponentMessage(new ChatComponentText("Linked to launcher at " + pos))
        }
      case _ =>
        if (stack.getTagCompound.hasKey("launcher")) {
          val launcherPos = stack.getTagCompound.readBlockPos("launcher").get
          val tile = worldIn.getTileEntity(launcherPos)
          if (tile != null && tile.isInstanceOf[TileEntityLauncher]) {
            val launcher = tile.asInstanceOf[TileEntityLauncher]
            launcher.currentTarget_(pos)
            launcher.path_(Path(launcherPos, pos))
            playerIn.addChatComponentMessage(new ChatComponentText(s"Set target & path ${launcher.currentTarget.get}, ${launcher.path.get}"))
          }
        }
    }

    true
  }
}
