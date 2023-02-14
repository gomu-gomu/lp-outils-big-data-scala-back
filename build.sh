#!/usr/bin/env bash
dist="dist"
src="src"
file="Hello"

# Clean up
rm -rf "./${dist}"

# Dist creation
mkdir "./${dist}"

# Compilation
scalac -d "./${dist}" "./${src}/${file}.scala"

# Run
cd $dist && scala $file