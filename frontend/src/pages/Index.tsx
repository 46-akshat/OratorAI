import { useState } from "react";
import axios from "axios";
import { AnimatePresence } from "framer-motion"; // 1. Import AnimatePresence
import ScriptInput from "@/components/ScriptInput";
import Recorder from "@/components/Recorder";
import FeedbackDisplay from "@/components/FeedbackDisplay";
import ModalLoader from "@/components/ModalLoader"; // 2. Import your ModalLoader
import { useToast } from "@/hooks/use-toast";
import { Brain } from "lucide-react";

interface FeedbackData {
  score: number;
  positiveFeedback: string;
  improvementPoints: string;
  audioUrl: string;
}

const Index = () => {
  const [script, setScript] = useState("");
  const [isScriptLocked, setIsScriptLocked] = useState(false);
  const [transcript, setTranscript] = useState("");
  const [isRecording, setIsRecording] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [feedbackData, setFeedbackData] = useState<FeedbackData | null>(null);
  const [error, setError] = useState<string | null>(null);
  const { toast } = useToast();

  const handleLockScript = () => {
    if (script.trim()) {
      setIsScriptLocked(true);
      toast({
        title: "Script Locked",
        description: "You can now start recording your delivery.",
      });
    }
  };

  const handleAnalyze = async () => {
    if (!transcript.trim()) {
      toast({
        title: "No Recording Found",
        description: "Please record your delivery first.",
        variant: "destructive",
      });
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      const response = await axios.post("/api/analyze", {
        originalScript: script,
        spokenTranscript: transcript,
      });

      setFeedbackData(response.data);
      toast({
        title: "Analysis Complete",
        description: "Your presentation feedback is ready!",
      });
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : "Failed to analyze your delivery. Please try again.";
      setError(errorMessage);
      toast({
        title: "Analysis Failed",
        description: errorMessage,
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-background relative"> {/* Added `relative` for positioning context */}
      
      {/* 3. Add the ModalLoader here */}
      {/* It will appear on top of everything when isLoading is true */}
      <AnimatePresence>
        {isLoading && <ModalLoader />}
      </AnimatePresence>

      {/* Header */}
      <header className="border-b border-border bg-gradient-surface">
        <div className="container mx-auto px-6 py-4">
          <div className="flex items-center gap-3">
            <Brain className="h-8 w-8 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">AI Presentation Coach</h1>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto px-6 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 h-[calc(100vh-160px)]">
          {/* Column 1: Script Input */}
          <div className="bg-gradient-surface rounded-xl p-6 border border-border shadow-card">
            <ScriptInput
              script={script}
              onScriptChange={setScript}
              isLocked={isScriptLocked}
              onLockScript={handleLockScript}
            />
          </div>

          {/* Column 2: Recording */}
          <div className="bg-gradient-surface rounded-xl p-6 border border-border shadow-card">
            <Recorder
              isScriptLocked={isScriptLocked}
              transcript={transcript}
              onTranscriptChange={setTranscript}
              isRecording={isRecording}
              onStartRecording={() => setIsRecording(true)}
              onStopRecording={() => setIsRecording(false)}
              onAnalyze={handleAnalyze}
              isLoading={isLoading}
            />
          </div>

          {/* Column 3: Feedback */}
          <div className="bg-gradient-surface rounded-xl p-6 border border-border shadow-card">
            <FeedbackDisplay
              // The modal now handles the global loading UI, so isLoading prop is not needed here
              feedbackData={feedbackData}
              error={error}
            />
          </div>
        </div>
      </main>
    </div>
  );
};

export default Index;