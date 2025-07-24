# Voice Recommendation Feature

## Overview
This feature analyzes user speech and recommends optimal voice characteristics for presentation improvement using AI-powered analysis.

## How It Works
1. **Speech Analysis**: User recites their speech (audio or text)
2. **AI Analysis**: Gemini AI analyzes the speech content and delivery style
3. **Voice Recommendation**: System selects optimal Murf AI voice based on analysis
4. **Audio Generation**: Murf AI generates the speech in the recommended voice
5. **Playback**: Frontend plays the optimized version for user learning

## API Endpoints

### 1. Complete Voice Recommendation
```http
POST /api/voice/recommend
Content-Type: application/json

{
  "speechText": "Your speech text here...",
  "audioBase64": "base64-encoded-audio-data", // Optional if speechText provided
  "presentationType": "business", // business, casual, educational
  "targetAudience": "professional", // professional, students, general
  "preferredGender": "MALE" // Optional: MALE, FEMALE
}
```

**Response:**
```json
{
  "success": true,
  "analysis": {
    "analysisId": "uuid",
    "userSpeechText": "Your speech text...",
    "detectedTone": "hesitant",
    "recommendedTone": "confident",
    "recommendedVoice": {
      "voiceId": "marcus",
      "gender": "MALE",
      "age": "MIDDLE_AGED",
      "primaryTone": "CONFIDENT",
      "description": "Deep, authoritative voice perfect for leadership presentations"
    },
    "analysisReason": "Your speech shows uncertainty. A confident voice will demonstrate the authority needed for business presentations.",
    "improvementAreas": ["Reduce hesitation", "Increase authority", "Improve pacing"],
    "confidenceScore": 0.85
  },
  "generatedAudioUrl": "https://murf-audio-url.com/audio.mp3"
}
```

### 2. Voice Analysis Only
```http
POST /api/voice/analyze
Content-Type: application/json

{
  "speechText": "Your speech text here...",
  "presentationType": "business",
  "targetAudience": "professional"
}
```

### 3. Get Available Voices
```http
GET /api/voice/voices
```

### 4. Get Voices by Tone
```http
GET /api/voice/voices/tone/confident
```

### 5. Test Endpoints
```http
GET /api/test/health
GET /api/test/voices
POST /api/test/voice-recommendation
```

## Available Voice Options

### Confident Voices
- **MARCUS** (Male, Middle-aged): Deep, authoritative voice for leadership
- **DAVID** (Male, Young): Clear, professional voice with confidence
- **SARAH** (Female, Middle-aged): Strong, professional female voice
- **EMMA** (Female, Young): Crisp, confident voice for business

### Conversational Voices
- **ALEX** (Male, Young): Friendly, approachable voice
- **JAMES** (Male, Middle-aged): Warm, engaging voice for storytelling
- **LILY** (Female, Young): Natural, friendly voice
- **SOPHIA** (Female, Middle-aged): Warm, relatable voice

### Urgent/Energetic Voices
- **RYAN** (Male, Young): High-energy voice for dynamic presentations
- **MAYA** (Female, Young): Energetic, compelling voice

### Empathetic Voices
- **MICHAEL** (Male, Middle-aged): Gentle, understanding voice
- **GRACE** (Female, Middle-aged): Compassionate, caring voice

## Tone Types
- **CONFIDENT**: Strong, assured tone for authority
- **CONVERSATIONAL**: Natural, friendly tone for engagement
- **URGENT**: Energetic, pressing tone for importance
- **EMPATHETIC**: Warm, understanding tone for care

## Environment Variables
Set these environment variables:
```bash
GEMINI_API_KEY=your-gemini-api-key
MURF_API_KEY=your-murf-api-key
ASSEMBLYAI_API_KEY=your-assemblyai-api-key
```

## Usage Flow

### Frontend Integration
1. Record user speech or get text input
2. Call `/api/voice/recommend` with speech data
3. Display analysis results to user
4. Play recommended voice audio
5. Allow user to compare their delivery with the AI-recommended version

### Example Frontend Code
```javascript
// Analyze and get voice recommendation
const analyzeVoice = async (speechText, presentationType = 'business') => {
  const response = await fetch('/api/voice/recommend', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      speechText,
      presentationType,
      targetAudience: 'professional'
    })
  });
  
  const result = await response.json();
  
  if (result.success) {
    // Display analysis
    console.log('Recommended Voice:', result.analysis.recommendedVoice.voiceId);
    console.log('Improvement Areas:', result.analysis.improvementAreas);
    
    // Play recommended audio
    const audio = new Audio(result.generatedAudioUrl);
    audio.play();
  }
};
```

## Testing
1. Start the application
2. Test health: `GET /api/test/health`
3. Test voice models: `GET /api/test/voices`
4. Test recommendation: `POST /api/test/voice-recommendation`

## Error Handling
- Invalid voice IDs return 400 Bad Request
- Missing required fields return 400 Bad Request
- API failures return 500 Internal Server Error
- All errors include descriptive messages

## Next Steps
1. Add user preference learning
2. Implement voice similarity scoring
3. Add emotional tone detection
4. Create voice training recommendations
5. Add multi-language support