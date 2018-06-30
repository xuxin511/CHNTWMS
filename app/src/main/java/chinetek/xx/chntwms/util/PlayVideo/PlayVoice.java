package chinetek.xx.chntwms.util.PlayVideo;
import android.content.Context;
import android.media.MediaPlayer;
import chinetek.xx.chntwms.cywms.R;


/**
 * Created by 123 on 2018/5/2.
 */

public class PlayVoice {
    public static MediaPlayer mediaPlayer1 = null;
    public static void PlayError(Context content) {
        mediaPlayer1 = MediaPlayer.create(content, R.raw.error3);
        mediaPlayer1.start();
    }

}
