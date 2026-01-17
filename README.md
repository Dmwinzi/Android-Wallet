# Digital Wallet Android Application

A robust, offline-first mobile application developed with Jetpack Compose. This client integrates with a Spring Boot backend to facilitate digital transactions with high reliability and data persistence.

## Project Overview

The application implements a local outbox pattern, ensuring that financial transactions are never lost due to poor network connectivity. By leveraging a local Room database and WorkManager, the app provides a seamless user experience that handles background synchronization and automatic error recovery.

## Technical Specifications

* **UI Framework**: Jetpack Compose (Material 3)
* **Architecture**: MVVM with Clean Architecture principles
* **Asynchronous Flow**: Kotlin Coroutines and StateFlow
* **Dependency Injection**: Hilt (Dagger)
* **Local Persistence**: Room Database for transaction caching
* **Session Management**: Preferences DataStore for secure user metadata storage
* **Background Tasks**: WorkManager for reliable API synchronization
* **Networking**: Retrofit 2 with OkHttp logging

## System Architecture

The application follows a reactive data flow:

1. **User Interaction**: Transactions are initiated via the UI and validated locally.
2. **Local Persistence**: Data is committed to the Room database with a status of `QUEUED`.
3. **Background Sync**: A `SyncTransactionWorker` is immediately enqueued to process the transaction.
4. **Network Execution**: The worker communicates with the Spring Boot REST API.
5. **State Finalization**: Upon a successful response, the status is updated to `SYNCED`. If the network fails, the system executes an exponential backoff retry strategy.

## Getting Started

### Prerequisites

* Android Studio Ladybug or later
* Android SDK 26 or higher
* A running instance of the Digital Wallet Backend API

### Installation and Setup

1. Clone the repository to your local machine.
2. Open the project in Android Studio and allow Gradle to sync.
3. Verify the server connection string:
* For **Android Emulator**: The application is pre-configured to use `http://10.0.2.2:8080/`.
* For **Physical Devices**: Navigate to `di/AppModule.kt` and update the `BASE_URL` to your machine's local IP address.


4. Execute the build and deploy to your chosen device.

## Synchronization and Retry Policy

The application is designed to handle API and network instability:

* **Retry Limit**: The system will attempt to synchronize a transaction up to 3 times.
* **Failure Handling**: If the retry limit is exceeded or a business logic error is returned (e.g., 400 Bad Request), the transaction is marked as `FAILED`.
* **Error Visibility**: Specific server error messages are captured and displayed on the transaction detail view to assist in troubleshooting.

## Known Limitations

* **Sync Latency**: Depending on the device's battery optimization settings, WorkManager may batch requests, leading to a slight delay in synchronization.
* **Session Expiry**: If the backend database state is reset, the user must re-authenticate to synchronize new transactions with a valid Customer ID.
