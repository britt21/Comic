# Rick and Morty Character Browser

A professional Android application built with Native Kotlin, Jetpack Compose, and Clean Architecture.

## Architecture
The project follows **Clean Architecture** principles combined with **MVVM (Model-View-ViewModel)**.

### Layers:
1.  **Domain Layer**: Contains the core business logic.
    - **Models**: Plain Kotlin data classes (`Character`, `Episode`).
    - **Repositories**: Interfaces defining data operations.
    - **Use Cases**: Single-responsibility components that encapsulate business rules (e.g., `GetCharactersUseCase`).
2.  **Data Layer**: Responsible for data retrieval and persistence.
    - **Remote**: Retrofit API interface and DTOs.
    - **Local**: Room Database, Entities, and DAOs.
    - **Repository Implementation**: Coordinates between remote and local data sources. Uses **Paging 3 with RemoteMediator** for seamless pagination and offline support.
3.  **Presentation Layer**: UI and state management.
    - **Jetpack Compose**: For a modern, declarative UI.
    - **ViewModels**: Manage UI state and communicate with Use Cases.
    - **Hilt**: Dependency Injection for decoupling components.

## Key Features
- **Task 2 - Pagination**: Implemented using `Paging 3` to efficiently load characters.
- **Task 3 - Detail View**: Shows full character info and the first 3 episodes.
- **Task 4 - Offline Support**: Room database acts as a single source of truth. Data is cached locally and available offline.
- **Task 5 - Search & Filter**: Search queries are debounced in the ViewModel to prevent excessive API calls.
- **Task 6 - Error Handling**: Paging LoadStates are used to show loading/error views with retry logic.

## Technical Stack
- **Kotlin**
- **Jetpack Compose** (UI)
- **Hilt** (Dependency Injection)
- **Retrofit & OkHttp** (Networking)
- **Room** (Local Database)
- **Paging 3** (Pagination & Offline Caching)
- **Coil** (Image Loading)
- **Coroutines & Flow** (Asynchronous Programming)
