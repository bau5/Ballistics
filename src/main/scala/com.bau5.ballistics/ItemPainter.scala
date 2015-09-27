package com.bau5.ballistics

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{EnumParticleTypes, EnumFacing, BlockPos, ChatComponentText}
import net.minecraft.world.World

import com.bau5.lib.RichNBTTagCompound._

/**
 * Created by bau5 on 9/25/15.
 */
class ItemPainter extends Item {
  setCreativeTab(CreativeTabs.tabMisc)

  override def onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer): ItemStack = {
    if (!worldIn.isRemote) {
      Option(itemStackIn.getTagCompound) match {
        case Some(tag) if tag.hasKey("launcher") =>
          playerIn.addChatComponentMessage(new ChatComponentText("Launcher coords: " + tag.readBlockPos("launcher")))
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

    val launcher = stack.getTagCompound.readBlockPos("launcher") match {
      case Some(tagPos) =>
        val te = worldIn.getTileEntity(tagPos)
        if (te != null && te.isInstanceOf[TileEntityLauncher]) {
          playerIn.getHeldItem.getTagCompound.writeBlockPos("launcher", te.getPos)
          playerIn.addChatComponentMessage(new ChatComponentText("Bound to launcher at " + te.getPos))
          Option(te.asInstanceOf[TileEntityLauncher])
        } else {
          None
        }
      case _ =>
        None
    }

    launcher.exists{ case tile =>
      tile.currentTarget_(Target(pos))
      tile.path_(Path(tile.getPos, pos))
      val path = tile.path.get
      for (i <- 0 until path.totalDistance.toInt) {
        val vec = path.pointAtT(i / path.totalDistance)
        println(vec)
      }
      true
    }
  }
}
