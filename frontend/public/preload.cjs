const { contextBridge, ipcRenderer } = require('electron');

// Expose protected methods that allow the renderer process to use
// the ipcRenderer without exposing the entire object
contextBridge.exposeInMainWorld('electronAPI', {
  openFile: () => ipcRenderer.invoke('dialog:openFile'),
  saveFeedback: (content) => ipcRenderer.invoke('dialog:saveFeedback', content),
  saveAudio: (url) => ipcRenderer.invoke('dialog:saveAudio', url),
});
