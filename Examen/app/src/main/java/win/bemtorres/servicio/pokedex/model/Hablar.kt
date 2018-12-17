package win.bemtorres.servicio.pokedex.model

import android.app.Activity
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.lang.Exception
import java.util.*

class Hablar(val activity: Activity, val message: String) : TextToSpeech.OnInitListener {

    val tts: TextToSpeech = TextToSpeech(activity, this)

    override fun onInit(i: Int) {
        try{
        if (i == TextToSpeech.SUCCESS) {

            val localeBR =Locale.getDefault();
            val result: Int
            result = tts.setLanguage(localeBR)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(activity, "This Language is not supported", Toast.LENGTH_SHORT).show()
            } else {
                speakOut(message)
            }

        } else {
            Toast.makeText(activity, "Initilization Failed!", Toast.LENGTH_SHORT).show()
        }
        }catch (ex : Exception){
            tts.stop()
            tts.shutdown()
        }

    }

    fun speakOut(message: String) {
        try{
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        }catch (ex : Exception){
            tts.stop()
            tts.shutdown()
        }
    }



}