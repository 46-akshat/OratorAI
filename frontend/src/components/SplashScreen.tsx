import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import { Brain } from 'lucide-react';

interface SplashScreenProps {
  onReady: () => void;
}

const SplashScreen = ({ onReady }: SplashScreenProps) => {
  const [progress, setProgress] = useState(0);
  const [loadingText, setLoadingText] = useState("Initializing...");

  useEffect(() => {
    // This effect now simulates a longer, multi-stage loading process
    
    // Stage 1: Initializing (progress bar is at 0%)
    setTimeout(() => {
      setProgress(25);
      setLoadingText("Connecting to services...");
    }, 1000); // Start after 1s

    // Stage 2: Loading assets
    setTimeout(() => {
      setProgress(70);
      setLoadingText("Loading AI models...");
    }, 2800); // Pause, then jump to 70%

    // Stage 3: Finalizing
    setTimeout(() => {
      setProgress(95);
      setLoadingText("Finalizing setup...");
    }, 4500); // Pause again, then jump to 95%

    // Stage 4: Complete
    setTimeout(() => {
      setProgress(100);
    }, 5000);

    // Stage 5: Transition to the main app
    setTimeout(() => {
      onReady();
    }, 5500); // Wait a moment after completion before fading out

  }, [onReady]);

  return (
    <motion.div
      initial={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      transition={{ duration: 0.5 }}
      className="absolute inset-0 z-50 flex flex-col items-center justify-center bg-background"
    >
      <div className="flex flex-col items-center justify-center flex-1">
        <motion.div layoutId="header-logo-icon" className="mb-4">
          <Brain className="h-16 w-16 text-primary" />
        </motion.div>
        <motion.h1 layoutId="header-logo-text" className="text-4xl font-bold text-foreground">
          Orator AI
        </motion.h1>
      </div>

      <div className="w-full max-w-xs mb-12 text-center">
        {/* The text now updates based on the loading stage */}
        <motion.p 
          key={loadingText} // Use key to re-trigger animation on text change
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.3 }}
          className="text-muted-foreground mb-2 text-sm"
        >
          {loadingText}
        </motion.p>
        
        {/* Progress Bar */}
        <div className="w-full bg-muted rounded-full h-2.5">
          <motion.div
            className="bg-primary h-2.5 rounded-full"
            // ADDED: The initial prop forces the bar to start at 0%
            initial={{ width: '0%' }}
            // The animation is now a spring for a smoother effect
            animate={{ width: `${progress}%` }}
            transition={{ type: "spring", stiffness: 50, damping: 20 }}
          />
        </div>
      </div>
    </motion.div>
  );
};

export default SplashScreen;
