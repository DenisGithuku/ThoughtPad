#!/bin/bash

# Read current versionCode and versionName from build.gradle
VERSION_CODE=$(grep versionCode app/build.gradle | awk '{print $2}')
VERSION_NAME=$(grep versionName app/build.gradle | awk '{print $2}' | tr -d '"')

# Split versionName into major, minor, and patch
IFS='.' read -r -a VERSION_PARTS <<< "$VERSION_NAME"

# Extract major, minor, and patch numbers
MAJOR=${VERSION_PARTS[0]}
MINOR=${VERSION_PARTS[1]}
PATCH=${VERSION_PARTS[2]:-0}

# Check if we should increment minor instead of patch
if [ "$PATCH" -eq 9 ]; then
    NEW_PATCH=0
    NEW_MINOR=$((MINOR + 1))
else
    NEW_PATCH=$((PATCH + 1))
    NEW_MINOR=$MINOR
fi

# Construct new versionName
NEW_VERSION_NAME="${MAJOR}.${NEW_MINOR}.${NEW_PATCH}"

# Update build.gradle with new values
sed -i "s/versionCode $VERSION_CODE/versionCode $((VERSION_CODE + 1))/" app/build.gradle
sed -i "s/versionName \"$VERSION_NAME\"/versionName \"$NEW_VERSION_NAME\"/" app/build.gradle

echo "Updated to versionCode: $((VERSION_CODE + 1)) and versionName: $NEW_VERSION_NAME"
