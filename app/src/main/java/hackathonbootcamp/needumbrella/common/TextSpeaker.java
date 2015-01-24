package hackathonbootcamp.needumbrella.common;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

import jp.ne.docomo.smt.dev.aitalk.AiTalkTextToSpeech;
import jp.ne.docomo.smt.dev.aitalk.data.AiTalkSsml;
import jp.ne.docomo.smt.dev.common.http.AuthApiKey;

/**
 * テキストを音声に変えて出力（しゃべらせる）クラス
 * 利用例：
 *   TextSpeaker speaker = new TextSpeaker();
 *   speaker.talk("アラーム時間を登録したで");
 */
public class TextSpeaker {

    private AiTalkSsml ssml;
    private AiTalkAsyncTask task;

    public TextSpeaker() {
        AuthApiKey.initializeAuth(AlarmConstants.TEXT_TO_SOUND_API_KEY);
    }


    private class AiTalkAsyncTask extends AsyncTask<Object, Integer, byte[]> {
        public AiTalkAsyncTask() {
            super();
        }

        @Override
        protected byte[] doInBackground(Object... params) {
            byte[] resultData = null;
            try {
                AiTalkTextToSpeech search = new AiTalkTextToSpeech();
                resultData = search.requestAiTalkSsmlToSound(((AiTalkSsml) params[0]).makeSsml());
                int bufSize = AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
                search.convertByteOrder16(resultData);
                AudioTrack at = new AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, bufSize, AudioTrack.MODE_STREAM);
                at.play();
                at.write(resultData, 0, resultData.length);
                Thread.sleep(resultData.length / 32);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return resultData;
        }

        @Override
        protected void onCancelled() {
        }

        @Override
        protected void onPostExecute(byte[] resultData) {

        }
    }

    /**
     * 引数に受け取った文字列を音声に出す
     * @param text 音声にする文字
     */
    public void talk(String text) {
        ssml = new AiTalkSsml();
        ssml.startVoice("nozomi");
        ssml.addText(text);
        ssml.endVoice();

        task = new AiTalkAsyncTask();
        task.execute(ssml);
    }
}
