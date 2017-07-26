package tiiehenry.content;

import android.content.*;

public class ClipBoardManager {

  private android.content.ClipboardManager clipboard;
  private Context c;

  public ClipBoardManager(Context c) {
	this.c = c;
	clipboard = (android.content.ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
  }
  
  public void copyToClipboard(String text) {
	clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
  }

  public int getItemCount() {
	ClipData data = clipboard.getPrimaryClip();
	return data.getItemCount();
  }

  public String getText(int index) {
	ClipData clip = clipboard.getPrimaryClip();
	if (clip != null && clip.getItemCount() > index) {
	  return String.valueOf(clip.getItemAt(0).coerceToText(c));
	}
	return null;
  }

  public String getLatestText() {
	ClipData clip = clipboard.getPrimaryClip();
	if (clip != null && clip.getItemCount() > 0) {
	  return String.valueOf(clip.getItemAt(0).coerceToText(c));
	}
	return null;
  }
}
