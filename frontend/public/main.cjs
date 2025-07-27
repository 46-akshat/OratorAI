const { app, BrowserWindow, ipcMain, dialog } = require('electron');
const path = require('path');
const fs = require('fs');
const https = require('https');

function createWindow() {
  const win = new BrowserWindow({
    width: 1200,
    height: 800,
    webPreferences: {
      preload: path.join(__dirname, 'preload.cjs')
    }
  });

  // Load your Vite dev server
  win.loadURL('http://localhost:8081'); // Make sure this port is correct
  
  // Optional: Open DevTools automatically for debugging
  // win.webContents.openDevTools();
}

app.whenReady().then(createWindow);

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      createWindow();
    }
});

// --- IPC Handlers for File System ---

ipcMain.handle('dialog:openFile', async () => {
  try {
    const { canceled, filePaths } = await dialog.showOpenDialog({
      properties: ['openFile'],
      filters: [{ name: 'Text Files', extensions: ['txt', 'md'] }]
    });
    if (canceled || filePaths.length === 0) return null;
    return fs.readFileSync(filePaths[0], 'utf-8');
  } catch (error) {
    console.error('Failed to open file:', error);
    return null;
  }
});

ipcMain.handle('dialog:saveFeedback', async (event, feedbackContent) => {
    try {
        const { canceled, filePath } = await dialog.showSaveDialog({
            title: 'Save Feedback',
            defaultPath: 'presentation-feedback.txt',
            filters: [{ name: 'Text Files', extensions: ['txt'] }]
        });
        if (canceled || !filePath) return false;
        fs.writeFileSync(filePath, feedbackContent);
        return true;
    } catch(error) {
        console.error('Failed to save feedback:', error);
        return false;
    }
});

ipcMain.handle('dialog:saveAudio', async (event, audioUrl) => {
    try {
        const { canceled, filePath } = await dialog.showSaveDialog({
            title: 'Save Ideal Delivery Audio',
            defaultPath: 'ideal-delivery.mp3',
            filters: [{ name: 'MP3 Audio', extensions: ['mp3'] }]
        });

        if (canceled || !filePath) {
            console.log('Save audio dialog was cancelled.');
            return false;
        }

        await new Promise((resolve, reject) => {
            const fileStream = fs.createWriteStream(filePath);
            https.get(audioUrl, (response) => {
                if (response.statusCode !== 200) {
                    reject(new Error(`Failed to download audio. Status code: ${response.statusCode}`));
                    return;
                }
                response.pipe(fileStream);
                fileStream.on('finish', () => {
                    fileStream.close(resolve);
                });
            }).on('error', (err) => {
                fs.unlink(filePath, () => {});
                reject(err);
            });
        });

        console.log('Audio file saved successfully to:', filePath);
        return true;

    } catch (error) {
        console.error('An error occurred in the saveAudio handler:', error);
        return false;
    }
});
