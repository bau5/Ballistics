package com.bau5.ballistics

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{EnumFacing, BlockPos, ChatComponentText}
import net.minecraft.world.World
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry

import com.bau5.lib.RichNBTTagCompound._

@Mod(modid = "ballistics", name = "Ballistics", version = "v0.0", modLanguage = "scala")
object Ballistics {

  val launcher = new BlockLauncher().setCreativeTab(CreativeTabs.tabMisc)

  val targetPainter = new ItemPainter()

  @EventHandler
  def init(ev: FMLInitializationEvent) = {
    GameRegistry.registerTileEntity(classOf[TileEntityLauncher], "tile_launcher_base")
    GameRegistry.registerBlock(Ballistics.launcher, "block_launcher_base")
    GameRegistry.registerItem(targetPainter, "painter")
  }
}