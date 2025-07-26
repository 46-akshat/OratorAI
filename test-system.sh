#!/bin/bash

echo "🎤 AI Voice Coach - System Test"
echo "================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test if backend is running
echo -e "${BLUE}Testing backend connection...${NC}"
if curl -s http://localhost:8080/api/test/health > /dev/null; then
    echo -e "${GREEN}✅ Backend is running on port 8080${NC}"
else
    echo -e "${RED}❌ Backend is not running. Please start it first:${NC}"
    echo "   cd aipresentationmaker/aipresentationbackend/aipresentationbackend"
    echo "   ./mvnw spring-boot:run"
    exit 1
fi

# Test health endpoint
echo -e "\n${BLUE}Testing health endpoint...${NC}"
HEALTH_RESPONSE=$(curl -s http://localhost:8080/api/test/health)
if echo "$HEALTH_RESPONSE" | grep -q "UP"; then
    echo -e "${GREEN}✅ Health check passed${NC}"
else
    echo -e "${RED}❌ Health check failed${NC}"
    echo "Response: $HEALTH_RESPONSE"
fi

# Test voice models
echo -e "\n${BLUE}Testing voice models...${NC}"
VOICES_RESPONSE=$(curl -s http://localhost:8080/api/test/voices)
if echo "$VOICES_RESPONSE" | grep -q "success"; then
    echo -e "${GREEN}✅ Voice models loaded successfully${NC}"
    VOICE_COUNT=$(echo "$VOICES_RESPONSE" | grep -o '"voiceId"' | wc -l)
    echo -e "${YELLOW}   Found $VOICE_COUNT available voices${NC}"
else
    echo -e "${RED}❌ Voice models test failed${NC}"
fi

# Test voice recommendation
echo -e "\n${BLUE}Testing voice recommendation...${NC}"
RECOMMENDATION_RESPONSE=$(curl -s -X POST http://localhost:8080/api/test/voice-recommendation)
if echo "$RECOMMENDATION_RESPONSE" | grep -q "success"; then
    echo -e "${GREEN}✅ Voice recommendation working${NC}"
    
    # Extract recommended voice
    RECOMMENDED_VOICE=$(echo "$RECOMMENDATION_RESPONSE" | grep -o '"voiceId":"[^"]*"' | head -1 | cut -d'"' -f4)
    if [ ! -z "$RECOMMENDED_VOICE" ]; then
        echo -e "${YELLOW}   Recommended voice: $RECOMMENDED_VOICE${NC}"
    fi
else
    echo -e "${RED}❌ Voice recommendation test failed${NC}"
    echo "Response: $RECOMMENDATION_RESPONSE"
fi

# Test with custom speech
echo -e "\n${BLUE}Testing with custom speech...${NC}"
CUSTOM_TEST=$(curl -s -X POST http://localhost:8080/api/voice/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "speechText": "Good morning everyone. I am excited to present our quarterly results today. We have achieved significant growth and I believe we are well positioned for the future.",
    "presentationType": "business",
    "targetAudience": "professional"
  }')

if echo "$CUSTOM_TEST" | grep -q "recommendedVoice"; then
    echo -e "${GREEN}✅ Custom speech analysis working${NC}"
    
    # Extract analysis details
    DETECTED_TONE=$(echo "$CUSTOM_TEST" | grep -o '"detectedTone":"[^"]*"' | cut -d'"' -f4)
    RECOMMENDED_TONE=$(echo "$CUSTOM_TEST" | grep -o '"recommendedTone":"[^"]*"' | cut -d'"' -f4)
    
    if [ ! -z "$DETECTED_TONE" ] && [ ! -z "$RECOMMENDED_TONE" ]; then
        echo -e "${YELLOW}   Detected tone: $DETECTED_TONE${NC}"
        echo -e "${YELLOW}   Recommended tone: $RECOMMENDED_TONE${NC}"
    fi
else
    echo -e "${RED}❌ Custom speech analysis failed${NC}"
fi

# Check if frontend file exists
echo -e "\n${BLUE}Checking frontend...${NC}"
if [ -f "frontend/voice-coach.html" ]; then
    echo -e "${GREEN}✅ Frontend file exists${NC}"
    echo -e "${YELLOW}   Open frontend/voice-coach.html in your browser${NC}"
else
    echo -e "${RED}❌ Frontend file not found${NC}"
fi

# Final summary
echo -e "\n${BLUE}================================${NC}"
echo -e "${GREEN}🎉 System Test Complete!${NC}"
echo -e "\n${YELLOW}Next Steps:${NC}"
echo "1. Open frontend/voice-coach.html in your browser"
echo "2. Try the sample speeches or enter your own"
echo "3. Check the console logs for detailed information"
echo -e "\n${YELLOW}API Endpoints Available:${NC}"
echo "• http://localhost:8080/api/voice/recommend"
echo "• http://localhost:8080/api/voice/analyze" 
echo "• http://localhost:8080/api/voice/voices"
echo "• http://localhost:8080/api/test/health"

echo -e "\n${BLUE}Happy voice coaching! 🎤${NC}"