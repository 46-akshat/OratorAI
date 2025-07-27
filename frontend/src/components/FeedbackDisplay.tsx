import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Award, TrendingUp, Volume2, AlertCircle, Download } from "lucide-react";
import { useToast } from "@/hooks/use-toast";

interface FeedbackData {
  score: number;
  positiveFeedback: string;
  improvementPoints: string;
  audioUrl: string;
  spokenTranscript?: string;
}

interface FeedbackDisplayProps {
  feedbackData: FeedbackData | null;
  error: string | null;
}

const FeedbackDisplay = ({ feedbackData, error }: FeedbackDisplayProps) => {
  const { toast } = useToast();

  const cardVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 },
  };

  const staggerContainer = {
    hidden: {},
    visible: {
      transition: {
        staggerChildren: 0.1,
      },
    },
  };

  const getScoreColor = (score: number) => {
    if (score >= 8) return "text-green-400";
    if (score >= 5) return "text-yellow-400";
    return "text-red-400";
  };

  const getScoreBadgeVariant = (score: number): "default" | "secondary" | "destructive" => {
    if (score >= 8) return "default";
    if (score >= 5) return "secondary";
    return "destructive";
  };
  
  const handleDownloadAudio = async () => {
    // ... download audio logic
  };
  
  const handleExportFeedback = async () => {
    // ... export feedback logic
  };

  if (error) {
    return (
      <div className="flex flex-col h-full items-center justify-center text-center">
        <AlertCircle className="h-12 w-12 text-destructive mb-4" />
        <h3 className="text-lg font-semibold text-destructive mb-2">Analysis Failed</h3>
        <p className="text-muted-foreground">{error}</p>
      </div>
    );
  }

  if (feedbackData) {
    return (
      <motion.div initial="hidden" animate="visible" className="flex flex-col h-full">
        <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-3">
                <Award className="h-6 w-6 text-primary" />
                <h2 className="text-xl font-bold text-foreground">AI Feedback</h2>
            </div>
            <Button variant="outline" size="sm" onClick={handleExportFeedback}>
                <Download className="mr-2 h-4 w-4" />
                Export
            </Button>
        </div>
        
        <div className="flex-1 space-y-4 overflow-y-auto pr-2">
          {/* Score Card */}
          <motion.div variants={cardVariants}>
            <Card className="bg-gradient-surface border-border">
              <CardHeader>
                <CardTitle className="flex items-center justify-between text-lg">
                  Overall Score
                  <Badge variant={getScoreBadgeVariant(feedbackData.score)} className="text-lg px-3 py-1">
                    {feedbackData.score}/10
                  </Badge>
                </CardTitle>
              </CardHeader>
            </Card>
          </motion.div>

          {/* "What We Heard" card has been REMOVED */}

          {/* What Went Well Card */}
          <motion.div variants={cardVariants}>
            <Card className="bg-gradient-surface border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-green-400">
                  <Award className="h-5 w-5" /> What Went Well
                </CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-foreground">{feedbackData.positiveFeedback}</p>
              </CardContent>
            </Card>
          </motion.div>

          {/* Areas for Improvement Card */}
          <motion.div variants={cardVariants}>
            <Card className="bg-gradient-surface border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-yellow-400">
                  <TrendingUp className="h-5 w-5" /> Areas for Improvement
                </CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-foreground">{feedbackData.improvementPoints}</p>
              </CardContent>
            </Card>
          </motion.div>

          {/* Audio Feedback Card */}
          {feedbackData.audioUrl && (
            <motion.div variants={cardVariants}>
              <Card className="bg-gradient-surface border-border">
                <CardHeader>
                  <CardTitle className="flex items-center justify-between text-primary">
                    <div className="flex items-center gap-2">
                      <Volume2 className="h-5 w-5" /> Hear an Ideal Delivery
                    </div>
                
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <audio controls className="w-full h-12" src={feedbackData.audioUrl}>
                    Your browser does not support the audio element.
                  </audio>
                </CardContent>
              </Card>
            </motion.div>
          )}
        </div>
      </motion.div>
    );
  }

  return (
    <div className="flex flex-col h-full items-center justify-center text-center text-muted-foreground">
      <Award className="h-16 w-16 mb-4" />
      <h3 className="text-lg font-semibold">Your feedback will appear here</h3>
      <p>Complete your recording and analysis first.</p>
    </div>
  );
};

export default FeedbackDisplay;
