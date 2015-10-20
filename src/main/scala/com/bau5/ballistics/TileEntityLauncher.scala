package com.bau5.ballistics

import net.minecraft.server.gui.IUpdatePlayerListBox
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumParticleTypes

class TileEntityLauncher extends TileEntity with IUpdatePlayerListBox {
  private[this] var _currentTarget: Option[RichVector] = None
  private[this] var _path: Option[Path] = None

  var missile: EntityMissile = null

  override def update(): Unit = {
    if (missile == null) {
      missile = new EntityMissile(worldObj)
      missile.setPosition(pos.getX + 0.5, pos.getY + 0.5, pos.getZ + 0.5)
      worldObj.spawnEntityInWorld(missile)
    }
    if (!missile.isEntityAlive) {
      missile = null
    }
    if (worldObj.isRemote) {
      _path.foreach { case path =>
        path.asList.foreach { pos =>
          worldObj.spawnParticle(EnumParticleTypes.PORTAL, pos.xCoord + 0.5, pos.yCoord + 0.5, pos.zCoord + 0.5, 0, 0, 0)
        }
      }
    }
  }

  def currentTarget: Option[RichVector] = _currentTarget

  def currentTarget_(value: RichVector) = _currentTarget = Option(value)

  def path: Option[Path] = _path

  def path_(value: Path) = _path = Option(value)
}