package com.bau5.ballistics

import net.minecraft.util.{BlockPos, Vec3}

/**
 * Created by bau5 on 9/25/15.
 */
object Target {
  def apply(x: Int, y: Int, z: Int): Target = new Target(new BlockPos(x, y, z))
}

case class Target(pos: BlockPos) {

  def distanceToTarget(p: BlockPos): Double = distanceToTarget(p.getX, p.getY, p.getZ)
  def distanceToTarget(xi: Double, yi: Double, zi: Double): Double = {
    Math.sqrt(Math.pow(pos.getX - xi, 2) + Math.pow(pos.getY - yi, 2) + Math.pow(pos.getZ - zi, 2))
  }

  def xzDistanceToTarget(p: BlockPos): Double = xzDistanceToTarget(p.getX, p.getZ)
  def xzDistanceToTarget(xi: Double, zi: Double): Double = {
    Math.sqrt(Math.pow(pos.getX - xi, 2) + Math.pow(pos.getZ - zi, 2))
  }

  def centeredTarget: (Double, Double, Double) = (pos.getX + 0.5, pos.getY + 0.5, pos.getZ + 0.5)
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
    val dist = new Target(target).distanceToTarget(climbTo)


    println(
      s"""
         |Initial      $initial
         |Climb to     $climbTo
         |Descend at   ----
         |Target       $target
         |Distance XZ  ${Target(target).xzDistanceToTarget(initial)}
          |Distance Y   $dY
          |Distance     $dist
       """.stripMargin
    )

    new Path(initial, climbTo, new BlockPos(0, 0, 0), target)
  }
}

case class Path(initial: BlockPos, climbTo: BlockPos, descendAt: BlockPos, target: BlockPos) {
  val diffVector = target.subtract(climbTo)
  val totalDistance = new Target(target).distanceToTarget(climbTo)

  def pointAtT(t: Double): Vec3 = {
    new Vec3(
      climbTo.getX + (t * diffVector.getX),
      climbTo.getY + (t * diffVector.getY),
      climbTo.getZ + (t * diffVector.getZ)
    )
  }

  def asList = List(initial, climbTo, descendAt, target)
}
