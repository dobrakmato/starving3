package eu.matejkormuth.starving.zombies.groups;

import eu.matejkormuth.starving.zombies.Zombie;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ZombieGroup {

    /**
     * Maximum amount of members in one zombie group.
     */
    private static final int MAX_MEMBERS = 16;
    private static final Random random = new Random();

    private final List<Zombie> members;

    public ZombieGroup() {
        members = new ArrayList<>(MAX_MEMBERS);
    }

    public boolean add(Zombie zombie) {
        if (this.members.size() >= MAX_MEMBERS) {
            throw new IllegalStateException("This group is full.");
        }

        return members.add(zombie);
    }

    public boolean remove(Object o) {
        return members.remove(o);
    }

    /**
     * Returns random member from this group.
     *
     * @return random member from group
     */
    public Zombie getRandomMember() {
        return members.get(random.nextInt(members.size()));
    }

    /**
     * Gets location of this group. Location is computed by averaging positions of all members.
     *
     * @return location of this group
     */
    public Location getLocation() {
        // We are sure these values wont overflow because our map is within bounds of -6000 to 6000.
        int x = 0;
        int z = 0;
        int y = 0;
        int count = 0;
        for (Zombie zombie : members) {
            x += zombie.getX();
            y += zombie.getY();
            z += zombie.getZ();
            count++;
        }
        return new Location(members.get(0).getWorld().getWorld(), x / count, y / count, z / count);
    }

    public int size() {
        return members.size();
    }
}
