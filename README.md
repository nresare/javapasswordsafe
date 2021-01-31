# JavaPasswordSafe

PasswordSafe is a password database utility. This project is a fork of the project of the same name 
available from https://sourceforge.net/projects/jpwsafe/

The fork is maintained by Noa Resare and is currently available from 
https://github.com/nresare/javapasswordsafe

This is a re-implementation of [PasswordSafe](https://pwsafe.org) and share many of the features 
and the file format of the original  project.

So far this fork has focused on my immediate needs, an easy-to-install macOS version. I would
be very happy to accept contributions adapting packaging to work with other targets such as Linux
or Windows.

## Why a fork?

The original project was very difficult to work with, which seems to have prevented work fixing
some issues making the software run on recent versions of macOS as evidenced by several
different discussion forum threads. Another kind of serious problem with the original project was
the dependency on "mystery jars", binary files checked into the repository. One central aim of
the fork is to replace those dependencies with known upstream versions from maven central.

All in all it was the impression that development seems to have stagnated with the upstream project,
and how the project was structured, hosted and built made the barriers of entry for new contributors
needlessly high. So I decided to experiment a bit, and see if I could create something that was
easier to work with.

The intention of this is not to create a hostile fork, if my ideas turns out to be workable I'm more
than happy to work with the original developers to eventually unify development into a single
project once more. So far, however, the upstream maintainers has not been responding to contact
attempts.

## Versioning

To avoid clobbering the upstream version numbering, should upstream development pick up at some
point, I have opted for making releases with the pre-release tag 'nresare' to the upcoming upstream
version followed by a simple integer version number according to paragraph 9 semver.org 2.0.0 spec.

## How to build?

Building should be simple, provided your `JAVA_HOME` points to a recent java. Building and running unit tests
should be as simple as executing `./gradlew check`. There is a target for building a macOS disk image
containing an .app with an embedded java runtime environment. To build it you need to download an appropriate
java runtime environment, see the comment in `PasswordSafeSWT/build.gradle` and execute the `./gradlew dmg`
target.

## What has been done in the fork?

1. The original source has been imported into git using the following invocation 
```shell
git svn clone -s --ignore-paths='(PasswordSafeLib/lib|PasswordSafeSWT/lib|platform|PasswordSafeJ/)' \
 svn://svn.code.sf.net/p/jpwsafe/code`
git for-each-ref --format="%(refname:short) %(objectname)" refs/remotes/origin/tags \
 |  cut -d / -f 3- | while read ref; do git tag -a $ref -m 'import tag from svn'; done

```
2. Gradle build configuration has been added
3. Needed dependencies has been added from maven central for PasswordSafeLib to be built and tests
   run.
4. Work on the unit tests to not leave files around (not finished)
6. ..various other things. Look at the commits :)

## What is left to be done?

1. Figure out the license chaos of the various components.
2. Investigate the possibility to use crypto classes included in recent JVMs instead of bouncycastle
3. Investigate the possibility to create and release this project on the macOS App Store.
4. A test run still leaves some files behind.

