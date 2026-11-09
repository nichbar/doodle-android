#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

echo "=== Doodle Android Release Builder ==="
echo ""

# Check for key.properties
if [ ! -f "key.properties" ]; then
  echo "ERROR: key.properties not found at ./key.properties"
  echo ""
  echo "To set up release signing:"
  echo "  1. Copy key.properties.example to key.properties:"
  echo "     cp key.properties.example key.properties"
  echo "  2. Generate a keystore if you don't already have one:"
  echo "     keytool -genkey -v -keystore upload-keystore.jks -alias doodle -keyalg RSA -keysize 2048 -validity 10000"
  echo "  3. Update key.properties with your keystore path and passwords."
  exit 1
fi

# Check for keystore file referenced in key.properties
KEYSTORE_FILE=$(grep '^storeFile=' key.properties | cut -d'=' -f2- | tr -d '\r\n')
if [ -z "$KEYSTORE_FILE" ] || { [ ! -f "$KEYSTORE_FILE" ] && [ ! -f "app/$KEYSTORE_FILE" ]; }; then
  echo "ERROR: Keystore file '$KEYSTORE_FILE' referenced in key.properties not found."
  exit 1
fi

VERSION_NAME=$(grep "versionName" app/build.gradle | head -1 | sed -E "s/.*['\"]([^'\"]+)['\"].*/\1/")
VERSION_CODE=$(grep "versionCode" app/build.gradle | head -1 | sed -E 's/.*[[:space:]]+([0-9]+).*/\1/')

echo "Version Name: $VERSION_NAME"
echo "Version Code: $VERSION_CODE"
echo ""

# Run verification tests
echo "--- Running verification checks ---"
./gradlew check

# Build release APK
echo ""
echo "--- Building Release APK ---"
./gradlew assembleRelease
APK_OUTPUT="Doodle.v${VERSION_NAME}.apk"
cp app/build/outputs/apk/release/app-release.apk "$APK_OUTPUT"

# Build release bundle (AAB)
echo ""
echo "--- Building Release App Bundle (AAB) ---"
./gradlew bundleRelease
AAB_OUTPUT="Doodle.v${VERSION_NAME}.aab"
cp app/build/outputs/bundle/release/app-release.aab "$AAB_OUTPUT"

echo ""
echo "=== Build Complete ==="
echo ""
echo "Artifacts generated:"
ls -lh "$APK_OUTPUT" "$AAB_OUTPUT"
echo ""
echo "Next steps:"
echo "  1. Test on connected device:"
echo "     adb install -r $APK_OUTPUT"
echo "  2. Create git tag:"
echo "     git tag v${VERSION_NAME}"
echo "  3. Push git tag to trigger GitHub Actions release:"
echo "     git push origin v${VERSION_NAME}"
