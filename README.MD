# The library serves to generate decentralized unique ID with long number format or text ordered format
The cogwheel ID generator makes about 250 000 unique values per second for one JVM instance.

## Getting started with simple application

        CogwheelIdGenerator generator = CogwheelIdGenerator.randomOf("my-seed-value");
        long id = generator.uniqueLong();
        // id = 361986213143409583
        String text = generator.uniqueText();
        // text = 2O52L4S227TUJ

## Getting started with cluster of applications

        CogwheelIdGenerator generator = CogwheelIdGenerator.instanceOf(/* service id */ 1, /* replica id */ 1);
        long id = generator.uniqueLong();
        // id = 361986213143409583
        String text = generator.uniqueText();
        // text = 2O52L4S227TUJ

## Getting started with cluster of k8s replicaSet applications

        CogwheelIdGenerator generator = CogwheelIdGenerator.replicaSetOf(/* service id */ 1);
        long id = generator.uniqueLong();
        // id = 361986213143409583
        String text = generator.uniqueText();
        // text = 2O52L4S227TUJ

## Support
If you have any question to contact with me dimail777@gmail.com
