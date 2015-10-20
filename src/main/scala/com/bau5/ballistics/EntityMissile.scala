package com.bau5.ballistics

import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

/**
 * Created by bau5 on 9/27/15.
 */
class EntityMissile(w: World) extends Entity(w) {

  val speed = 1.0F

  var path: Path = null
  var movingTo: RichVector = null

  override def onEntityUpdate(): Unit = {
    if (movingTo == null && path != null) {
      movingTo = path.climbTo
    }

    if (movingTo != null && path != null) {
      val vec = movingTo.subtract(posX, posY, posZ)
      println(vec)
    }

    super.onEntityUpdate()
  }

  def setPath(p: Path) = path = p

  override def entityInit(): Unit = {}

  override def writeEntityToNBT(tag: NBTTagCompound): Unit = {

  }
  override def readEntityFromNBT(tag: NBTTagCompound): Unit = {

  }
}
