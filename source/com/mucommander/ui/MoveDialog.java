
package com.mucommander.ui;

import com.mucommander.file.AbstractFile;
import com.mucommander.file.FileSet;
import com.mucommander.job.MoveJob;
import com.mucommander.text.Translator;


/**
 * Dialog invoked when the user wants to move or rename (F6) files.
 *
 * @author Maxence Bernard
 */
public class MoveDialog extends DestinationDialog {

	
    public MoveDialog(MainFrame mainFrame, FileSet files, boolean isShiftDown) {
        super(mainFrame, files);
		
        int nbFiles = files.size();
        boolean rename = isShiftDown && nbFiles==1;

        init(Translator.get(rename?"move_dialog.rename":"move_dialog.move"),
             Translator.get(rename?"move_dialog.rename_description":"move_dialog.move_description"),
             Translator.get(rename?"move_dialog.rename":"move_dialog.move"),
             Translator.get("move_dialog.error_title"));
        
        String fieldText;
        if(isShiftDown && nbFiles==1) {
            fieldText = ((AbstractFile)files.elementAt(0)).getName();
            // If rename mode, select the filename without extension, only if filename part is not empty (unlike '.DS_Store' for example)
            int extPos = fieldText.indexOf('.');
            int len = fieldText.length();
			
            setTextField(fieldText, 0, extPos>0?extPos:len);
        }
        else {
            AbstractFile destFolder = mainFrame.getInactiveTable().getCurrentFolder();
            fieldText = destFolder.getAbsolutePath(true);
            // Append filename to destination path if there is only one file to move
            // and if the file is not a directory that already exists in destination
            // (otherwise folder would be moved inside the destination folder)
            if(nbFiles==1) {
                AbstractFile file = ((AbstractFile)files.elementAt(0));
                AbstractFile testFile;
                if(!(file.isDirectory() && (testFile=AbstractFile.getAbstractFile(fieldText+file.getName())).exists() && testFile.isDirectory()))
                    fieldText += file.getName();
            }
			
            setTextField(fieldText);
        }
 
        showDialog();
    }



    /**
     * Starts a MoveJob. This method is trigged by the 'OK' button or return key.
     */
    protected void startJob(AbstractFile destFolder, String newName, int defaultFileExistsAction) {
        if (newName==null && files.getBaseFolder().equals(destFolder) || files.contains(destFolder)) {
            showErrorDialog(Translator.get("same_source_destination"));
            return;
        }
		
        // Starts moving files
        ProgressDialog progressDialog = new ProgressDialog(mainFrame, Translator.get("move_dialog.moving"));
        MoveJob moveJob = new MoveJob(progressDialog, mainFrame, files, destFolder, newName, defaultFileExistsAction);
        progressDialog.start(moveJob);
    }
	
}
