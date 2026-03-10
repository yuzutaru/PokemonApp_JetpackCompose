# PokeApp_JetpackCompose

A modern Android application built with Jetpack Compose, demonstrating Clean Architecture, multi-module setup, and various Jetpack libraries.

## 🚀 Technologies & Libraries

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for building native UI.
- **Language**: [Kotlin](https://kotlinlang.org/) (2.0.21) with Compose Compiler.
- **Dependency Injection**: [Koin](https://insert-koin.io/) for lightweight dependency injection.
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) for API communication.
- **Local Database**: [Room](https://developer.android.com/training/data-storage/room) for offline caching and persistence.
- **Pagination**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data) for efficient list loading.
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) for asynchronous image loading.
- **Navigation**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for screen transitions.
- **Session Management**: [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore) for user session handling.
- **Concurrency**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html).
- **Testing**: JUnit 4, Mockk, and Espresso for unit and UI testing.

## 🏗 Architecture

The project follows **Clean Architecture** principles combined with **MVVM (Model-View-ViewModel)**:

- **Domain Layer**: Contains Business Logic, UseCases, and Domain Models.
- **Data Layer**: Handles data retrieval from Remote (API) and Local (Room) sources, implementing Repository interfaces.
- **Presentation Layer**: Compose-based UI components, ViewModels, States, and Events.

## 📂 Project Structure

The project is modularized by feature and core functionality:

- **`:app`**: The main entry point. Initializes Koin, sets up the Global Navigation Host, and coordinates between modules.
- **`:core`**: Shared resources, common UI widgets, base theme, networking configuration, and session management.
- **`:auth`**: Handles authentication flows including Login and Registration.
- **`:dashboard`**: The main feature module containing Pokemon listing (with Paging), Pokemon details, and User Profile.

## 📱 Screen UI Structure

The app uses a single-activity architecture with `NavHost` managing multiple navigation graphs:

- **Auth Graph**:
  - `LoginScreen`: User authentication entry.
  - `RegisterScreen`: New user registration.
- **Main Menu Graph**:
  - `DashboardScreen`: A container screen with a **Bottom Navigation Bar** that toggles between:
    - `MenuScreen` (Home): Paginated list of Pokemon.
    - `ProfileScreen`: User profile details and logout option.
  - `DetailScreen`: Detailed information about a specific Pokemon, navigated from the list.

## 🛠 Setup

1. Clone the repository.
2. Sync the project with Gradle.
3. Run the `:app` module on an Android emulator or device.
