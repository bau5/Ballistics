package com.bau5.ballistics

import net.minecraft.util.{BlockPos, Vec3}
import com.bau5.ballistics.RichVector._

/**
 * Created by bau5 on 9/25/15.
 */

object RichVector {
  def apply(p: BlockPos) = new Vec3(p.getX, p.getY, p.getZ)

  implicit def toRichVector(v: Vec3) = RichVector(v.xCoord, v.yCoord, v.zCoord)
  implicit def toRichVector(p: BlockPos) = RichVector(p.getX, p.getY, p.getZ)
}

case class RichVector(x: Double, y: Double, z: Double) extends Vec3(x, y, z) {
  def xzDistanceToTarget(p: BlockPos): Double = xzDistanceToTarget(p.getX, p.getZ)
  def xzDistanceToTarget(xi: Double, zi: Double): Double = {
    Math.sqrt(Math.pow(x - xi, 2) + Math.pow(z - zi, 2))
  }
}

object Path {
  def apply(initial: BlockPos, target: BlockPos) = {
    val climbTo = if (initial.getY > target.getY) {
      initial.add(0, 10D, 0)
    } else {
      initial.add(0, Math.abs(124D - target.getY), 0)
    }

    // distance to travel from cruising altitude to the target
    val dY = Math.abs(target.getY - climbTo.getY)
    // x,z should be certain percentage of the total xz dist to travel
    // y should be a percentage of only the y dist to travel
    val dist = RichVector(target).distanceTo(climbTo)

    println(
      s"""
         |Initial      $initial
         |Climb to     $climbTo
         |Descend at   ----
         |Target       $target
         |Distance XZ  ${target.xzDistanceToTarget(initial)}
         |Distance Y   $dY
         |Distance     $dist
       """.stripMargin
    )

    new Path(RichVector(initial), RichVector(climbTo), RichVector(new BlockPos(0, 0, 0)), RichVector(target))
  }
}

case class Path(initial: Vec3, climbTo: Vec3, descendAt: Vec3, target: Vec3) {
  val velocityVector = target.subtract(climbTo)
  val totalDistance = target.distanceTo(climbTo)

  def pointAtT(t: Double): Vec3 = {
    new Vec3(
      climbTo.xCoord + (t * velocityVector.xCoord),
      climbTo.yCoord + (t * velocityVector.yCoord),
      climbTo.zCoord + (t * velocityVector.zCoord)
    )
  }

  def asList = List(initial, climbTo, descendAt, target)
}
