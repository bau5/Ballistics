package com.bau5.ballistics

import com.bau5.lib.RichNBTTagCompound._
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.World

/**
 * Created by bau5 on 9/24/15.
 */
class BlockLauncher extends BlockContainer(Material.iron) {

  override def onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer,
     side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {

    Option(playerIn.getHeldItem) match {
      case Some(is) if is.getItem.equals(Ballistics.targetPainter) =>
        if (!is.hasTagCompound) {
          is.setTagCompound(new NBTTagCompound)
        }
        is.getTagCompound.writeBlockPos("launcher", pos)
      case _ => ;
    }

    super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ)
  }

  override def createNewTileEntity(worldIn: World, meta: Int): TileEntity = new TileEntityLauncher()
}
