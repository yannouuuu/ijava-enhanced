#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -ne 2 ]; then
  echo "Usage: diff_jars.sh <old.jar> <new.jar>" >&2
  exit 1
fi

old="$1"
new="$2"

workdir=$(mktemp -d)
trap 'rm -rf "$workdir"' EXIT

mkdir -p "$workdir/old" "$workdir/new"
unzip -qq "$old" -d "$workdir/old"
unzip -qq "$new" -d "$workdir/new"

find "$workdir/new" -type f ! -name '*.class' -delete
find "$workdir/old" -type f ! -name '*.class' -delete
find "$workdir/new" -type d -empty -delete
find "$workdir/old" -type d -empty -delete

if diff -qr "$workdir/old" "$workdir/new"; then
  exit 0
else
  exit 1
fi
