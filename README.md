# Orator AI - Your Personal Presentation Coach


<img width="1919" height="1008" alt="image" src="https://github.com/user-attachments/assets/2ac0573f-38fb-4463-b90b-46a2c0a3fb0d" />





Orator AI is a full-stack desktop application designed to help users improve their public speaking and presentation skills. By leveraging a powerful pipeline of modern AI services, it provides comprehensive feedback on a user's delivery by comparing their spoken words to their original script.

This project was developed as a comprehensive demonstration of integrating multiple AI services into a seamless, user-friendly application with a robust backend and a dynamic frontend.

---

## Features

-   **Script Input:** Users can type, paste, or open a local `.txt` file containing their presentation script.
-   **Audio Recording:** A simple interface to record the user's delivery of the script.
-   **AI-Powered Analysis:** The application provides a detailed analysis of the user's performance, including:
    -   An overall score from 1-10.
    -   Positive feedback on what the user did well.
    -   Actionable points for improvement.
-   **Intelligent Audio Feedback:**
    -   **Automatic Tone Detection:** Gemini analyzes the script's content to determine the most appropriate emotional tone (e.g., confident, urgent).
    -   **Automatic Gender Matching:** The application detects the user's voice and provides a matching AI coach voice (male or female).
    -   **Ideal Delivery:** Murf.ai generates a high-quality audio version of the original script, delivered in the AI-recommended voice and tone, for the user to learn from.
-   **File System Integration:** As a desktop application, users can save their feedback as a text file and download the generated audio feedback directly to their computer.
-   **Animated Splash Screen:** A professional, animated loading screen provides a polished user experience on startup.

---
Here are the screenshots for the desktop application:
<img width="1600" height="839" alt="image" src="https://github.com/user-attachments/assets/8fa54d72-b046-4cb2-8ffc-5e2f05e57e66" />


<img width="1600" height="806" alt="image" src="https://github.com/user-attachments/assets/9acf87a3-46a1-4810-ad8d-027448bd302d" />

<img width="1600" height="611" alt="image" src="https://github.com/user-attachments/assets/6f951ac7-132d-4f8f-90a6-e31ae3fa0f70" />



## Tech Stack

### Frontend
-   **Framework:** React with TypeScript (built with Vite)
-   **Desktop Wrapper:** Electron
-   **Styling:** Tailwind CSS & shadcn/ui
-   **State Management:** React Hooks
-   **Animations:** Framer Motion
-   **API Communication:** Axios

### Backend
-   **Framework:** Spring Boot 3 with Java 21
-   **Architecture:** Service-Oriented REST API
-   **API Communication:** Reactive WebClient
-   **Core Dependencies:** Spring Web, Spring WebFlux, Lombok

### AI & Cloud Services
-   **Speech-to-Text:** AssemblyAI (with Speaker Diarization)
-   **AI Analysis & Logic:** Google Gemini
-   **Text-to-Speech:** Murf.ai

---

## How to Run

### Prerequisites
-   Node.js and npm
-   Java 21 (or newer) and Maven
-   API keys for AssemblyAI, Google Gemini, and Murf.ai

### 1. Backend Setup
1.  Navigate to the `backend` directory.
2.  Create a file named `application.properties` in `src/main/resources/`.
3.  Add your API keys to this file:
    ```properties
    server.port=8080
    
    assemblyai.api.key=YOUR_ASSEMBLYAI_API_KEY
    gemini.api.key=YOUR_GEMINI_API_KEY
    murf.api.key=YOUR_MURFAI_API_KEY
    
    # Increase file upload limits
    spring.servlet.multipart.max-file-size=10MB
    spring.servlet.multipart.max-request-size=10MB
    ```
4.  Run the Spring Boot application using your IDE or the Maven wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```
    The backend will be running on `http://localhost:8080`.

### 2. Frontend (Electron App) Setup
1.  Navigate to the `frontend` directory in a new terminal.
2.  Install the necessary dependencies:
    ```bash
    npm install
    ```
3.  Run the application:
    ```bash
    npm run electron:start
    ```
    This command will start the Vite development server and launch the Electron desktop application.

