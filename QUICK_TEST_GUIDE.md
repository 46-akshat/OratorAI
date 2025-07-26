# ⚡ Quick Test Guide - 5 Minute Voice Recommendation Test

## 🚀 **Super Quick Test (2 minutes)**

### **Test 1: Hesitant Business Speech**
```
Um, good morning everyone. So, uh, today I want to present our quarterly results. We've, um, achieved some growth this quarter.
```
**Expected**: Confident voice (Marcus/Sarah) to improve authority

### **Test 2: Enthusiastic Casual Speech**
```
Hey everyone! I'm super excited to share this really cool project I've been working on. It's been such an amazing journey!
```
**Expected**: Conversational voice (Alex/Lily) for natural engagement

---

## 🎯 **Medium Test (5 minutes)**

### **Test 3: Professional Business**
```
Good morning. I'm pleased to present our Q3 financial results. Revenue increased 25% to $2.4 million, exceeding all targets.
```
**Expected**: Professional confident voice (Marcus/Sarah)

### **Test 4: Urgent Announcement**
```
Attention everyone! We have a critical update. The project deadline has been moved up by two weeks. We need to act immediately.
```
**Expected**: Urgent energetic voice (Ryan/Maya)

### **Test 5: Supportive Message**
```
I understand this has been challenging for everyone. We're here to support each other through these difficult times.
```
**Expected**: Empathetic caring voice (Michael/Grace)

---

## 🧪 **Full Test Suite (10 minutes)**

### **Business Category**
1. **Confident Executive**: "Our company achieved record profits this quarter..."
2. **Nervous Presenter**: "Um, so today I want to, uh, talk about our results..."
3. **Financial Report**: "Revenue increased 18% to $2.4 million..."

### **Casual Category**
4. **Student Project**: "Hey everyone! I'm excited to share my app..."
5. **Team Update**: "Hi team! Quick update on our project progress..."

### **Urgent Category**
6. **Emergency**: "Attention! Critical system update required immediately..."
7. **Sales Pitch**: "This incredible opportunity ends tomorrow! Don't miss out..."

### **Empathetic Category**
8. **Support**: "I know this is difficult. We're here for each other..."
9. **Sensitive Topic**: "I want to address these changes with transparency..."

---

## ✅ **Quick Checklist**

After each test, verify:
- [ ] **Voice recommendation appears** in right column
- [ ] **Transcribed text shows** in middle column  
- [ ] **Audio player works** with recommended voice
- [ ] **Confidence score** is reasonable (70%+)
- [ ] **Explanation makes sense** for the content
- [ ] **Voice matches expectation** for the speech type

---

## 🔧 **If Something's Wrong**

### **No Voice Recommendation Card**
- Check browser console for errors
- Verify backend is running on port 8080
- Check if API response includes `voiceRecommendation` field

### **No Transcribed Text**
- Check if `spokenTranscript` appears in browser console
- Verify AssemblyAI is working (check backend logs)
- Make sure you recorded audio properly

### **Wrong Voice Recommendation**
- This is actually good! It means the AI is analyzing
- Try different delivery styles (confident vs. hesitant)
- Check if the explanation makes sense

---

## 🎉 **Success Looks Like**

```
┌─────────────────────────────────┐
│ 🎤 Script Input                 │
│ [Your script here...]           │
│ [🔒 Locked]                     │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│ 🎙️ Delivery & Recording         │
│ Recording complete...           │
│                                 │
│ 📊 What You Said:               │
│ "Good morning everyone..."      │ ← Transcribed text
│                                 │
│ [Record Again]                  │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│ 🏆 AI Feedback                  │
│ Overall Score: 8/10             │
│                                 │
│ ✅ What Went Well               │
│ ⚡ Areas for Improvement        │
│                                 │
│ 🎤 Voice Recommendation        │ ← NEW!
│ [M] Marcus                      │
│     Male • confident            │
│ Your speech shows hesitation... │
│ AI Confidence: 85%              │
│ ████████████████░░░░            │
│                                 │
│ 🔊 Hear Your Speech in          │
│    Recommended Voice            │
│ [Audio Player]                  │
└─────────────────────────────────┘
```

## 🎯 **Pro Tips**

1. **Try the same script with different emotions** - see how recommendations change
2. **Add "um" and "uh" to any script** - should always recommend confident voices
3. **Use business words** (revenue, growth, targets) - should get professional voices
4. **Use casual words** (awesome, cool, excited) - should get conversational voices
5. **Use urgent words** (immediately, critical, deadline) - should get energetic voices

**The voice recommendation feature is working when you see different voices recommended for different types of content and delivery styles!** 🎤✨