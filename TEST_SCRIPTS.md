# 🎤 Test Scripts for Voice Recommendation Feature

## 🧪 **Test Scripts Collection**

Use these scripts to test different scenarios and see how the AI recommends different voices based on content and delivery style.

---

## 📊 **Business Presentation Scripts**

### **Script 1: Confident Business Leader**
```
Good morning everyone. I'm excited to present our Q3 results today. We've achieved a remarkable 25% growth in revenue, exceeding all our targets. Our team has demonstrated exceptional performance across all departments. I'm confident that we're well-positioned for continued success in Q4. Thank you for your attention.
```
**Expected Voice**: Marcus/Sarah (Confident, Professional)
**Expected Tone**: Confident
**Test Focus**: Professional business content

### **Script 2: Hesitant Business Presenter**
```
Um, good morning everyone. So, uh, today I want to present our quarterly results. We've, um, achieved some growth this quarter - about 15% I think. The team has, uh, worked really hard and, um, I believe we can do better next quarter. That's, uh, that's all I have to share today.
```
**Expected Voice**: Marcus/David (Confident Male) or Sarah/Emma (Confident Female)
**Expected Tone**: Confident (to improve hesitation)
**Test Focus**: Filler words and hesitation detection

### **Script 3: Financial Report**
```
Ladies and gentlemen, I'm pleased to report our financial performance for the fiscal year. Revenue increased by 18% to $2.4 million. Operating expenses were well-controlled at $1.8 million. Our net profit margin improved to 12%, demonstrating strong operational efficiency. These results reflect our strategic focus on sustainable growth.
```
**Expected Voice**: Marcus/Sarah (Professional, Authoritative)
**Expected Tone**: Confident
**Test Focus**: Technical/financial content

---

## 💬 **Casual Presentation Scripts**

### **Script 4: Enthusiastic Student**
```
Hey everyone! I'm super excited to share my project with you today. So I've been working on this really cool app that helps students manage their time better. It's been such an amazing journey building this, and I think you're all going to love what I've created. The features are just incredible!
```
**Expected Voice**: Alex/Lily (Conversational, Young)
**Expected Tone**: Conversational
**Test Focus**: Casual, enthusiastic delivery

### **Script 5: Friendly Team Update**
```
Hi team! I wanted to give you all a quick update on our project progress. We've made some really great strides this week. The design phase is complete, and we're moving into development. I'm really happy with how everything is coming together. Thanks for all your hard work!
```
**Expected Voice**: Alex/Sophia (Conversational, Warm)
**Expected Tone**: Conversational
**Test Focus**: Team communication style

---

## 🎓 **Educational Scripts**

### **Script 6: Teacher Explaining Concept**
```
Today we're going to learn about photosynthesis. This is a fascinating process where plants convert sunlight into energy. Think of it as nature's way of making food from light. The process involves chlorophyll, which gives plants their green color. Let's explore how this amazing process works step by step.
```
**Expected Voice**: Sophia/James (Conversational, Educational)
**Expected Tone**: Conversational
**Test Focus**: Educational content delivery

### **Script 7: Training Session**
```
Welcome to our training session on customer service excellence. Today we'll cover three key principles: active listening, empathy, and problem-solving. These skills will help you create positive experiences for every customer interaction. Remember, every customer deserves our best effort and attention.
```
**Expected Voice**: Michael/Grace (Empathetic) or Sophia/James (Conversational)
**Expected Tone**: Empathetic or Conversational
**Test Focus**: Training and development content

---

## 🚨 **Urgent/Important Scripts**

### **Script 8: Urgent Announcement**
```
Attention everyone, I need to share some important information immediately. We've received a critical update that affects our project timeline. The deadline has been moved up by two weeks. We need to accelerate our efforts and prioritize the most essential features. Please adjust your schedules accordingly.
```
**Expected Voice**: Ryan/Maya (Urgent, Energetic)
**Expected Tone**: Urgent
**Test Focus**: Urgency and importance detection

