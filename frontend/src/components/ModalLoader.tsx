import React from 'react';
import { motion } from 'framer-motion';

const ModalLoader = () => {
  return (
    // This is the main overlay div.
    // It's fixed to cover the whole screen and has a high z-index to be on top.
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      transition={{ duration: 0.3 }}
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm"
    >
      <div className="text-center space-y-4">
        {/* You can use the previous loader animation or this new one */}
        <motion.div
          animate={{ rotate: 360 }}
          transition={{ duration: 1.5, repeat: Infinity, ease: "linear" }}
          className="w-16 h-16 border-4 border-primary border-t-transparent rounded-full mx-auto"
        />
        <motion.p 
          initial={{ y: 10, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="text-xl font-medium text-white"
        >
          Analyzing your delivery...
        </motion.p>
        <p className="text-gray-400">This may take a few moments</p>
      </div>
    </motion.div>
  );
};

export default ModalLoader;