# IPA Test
Contains utilities for testing APIs

# How To Use

Pull in the artifact via the following maven coordinates:

```
<groupId>com.vspglobal.ipa</groupId>
<artifactId>ipa-test</artifactId>
<version>1.6.2</version>
```
# MockAPIServer

```
withDelayInMillis(long delayInMillis)
```
Adds a delay for all paths before API call. 

```
registerStatusAndTimeout(String path, int status, long timeout)
```
Adds a timeout for a specific path that will be used instead of what is declared by withDelayInMillis(long delayInMillis)
