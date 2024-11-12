# # ThoughtPad

<p><img title="Feature Graphic" src="https://github.com/user-attachments/assets/322c226a-7db5-4bc7-9333-2b8092b3bba0" alt="Feature graphic" /></p>

<a href = "https://play.google.com/store/apps/details?id=com.gitsoft.thoughtpad&pli=1">
<img src = "https://github.com/user-attachments/assets/e031483f-0229-49c1-aec4-23309aabfb2e" height = "56" alt = "Google Play"/>
</a>

ThoughtPad is a simple yet powerful app designed to help you organize your thoughts, tasks, and reminders all in one place. Whether you’re planning your day, jotting down ideas, or keeping record of your day, ThoughtPad is here to help you achieve your goals.

Key features of ThoughtPad include:

- **Search**: Quickly find notes by searching keywords or tags. 
- **Reminders**: Set custom reminders for individual notes to receive notifications on important dates. 
- **Tags**: Organize your notes with tags, making it easier to categorize and retrieve them. 
- **Widget**: Add a ThoughtPad widget to your home screen for quick access to your most important notes and reminders. 
- **App Actions**: Take advantage of Android’s App Actions to quickly create new notes or reminders from your phone’s home screen. 
- **Dark Mode**: A sleek, dark mode for better nighttime reading and app interaction. 
- **Security**: Secure your most private information by password on fingerprint authentication.
- **Offline Access**: Access your notes and reminders even when you're offline. 
- **Room Database**: All your notes and reminders are stored locally using Room, ensuring fast and reliable access without the need for an internet connection. 
- **Sync (Coming Soon)**: Sync notes across devices with cloud integration (Planned feature).

---

#### Modularization

**NB**: The app has 3 modules `app` and `notelist` , `notedetail/addnote` and `settings`.

**Components:**

1. [Jetpack compose](https://developer.android.com/jetpack/compose) - modern UI toolkit for designing great user interfaces on Android.

2. [Koin](https://insert-koin.io/): Pragmatic dependency injection library.

3. [Timber](https://github.com/JakeWharton/timber): This is a logger with a small, extensible API which provides utility on top of Android's normal `Log` class.

4. [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html): concurrency design pattern used on Android to simplify code that executes asynchronously.

5. [Material 3](https://m3.material.io/):  latest version of Google’s open-source design system.

6. [Lifecycle components](https://developer.android.com/reference/androidx/lifecycle/package-summary) : Lifecycle-aware components perform actions in response to a change in the lifecycle status of another component, such as activities and fragments.

7. [Datastore](https://developer.android.com/jetpack/androidx/releases/datastore): Store data asynchronously, consistently, and transactionally, overcoming some of the drawbacks of SharedPreferences.



#### Coming up

- Widget

- App actions

- Cloud sync

- Secure notes
