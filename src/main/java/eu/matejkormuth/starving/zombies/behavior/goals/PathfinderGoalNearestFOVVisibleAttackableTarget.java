/**
 * Starving - Bukkit API server mod with Zombies.
 * Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package eu.matejkormuth.starving.zombies.behavior.goals;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import eu.matejkormuth.starving.main.NMSHooks;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.TrigMath;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@NMSHooks(version = "v1_8_R3")
public class PathfinderGoalNearestFOVVisibleAttackableTarget<T extends EntityLiving> extends PathfinderGoalTarget {
    // Type of target entity.
    protected final Class<T> targetEntityClass;
    // Each time the a() method is called a random number is generated in range
    // of zero to targetingSpeed. If the generated number is not zero, a method
    // will run.
    private final int targetingSpeed;
    // Instance of comparator used for sorting entities in list by their
    // distance to pathfinding entity.
    protected final DistanceComparator distanceComparator;
    // Represents Predicate by which are found entities filtered before they are
    // ordered by distance.
    protected Predicate<? super T> customFilter;
    // Represents current target.
    protected EntityLiving currentTarget;
    // Horizontal field of view.
    private final int halfHorizontalFov;
    // Vertical field of view.
    private final int halfVerticalFov;

    // EntityCreature holder - the holder of this goal.
    public PathfinderGoalNearestFOVVisibleAttackableTarget(EntityCreature holder, Class<T> targetEntityType,
            boolean flag, int horizontalFov, int verticalFov) {
        this(holder, targetEntityType, flag, false, horizontalFov, verticalFov);
    }

    public PathfinderGoalNearestFOVVisibleAttackableTarget(EntityCreature holder, Class<T> targetEntityType,
            boolean flag, boolean flag1, int horizontalFov, int verticalFov) {
        this(holder, targetEntityType, 10, flag, flag1, null, horizontalFov, verticalFov);
    }

    public PathfinderGoalNearestFOVVisibleAttackableTarget(EntityCreature holder, Class<T> targetEntityType,
            int targetingSpeed, boolean flag, boolean flag1, Predicate<? super T> predicate, int horizontalFov,
            int verticalFov) {
        super(holder, flag, flag1);
        this.targetEntityClass = targetEntityType;
        this.targetingSpeed = targetingSpeed;
        this.halfHorizontalFov = horizontalFov / 2;
        this.halfVerticalFov = verticalFov / 2;
        this.distanceComparator = new DistanceComparator(holder);
        // Have no idea what this does.
        a(1);
        // Setup custom filter for entities.
        this.customFilter = entity -> {
            // Check if parent predicate applies.
            if ((predicate != null) && (!(predicate.apply(entity)))) {
                return false;
            }

            if (entity instanceof EntityHuman) {
                // Get follow range from entity attributes.
                double followRange = PathfinderGoalNearestFOVVisibleAttackableTarget.this.f();

                // Lower the follow range if player is sneaking.
                if (entity.isSneaking()) {
                    followRange *= 0.800000011920929D;
                }

                // Keep invisibility logic.
                if (entity.isInvisible()) {
                    float f1 = ((EntityHuman) entity).bY();

                    if (f1 < 0.1F) {
                        f1 = 0.1F;
                    }

                    followRange *= 0.7F * f1;
                }

                // Check if the entity isn't too far.
                if (entity.g(PathfinderGoalNearestFOVVisibleAttackableTarget.this.e) > followRange) {
                    return false;
                }
            }

            // Check for FOV.
            double alpha = TrigMath.atan2(e.locZ - entity.locZ, e.locX - entity.locX);
            double beta = TrigMath.atan2(e.locY - entity.locY, e.locX - entity.locX);
            boolean isInFieldOfView = alpha < halfHorizontalFov && beta < halfVerticalFov;

            return PathfinderGoalNearestFOVVisibleAttackableTarget.this.a(entity, false) && isInFieldOfView;
        };
    }

    // Chooses target by some rules, returns false if no entity was chosen. True
    // when entity was chosen.
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean a() {
        // Check if we are going to find a target.
        if ((this.targetingSpeed > 0) && (this.e.bc().nextInt(this.targetingSpeed) != 0)) {
            return false;
        }

        // Get follow range from entity attributes.
        double followRange = f();

        // Create search region based on follow range attribute.
        AxisAlignedBB searchRegion = this.e.getBoundingBox().grow(followRange, 4.0D, followRange);

        // This predicate filters players that are not spectators.
        Predicate playersNotSpectators = IEntitySelector.d;

        // Select by class this.a and specified predicate that works as filter.
        List entities = this.e.world.a(this.targetEntityClass, searchRegion, Predicates.and(this.customFilter,
                playersNotSpectators));

        // Sort player entities by distance to zombie.
        Collections.sort(entities, this.distanceComparator);

        if (entities.isEmpty()) {
            // Return that we have not chosen target.
            return false;
        }

        // Select first entity in list as target.
        this.currentTarget = ((EntityLiving) entities.get(0));

        // Return that we have chosen target.
        return true;
    }

    // Probably called when entity chosen by a() method should be pathfinding
    // entity's new target.
    public void c()
    {
        // Determinate reason.
        EntityTargetEvent.TargetReason reason = (this.currentTarget instanceof EntityPlayer) ? EntityTargetEvent.TargetReason.CLOSEST_PLAYER
                : EntityTargetEvent.TargetReason.CLOSEST_ENTITY;
        // Set goal target.
        this.e.setGoalTarget(this.currentTarget, reason, true);
        // Reset.
        super.c();
    }

    public static class DistanceComparator implements Comparator<Entity> {
        // Origin entity used to measure distances to.
        private final Entity origin;

        public DistanceComparator(Entity origin) {
            this.origin = origin;
        }

        // Checks which of two entities is closer to origin entity.
        public int check(Entity entity, Entity entity1) {
            // Get distance to first entity.
            double distance0 = this.origin.h(entity);
            // Get distance to second entity.
            double distance1 = this.origin.h(entity1);

            return ((distance0 > distance1) ? 1 : (distance0 < distance1) ? -1 : 0);
        }

        // Checks which of two entities is closer to origin entity.
        public int compare(Entity object, Entity object1) {
            return check(object, object1);
        }
    }
}
