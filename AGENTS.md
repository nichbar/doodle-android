# Project Contract

## Build And Test

- Build debug APK: `./gradlew assembleDebug`
- Build release APK: `./gradlew assembleRelease`
- Build release bundle (AAB): `./gradlew bundleRelease`
- Local release script: `./release.sh`
- Clean: `./gradlew clean`
- Test: `./gradlew test` (or `./gradlew testDebugUnitTest`)
- Lint: `./gradlew lintDebug` (or `./gradlew lint`)
- Lint auto-fix: `./gradlew lintFix`
- Check all: `./gradlew check`
- Install to device: `./gradlew installDebug`

## Architecture Boundaries

- Activities live in `app/src/main/java/xyz/zedler/patrick/doodle/activity/`
- Navigation screens / UI fragments live in `app/src/main/java/xyz/zedler/patrick/doodle/fragment/`
- Bottom sheet dialog fragments live in `app/src/main/java/xyz/zedler/patrick/doodle/fragment/dialog/`
- Live wallpaper service engine lives in `app/src/main/java/xyz/zedler/patrick/doodle/service/LiveWallpaperService.java`
- Wallpaper definitions live in `app/src/main/java/xyz/zedler/patrick/doodle/wallpaper/` and extend `BaseWallpaper`
- SVG rendering engine lives in `app/src/main/java/xyz/zedler/patrick/doodle/drawable/SvgDrawable.java` and `parser/PathParser.java`
- Custom UI views live in `app/src/main/java/xyz/zedler/patrick/doodle/view/`
- Shared utilities live in `app/src/main/java/xyz/zedler/patrick/doodle/util/`
- Shared preference keys, action constants, and default values live in `app/src/main/java/xyz/zedler/patrick/doodle/Constants.java`
- Material component overrides live in `app/src/main/java/com/google/android/material/`
- Wallpaper SVG resource files live in `app/src/main/res/raw/`
- Do not render wallpapers with Bitmaps or 3D engines; draw vectors directly to Android `Canvas` via `SvgDrawable`
- Do not put UI presentation or fragment logic inside `service/` or `wallpaper/`

## Coding Conventions

- Pure Java codebase targeting Java 17 compatibility (`minSdk 21`, `targetSdk 35`, `compileSdk 35`)
- Include the GPL-3.0 copyright license header on all new Java and XML source files
- Use 2-space indentation for Java code and 4-space continuation indentation for XML
- Use ViewBinding instead of `findViewById` where available (`buildFeatures.viewBinding true`)
- Use `PrefsUtil` and `Constants.PREF` for SharedPreferences access; never hardcode preference key strings
- Use `HapticUtil` for tactile feedback on user actions
- New wallpapers must extend `BaseWallpaper` and register in `Constants.WALLPAPER`, `Constants.getAllWallpapers()`, and `Constants.getWallpaper(String)`
- SVGs must follow `CUSTOM.md`: 1080x1920 viewBox, unique object IDs, solid background rect, SVGOMG cleaned, only supported elements (`g`, `path`, `rect`, `circle`, `ellipse`, `image`; no scaling or matrix transformations)

## Safety Rails

### NEVER

- Modify `gradle/libs.versions.toml` or `gradle/wrapper/` without explicit approval
- Modify or remove existing keys in `Constants.PREF` without backwards compatibility or migration handling
- Commit without running `./gradlew check`
- Manually edit generated string files that bypass Transifex mapping in `transifex-config.yml`
- Introduce Bitmaps or heavy memory allocations into wallpaper draw loops in `LiveWallpaperService`

### ALWAYS

- Show diff before committing
- Update `app/src/main/res/raw/changelog.txt` and `fastlane/metadata/android/en-US/changelogs/<versionCode>.txt` for user-facing changes
- Verify newly added or modified SVGs against `CUSTOM.md` constraints before adding to `app/src/main/res/raw/`
- Run `./gradlew lintDebug` and ensure zero errors before completing tasks

## Verification

- Code or UI changes: `./gradlew assembleDebug` + `./gradlew lintDebug`
- Full project check: `./gradlew check`
- Asset / resource changes: `./gradlew processDebugResources`
- Release readiness: `./gradlew assembleRelease`

## Compact Instructions

Preserve:

1. Architecture decisions (NEVER summarize)
2. Modified files and key changes
3. Current verification status (pass/fail commands)
4. Open risks, TODOs, rollback notes
