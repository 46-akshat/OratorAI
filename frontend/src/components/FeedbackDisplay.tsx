import { motion, AnimatePresence } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Loader2, Award, TrendingUp, Volume2, AlertCircle } from "lucide-react";

interface FeedbackData {
  score: number;
  positiveFeedback: string;
  improvementPoints: string;
  audioUrl: string;
}

interface FeedbackDisplayProps {
  isLoading: boolean;
  feedbackData: FeedbackData | null;
  error: string | null;
}

const FeedbackDisplay = ({ isLoading, feedbackData, error }: FeedbackDisplayProps) => {
  const cardVariants = {
    hidden: { opacity: 0, y: 20, scale: 0.95 },
    visible: { opacity: 1, y: 0, scale: 1 },
    exit: { opacity: 0, y: -20, scale: 0.95 }
  };

  const staggerContainer = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1
      }
    }
  };

  const getScoreColor = (score: number) => {
    if (score >= 8) return "text-success";
    if (score >= 6) return "text-primary";
    return "text-destructive";
  };

  const getScoreBadgeVariant = (score: number): "default" | "secondary" | "destructive" => {
    if (score >= 8) return "default";
    if (score >= 6) return "secondary";
    return "destructive";
  };

  if (isLoading) {
    return (
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        exit={{ opacity: 0, scale: 0.95 }}
        transition={{ duration: 0.3 }}
        className="flex flex-col h-full"
      >
        <motion.div 
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4, delay: 0.1 }}
          className="flex items-center gap-3 mb-4"
        >
          <Award className="h-6 w-6 text-primary" />
          <h2 className="text-xl font-bold text-foreground">AI Feedback</h2>
        </motion.div>
        
        <motion.div 
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="flex-1 flex items-center justify-center"
        >
          <div className="text-center space-y-4">
            <motion.div
              animate={{ rotate: 360 }}
              transition={{ duration: 2, repeat: Infinity, ease: "linear" }}
              className="w-12 h-12 border-4 border-primary border-t-transparent rounded-full mx-auto"
            />
            <motion.p 
              animate={{ opacity: [0.5, 1, 0.5] }}
              transition={{ duration: 1.5, repeat: Infinity }}
              className="text-lg font-medium text-foreground"
            >
              Analyzing your delivery...
            </motion.p>
            <p className="text-muted-foreground">This may take a few moments</p>
          </div>
        </motion.div>
      </motion.div>
    );
  }

  if (error) {
    return (
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.3 }}
        className="flex flex-col h-full"
      >
        <div className="flex items-center gap-3 mb-4">
          <Award className="h-6 w-6 text-primary" />
          <h2 className="text-xl font-bold text-foreground">AI Feedback</h2>
        </div>
        
        <motion.div 
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4 }}
          className="flex-1 flex items-center justify-center"
        >
          <Card className="w-full max-w-md bg-destructive/10 border-destructive/20">
            <CardContent className="pt-6 text-center">
              <AlertCircle className="h-12 w-12 text-destructive mx-auto mb-4" />
              <h3 className="text-lg font-semibold text-destructive mb-2">Analysis Failed</h3>
              <p className="text-muted-foreground">{error}</p>
            </CardContent>
          </Card>
        </motion.div>
      </motion.div>
    );
  }

  if (!feedbackData) {
    return (
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5, ease: "easeOut", delay: 0.2 }}
        className="flex flex-col h-full"
      >
        <motion.div 
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4, delay: 0.3 }}
          className="flex items-center gap-3 mb-4"
        >
          <Award className="h-6 w-6 text-primary" />
          <h2 className="text-xl font-bold text-foreground">AI Feedback</h2>
        </motion.div>
        
        <motion.div 
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4, delay: 0.4 }}
          className="flex-1 flex items-center justify-center"
        >
          <div className="text-center space-y-4">
            <Award className="h-16 w-16 text-muted-foreground/50 mx-auto" />
            <p className="text-lg text-muted-foreground">Your feedback will appear here</p>
            <p className="text-sm text-muted-foreground">Complete your recording and analysis first</p>
          </div>
        </motion.div>
      </motion.div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5, ease: "easeOut" }}
      className="flex flex-col h-full"
    >
      <motion.div 
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4, delay: 0.1 }}
        className="flex items-center gap-3 mb-4"
      >
        <Award className="h-6 w-6 text-primary" />
        <h2 className="text-xl font-bold text-foreground">AI Feedback</h2>
      </motion.div>
      
      <motion.div 
        variants={staggerContainer}
        initial="hidden"
        animate="visible"
        className="flex-1 space-y-4"
      >
        {/* Score Card */}
        <motion.div variants={cardVariants} transition={{ duration: 0.4, ease: "easeOut" }}>
          <Card className="bg-gradient-surface border-border shadow-card">
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center justify-between">
                <span className="text-lg">Overall Score</span>
                <motion.div
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  transition={{ duration: 0.5, delay: 0.3, type: "spring", stiffness: 200 }}
                >
                  <Badge variant={getScoreBadgeVariant(feedbackData.score)} className="text-lg px-3 py-1">
                    {feedbackData.score}/10
                  </Badge>
                </motion.div>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex items-center gap-2">
                <div className="flex-1 bg-muted rounded-full h-3">
                  <motion.div 
                    initial={{ width: 0 }}
                    animate={{ width: `${(feedbackData.score / 10) * 100}%` }}
                    transition={{ duration: 1.2, delay: 0.5, ease: "easeOut" }}
                    className={`h-3 rounded-full ${
                      feedbackData.score >= 8 ? 'bg-success' : 
                      feedbackData.score >= 6 ? 'bg-primary' : 'bg-destructive'
                    }`}
                  />
                </div>
                <motion.span 
                  initial={{ opacity: 0, scale: 0 }}
                  animate={{ opacity: 1, scale: 1 }}
                  transition={{ duration: 0.5, delay: 0.8 }}
                  className={`font-bold text-2xl ${getScoreColor(feedbackData.score)}`}
                >
                  {feedbackData.score}
                </motion.span>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* What Went Well */}
        <motion.div variants={cardVariants} transition={{ duration: 0.4, ease: "easeOut" }}>
          <Card className="bg-gradient-surface border-border shadow-card">
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2 text-success">
                <motion.div
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  transition={{ duration: 0.3, delay: 0.2 }}
                >
                  <Award className="h-5 w-5" />
                </motion.div>
                What Went Well
              </CardTitle>
            </CardHeader>
            <CardContent>
              <motion.p 
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.5, delay: 0.3 }}
                className="text-foreground leading-relaxed"
              >
                {feedbackData.positiveFeedback}
              </motion.p>
            </CardContent>
          </Card>
        </motion.div>

        {/* Areas for Improvement */}
        <motion.div variants={cardVariants} transition={{ duration: 0.4, ease: "easeOut" }}>
          <Card className="bg-gradient-surface border-border shadow-card">
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2 text-primary">
                <motion.div
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  transition={{ duration: 0.3, delay: 0.2 }}
                >
                  <TrendingUp className="h-5 w-5" />
                </motion.div>
                Areas for Improvement
              </CardTitle>
            </CardHeader>
            <CardContent>
              <motion.p 
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.5, delay: 0.3 }}
                className="text-foreground leading-relaxed"
              >
                {feedbackData.improvementPoints}
              </motion.p>
            </CardContent>
          </Card>
        </motion.div>

        {/* Audio Feedback */}
        <motion.div 
          variants={cardVariants} 
          transition={{ duration: 0.4, ease: "easeOut" }}
          whileHover={{ scale: 1.02 }}
        >
          <Card className="bg-gradient-surface border-border shadow-card">
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2 text-primary">
                <motion.div
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  transition={{ duration: 0.3, delay: 0.2 }}
                >
                  <Volume2 className="h-5 w-5" />
                </motion.div>
                Audio Feedback
              </CardTitle>
            </CardHeader>
            <CardContent>
              <motion.audio
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5, delay: 0.3 }}
                controls 
                className="w-full h-12"
                src={feedbackData.audioUrl}
                style={{
                  filter: 'sepia(20%) saturate(70%) hue-rotate(200deg) brightness(1.2)'
                }}
              >
                Your browser does not support the audio element.
              </motion.audio>
            </CardContent>
          </Card>
        </motion.div>
      </motion.div>
    </motion.div>
  );
};

export default FeedbackDisplay;