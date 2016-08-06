# JavaPasswordSafe

PasswordSafe is a password database utility. This project is a fork of the project of the same name 
available from https://sourceforge.net/projects/jpwsafe/

The fork is maintained by Noa Resare and is currently available from 
https://github.com/nresare/javapasswordsafe

This is a re-implementation of [PasswordSafe](https://pwsafe.org) and share many of the features 
and the file format of the original original project.

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
project once more.


## What has been done in the fork?

1. The original source has been imported into git using the following invocation 
```shell
git svn clone -s --ignore-paths='(PasswordSafeLib/lib|PasswordSafeSWT/lib|platform|PasswordSafeJ/)' \
 svn://svn.code.sf.net/p/jpwsafe/code`
git for-each-ref --format="%(refname:short) %(objectname)" refs/remotes/origin/tags \
 |  cut -d / -f 3- | while read ref; do git tag -a $ref -m 'import tag from svn'; done

```
2. Maven build configuration has been added
3. Needed dependencies has been added from maven central for PasswordSafeLib to be built and tests
   run.
4. S3 client code has bee ported to aws-java-sdk-s3 (the previous s3.jar seemed ancient an not available from anyhwere)

## What is left to be done?

1. Figure out the license chaos of the various components.
2. Investigate the possibility to use crypto classes included in recent JVMs instead of bouncycastle
3. Investigate the possibility to create and release this project on the macOS App Store.
4. PassphraseUtilsTest is flaky (sometimes a password is legitimately missing chars of a certain
   class)
5. A test run leaves files behind.

