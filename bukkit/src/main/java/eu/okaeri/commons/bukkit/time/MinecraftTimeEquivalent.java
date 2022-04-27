package eu.okaeri.commons.bukkit.time;

import lombok.NonNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

// utility exposing easy ready-to-use time->ticks values
// remember the final time is dependant on the server performance (tps)
@SuppressWarnings({"PointlessArithmeticExpression", "FieldNamingConvention"})
public final class MinecraftTimeEquivalent {

    public static final int SECOND = 20;
    public static final int MINUTE = SECOND * 60;
    public static final int HOUR = MINUTE * 60;
    public static final int DAY = HOUR * 24;
    public static final int WEEK = DAY * 7;

    public static int ticksOf(@NonNull TimeUnit timeUnit, int value) {
        return ticksOf(timeUnit.toMillis(value));
    }

    public static int ticksOf(@NonNull Duration duration) {
        return ticksOf(duration, false);
    }

    public static int ticksOf(@NonNull Duration duration, boolean failsafe) {
        return ticksOf(duration.toMillis(), failsafe);
    }

    public static int ticksOf(long millis) {
        return ticksOf(millis, false);
    }

    public static int ticksOf(long millis, boolean failsafe) {
        if (millis < 50) {
            if (failsafe) {
                return 1;
            }
            throw new IllegalArgumentException("cannot transform " + millis + " ms to ticks, too low value");
        }
        return Math.toIntExact(millis / 50L);
    }
}
