package com.dimail.cogwheel;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class CogwheelIdGenerator {

    private final long id;
    private final AtomicReference<LocalIncrement> atomic;

    public static CogwheelIdGenerator statefulSetOf(long service) {
        var hostname = System.getenv("HOSTNAME");
        if (hostname == null || hostname.trim().equals("")) {
            throw new IllegalStateException("The HOSTNAME env var is not defined");
        }
        return instanceOf(service, extractReplica(hostname));
    }

    public static CogwheelIdGenerator randomOf(String randomSeed) {
        if (randomSeed == null || randomSeed.isBlank()) {
            randomSeed = UUID.randomUUID().toString();
        }
        int maxId = (int) (SERVICE_MAX + 1) * (int) (REPLICA_MAX + 1);
        int id = new SecureRandom(randomSeed.getBytes(StandardCharsets.UTF_8)).nextInt(maxId);
        return new CogwheelIdGenerator(id);
    }

    public static CogwheelIdGenerator instanceOf(long service, long replica) {
        if (replica < 0 || replica > REPLICA_MAX) {
            throw new IllegalArgumentException("The replicaId cen be between 0 and " + REPLICA_MAX + 1);
        }
        if (service < 0 || service > SERVICE_MAX) {
            throw new IllegalArgumentException("The serviceId must be between 0 and " + SERVICE_MAX + 1);
        }
        return new CogwheelIdGenerator((service << SERVICE_SHIFT) | replica);
    }

    private CogwheelIdGenerator(long id) {
        this.id = id;
        this.atomic = new AtomicReference<>(new LocalIncrement(-1, getEpochTime()));
    }

    public long uniqueLong() {
        LocalIncrement increment = getLocalIncrement();
        return (increment.snapshotTime << TIME_SHIFT) | (increment.increment << INCREMENT_SHIFT) | id;
    }

    public String uniqueText() {
        return Text32.text32(uniqueLong());
    }

    private LocalIncrement getLocalIncrement() {
        boolean done;
        LocalIncrement newIncrement;
        do {
            LocalIncrement localIncrement = atomic.get();
            newIncrement = localIncrement.newLocalIncrement(getEpochTime());
            if (newIncrement == null) {
                done = false;
            } else {
                done = atomic.compareAndSet(localIncrement, newIncrement);
            }
        } while (!done);
        return newIncrement;
    }

    private static long getEpochTime() {
        return System.currentTimeMillis() / 1000 - COVID_EPOCH;
    }

    // hostname = pod_name-0, pod_name-1, pod_name-2
    private static long extractReplica(String hostname) {
        var m = POD_NAME_REG.matcher(hostname);
        if (!m.find()) {
            throw new IllegalStateException("Wrong format of replica-set name - " + hostname);
        }
        var number = m.group(2);
        if (number == null) {
            throw new IllegalStateException("Wrong format of replica-set name - " + hostname);
        }
        try {
            return Long.parseLong(number);
        } catch (Exception ex) {
            throw new IllegalStateException("Wrong format of replica-set name - " + hostname);
        }
    }

    private static class LocalIncrement {

        private final long increment;
        private final long snapshotTime;

        public LocalIncrement(long increment, long snapshotTime) {
            this.increment = increment;
            this.snapshotTime = snapshotTime;
        }

        public LocalIncrement newLocalIncrement(long newSnapshotTime) {
            LocalIncrement newLocalIncrement = null;
            if (newSnapshotTime > snapshotTime) {
                newLocalIncrement = new LocalIncrement(0, newSnapshotTime);
            } else {
                if (INCREMENT_MAX != increment) {
                    newLocalIncrement = new LocalIncrement(increment + 1, snapshotTime);
                }
            }
            return newLocalIncrement;
        }
    }

    private static final int INCREMENT_LENGTH = 16;
    private static final int SERVICE_ID_LENGTH = 8;
    private static final int REPLICA_ID_LENGTH = 8;

    private static final int SERVICE_SHIFT = REPLICA_ID_LENGTH;
    private static final int INCREMENT_SHIFT = SERVICE_ID_LENGTH + REPLICA_ID_LENGTH;
    private static final int TIME_SHIFT = INCREMENT_LENGTH + SERVICE_ID_LENGTH + REPLICA_ID_LENGTH;

    private static final long SERVICE_MAX = (1 << SERVICE_ID_LENGTH) - 1;
    private static final long REPLICA_MAX = (1 << REPLICA_ID_LENGTH) - 1;
    private static final long INCREMENT_MAX = (1 << INCREMENT_LENGTH) - 1;

    private static final long COVID_EPOCH = 1577836800;

    private static final Pattern POD_NAME_REG = Pattern.compile("^([0-9a-zA-Z_-]+)-([0-9]+)$");

}
