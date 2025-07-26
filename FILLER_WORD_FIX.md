# Filler Word Detection Fix

## Problem
AssemblyAI was not capturing filler words like "um", "uh", "like", "you know" in the speech-to-text transcription, making it impossible to analyze speech patterns and provide accurate voice recommendations.

## Root Cause
The AssemblyAI transcription request was using default settings that:
1. **Filtered out filler words** - `filter_profanity` was not explicitly set to `false`
2. **Missing disfluencies detection** - The `disfluencies` parameter was not enabled
3. **Text formatting** - `format_text` was removing raw speech patterns

## Solution Applied

### 1. Updated TranscriptionRequest Model
Added support for the `disfluencies` parameter:
```java
@JsonProperty("disfluencies")
private Boolean disfluencies;
```

### 2. Enhanced AssemblyAI Service Configuration
```java
TranscriptionRequest request = new TranscriptionRequest.Builder()
    .audioUrl(audioUrl)
    .languageCode("en_us")
    .punctuate(true)
    .formatText(false)        // Keep raw text for better filler word detection
    .filterProfanity(false)   // Don't filter out filler words like "um", "uh"
    .disfluencies(true)       // ENABLE: Detect filler words and speech disfluencies
    .build();
```

## Key Parameters Explained

| Parameter | Value | Purpose |
|-----------|-------|---------|
| `filter_profanity` | `false` | Prevents removal of filler words that might be classified as "profanity" |
| `disfluencies` | `true` | Specifically enables detection of speech disfluencies (um, uh, like, etc.) |
| `format_text` | `false` | Keeps raw transcription without cleaning up speech patterns |
| `punctuate` | `true` | Maintains sentence structure for better analysis |

## Expected Results

### Before Fix
```
Input: "Um, hello, uh, this is a test, like, you know, with filler words"
Output: "Hello, this is a test with filler words"
```

### After Fix
```
Input: "Um, hello, uh, this is a test, like, you know, with filler words"
Output: "Um, hello, uh, this is a test, like, you know, with filler words"
```

## Testing

Use the test file `test-filler-words.http` to verify:
1. Record audio with deliberate filler words
2. Send to `/api/test-transcribe` endpoint
3. Verify transcript includes all filler words
4. Check that voice recommendation system detects "hesitant" tone

## Impact on Voice Recommendations

With filler words now captured, the system can:
- Detect hesitant speaking patterns
- Recommend confident voices (Marcus, David) for speakers with many filler words
- Provide more accurate tone analysis
- Give better improvement suggestions through Gemini AI

## Files Modified
- `TranscriptionRequest.java` - Added disfluencies parameter
- `AssemblyAIService.java` - Updated transcription configuration
- `test-filler-words.http` - Created test case for verification