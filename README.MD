# The library serves to generate decentralized unique ID with big integer format or lexicographical ordered text format
The cogwheel ID generator makes 65536 unique values per second for one JVM instance.

## Getting started with simple application

        CogwheelIdGenerator generator = CogwheelIdGenerator.randomOf("my-seed-value");
        long id = generator.uniqueLong();
        // id = 361986213143409583
        String base32 = generator.uniqueText();
        // base32 = 2O52L4S227TUJ

## Getting started with cluster of applications

        CogwheelIdGenerator generator = CogwheelIdGenerator.instanceOf(/* service id */ 1, /* replica id */ 1);
        long id = generator.uniqueLong();
        // id = 361986213143409583
        String base32 = generator.uniqueText();
        // base32 = 2O52L4S227TUJ

## Getting started with cluster of k8s StatefulSet with application

        CogwheelIdGenerator generator = CogwheelIdGenerator.statefulSetOf(/* service id */ 1);
        long id = generator.uniqueLong();
        // id = 361986213143409583
        String base32 = generator.uniqueText();
        // base32 = 2O52L4S227TUJ

## Support
If you have any question to contact with me dimail777@gmail.com
