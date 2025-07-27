# Orator AI - Your Personal Presentation Coach
(Demo at :https://youtu.be/mXajQ91wOmw?si=TqcTlgrlj8RbJ6S3)

<img width="1919" height="1011" alt="image" src="https://github.com/user-attachments/assets/de427be4-9c0f-476d-9268-995fbc245161" />






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
-   **Ideal Audio Delivery:** Murf.ai generates a high-quality audio version of the original script using a professional, clear voice. This allows the user to hear an example of an ideal delivery.
-   **File System Integration:** As a desktop application, users can save their feedback as a text file and download the generated audio feedback directly to their computer.
-   **Animated Splash Screen:** A professional, animated loading screen provides a polished user experience on startup.


---
Here are the screenshots for the desktop application:

<img width="1915" height="952" alt="image" src="https://github.com/user-attachments/assets/dc29ad04-afc2-4894-b681-60fd9cc15c41" />



<img width="1763" height="953" alt="image" src="https://github.com/user-attachments/assets/7982e76b-dc2c-4b46-97ba-c942ebd97e6b" />






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

    
## Future Updates

This project has a strong foundation that can be extended with even more intelligent features. Potential future updates include:

-   **AI-Driven Tone & Gender Matching:** Implement the logic for Gemini to analyze the script's emotional content and the user's voice to automatically select the most appropriate tone and gender for the ideal audio delivery from Murf.ai.
-   **Practice History & Progress Tracking:** Allow users to save their practice sessions and view their progress over time, with charts visualizing their score improvements for specific scripts.
-   **Real-time Delivery Feedback:** Provide live visual feedback during the recording process, such as a VU meter for volume and a words-per-minute (WPM) counter to help users manage their pacing in real-time.
-   **Advanced Analysis:** Expand the Gemini prompt to analyze and provide feedback on more nuanced aspects of speech, such as the use and effectiveness of pauses, sentiment analysis, and the detection of repetitive phrases.

