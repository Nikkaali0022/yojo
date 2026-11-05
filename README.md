# YOJO — Complete Android UI Project

YOJO is an Android-first social app project foundation for short videos, photos, stories,
creator profiles, discovery, inbox, notifications, creator tools, safety and administration.

## Included in this ZIP
- Kotlin + Jetpack Compose + Material 3
- Five main tabs: Home, Explore, Create, Inbox, Profile
- Story row and feed cards with local like interactions
- Explore/search UI
- Create flow with Video / Photo / Story selection and local demo publishing
- Profile editing and local settings
- Dark theme toggle
- Notifications, messaging, creator studio, saved items, drafts, safety, support and admin screens
- Animated screen transitions
- GitHub Actions workflow to build a debug APK
- Firebase intentionally not connected yet

## Build on GitHub
1. Extract this ZIP.
2. Upload the CONTENTS of the extracted folder to the root of a new GitHub repository named `YOJO`.
3. Commit to `main`.
4. Open Actions and wait for `YOJO Android Build` to finish.
5. Download the `YOJO-debug-apk` artifact.

## Build requirements
- JDK 17
- Android SDK 35
- Gradle 8.11.1

## Package
- Application ID: `com.yojo.app`
- Minimum Android: API 24
- Target Android: API 35

## Important: honest implementation status
This ZIP is a comprehensive UI/navigation and local-interaction foundation. It is NOT yet a production-ready social network.
Firebase is intentionally absent. Real authentication, account sync, remote feed, media picker/upload,
video transcoding/CDN playback, stories expiry, private messaging, push notifications,
recommendation ranking, live streaming, moderation enforcement, admin permissions and monetization
require backend services, security rules, media infrastructure and provider credentials.
Some screens in this build are clearly marked as integration points/demo-only. Do not launch publicly
or collect real user data until these services and safety controls are implemented and tested.

## Next backend integration
Create a dedicated Firebase project for YOJO, register `com.yojo.app`, enable Authentication,
Firestore, Storage and FCM, then add the configuration securely. Admin privileges must be enforced
server-side; never rely on a hidden UI button as security.
