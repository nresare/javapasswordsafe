#!/bin/sh

LAUNCHER_DIR=$(dirname "$0")
CONTENTS_DIR=$LAUNCHER_DIR/..
JAVA_HOME=@javaRuntimeVersion@

exec "$CONTENTS_DIR"/$JAVA_HOME/bin/java \
  -cp "$CONTENTS_DIR/Java/*" \
  -Xdock:icon="$CONTENTS_DIR/Resources/PasswordSafe.icns" \
  -Xdock:name=PasswordsSafe \
  -XstartOnFirstThread \
  org.pwsafe.passwordsafeswt.PasswordSafeJFace
