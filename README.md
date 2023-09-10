# Gradi

Have you ever scared by how large is Gradle Caches?

Even though Gradle Officials explains that Caches will be cleaned automatically, there are still many trashes didn't get
cleaned or the automatic process never runs.

But Gradi won't. Gradi searches truly useless garbages and delete them in order to free your disk space.

# Plan

- Configurable Scanners
- Friendly CLI (PRS WELCOME)

# Targets

1. Remove backup dist downloaded by Gradle Wrapper. (DONE)
2. Remove unused or download-interrupted dist (DONE)
3. Remove logs from daemon (DONE)
4. Remove undeleted JDK archives downloaded by Gradle Toolchain (DONE)
5. Remove BuildScript Classes that never got cleaned. (DONE)
6. Global Cache: caches/jars-X (You'll have to re-download them, especially Android Developers) (DONE)
7. Remove old dependencies that you never use. (WIP)

All of them are optional and you can choose what you want to clean, but you will have to rebuild gradi.

# Screenshots
Before:  

![](https://upload.cc/i1/2022/05/14/rZ4qcf.png)

After:  

![](https://upload.cc/i1/2022/05/14/w6vqmd.png)
