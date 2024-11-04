
https://github.com/user-attachments/assets/67b62156-99c6-4804-bd5e-9b33188a5ec7


🌦 Weather App

Welcome to the Weather App repository! This project is a modern, feature-rich Android application that provides real-time weather updates based on the user’s current location.

🛠 Technologies Used


  ○ Jetpack Compose: Modern UI toolkit for building responsive and dynamic user interfaces.
  
  ○ MVI (Model-View-Intent) Architecture: Ensures a unidirectional data flow for better state management and scalability.
  
  ○ Clean Architecture (3 Layers): Divides the project into presentation, domain, and data layers for enhanced separation of concerns and testability.
  
  ○ Kotlin Coroutines: Handles asynchronous programming for smooth, non-blocking operations.
  
  ○ StateFlow with Channels: Manages reactive state handling and single-event emissions.
  
  ○ Hilt (Dagger): Simplifies dependency injection across the app.
  
  ○ Location Tracker: Fetches and updates the user's current location, enabling precise weather data retrieval.

⚙️ Features

  ○ Location-based Weather Data: Automatically retrieves and displays weather data based on the user’s location.
  
  ○ Retry Mechanism: Includes an option for users to retry data loading if location services are turned off or unavailable.
  
  ○ Error Handling with Toasts: Provides user-friendly feedback when issues arise, such as location permission denial or connectivity problems.
  
  ○ Loading Indicator: Displays a CircularProgressIndicator while data is being fetched, enhancing the user experience.

🌟 Key Highlights

This app was designed with modern Android practices to ensure high performance and maintainability:

  ○ State Management: Utilizes StateFlow combined with channels for robust state handling.
  
  ○ Seamless UI Updates: Thanks to Jetpack Compose, the app boasts a modern, adaptive user interface.
  
  ○ Clean Code: Built using a layered architecture that promotes clear separation of concerns and easier code maintenance.