### **Script 9: Sales Pitch**
```
This is an incredible opportunity that you don't want to miss. Our product will revolutionize how you work and save you hours every week. The early bird pricing ends tomorrow, so you need to act fast. Don't let this chance slip away - your future self will thank you for making this decision today.
```
**Expected Voice**: Ryan/Maya (Urgent, Compelling)
**Expected Tone**: Urgent
**Test Focus**: Sales and persuasion content

---

## 💝 **Empathetic/Supportive Scripts**

### **Script 10: Supportive Message**
```
I understand that this has been a challenging time for everyone. We're all facing difficulties, and it's completely normal to feel overwhelmed. Please know that we're here to support each other through this. Your wellbeing is our priority, and we'll work together to find solutions that work for everyone.
```
**Expected Voice**: Michael/Grace (Empathetic, Caring)
**Expected Tone**: Empathetic
**Test Focus**: Emotional support and understanding

### **Script 11: Condolence/Sensitive Topic**
```
I want to address the recent changes in our organization with sensitivity and transparency. I know this news may be concerning, and your feelings are completely valid. We're committed to supporting everyone through this transition. Please don't hesitate to reach out if you need someone to talk to.
```
**Expected Voice**: Michael/Grace (Empathetic, Gentle)
**Expected Tone**: Empathetic
**Test Focus**: Sensitive topic handling

---

## 🎯 **Mixed Tone Scripts**

### **Script 12: Motivational Speech**
```
Every great achievement starts with a single step. You have the power to create the change you want to see. Yes, it will be challenging, but that's what makes success so rewarding. Believe in yourself, take action, and don't let fear hold you back. Your dreams are waiting for you to make them reality.
```
**Expected Voice**: Could vary - Ryan/Maya (Urgent) or Michael/Grace (Empathetic)
**Expected Tone**: Could be Urgent or Empathetic
**Test Focus**: Motivational content analysis

### **Script 13: Problem-Solution Presentation**
```
We're facing a significant challenge with our current system. Customer complaints have increased by 30%, and response times are too slow. However, I have a solution that will address these issues. By implementing this new process, we can reduce response times by 50% and improve customer satisfaction dramatically.
```
**Expected Voice**: Marcus/Sarah (Confident) or Ryan/Maya (Urgent)
**Expected Tone**: Confident or Urgent
**Test Focus**: Problem-solution structure

---

## 🧪 **How to Test**

### **Testing Process**
1. **Copy a script** from above
2. **Paste it into your app's script input**
3. **Lock the script**
4. **Record yourself reading it** (try to match the intended style)
5. **Wait for analysis**
6. **Check the voice recommendation**

### **What to Look For**
- ✅ **Appropriate voice selection** (matches expected voice type)
- ✅ **Correct tone detection** (matches expected tone)
- ✅ **Relevant improvement suggestions**
- ✅ **High confidence score** (80%+)
- ✅ **Clear explanation** of why the voice was recommended

### **Variations to Try**
- **Read the same script with different emotions** (confident vs. hesitant)
- **Add filler words** ("um", "uh") to see if it recommends confident voices
- **Change your pace** (fast vs. slow) to see different recommendations
- **Mix formal and casual language** to test tone detection

---

## 🎉 **Success Indicators**

Your voice recommendation system is working perfectly when:
- ✅ **Business scripts** → Recommend professional voices (Marcus, Sarah, David, Emma)
- ✅ **Casual scripts** → Recommend conversational voices (Alex, Lily, James, Sophia)
- ✅ **Urgent scripts** → Recommend energetic voices (Ryan, Maya)
- ✅ **Supportive scripts** → Recommend empathetic voices (Michael, Grace)
- ✅ **Hesitant delivery** → Always recommends confident voices regardless of content
- ✅ **Clear explanations** → AI explains why each voice will help improve delivery

Happy testing! 🎤✨