package win.bemtorres.servicio.pokedex


import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class Fr_home : Fragment() {

    var li : LinearLayout? = null
    var miContexto : Context?= null
    var music :  MediaPlayer? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_fr_home, container, false)

        li = view.findViewById(R.id.LL_Foto)
        li!!.setBackgroundResource(R.drawable.ic_pika)

        li!!.setOnClickListener{
            if(music!=null) music!!.stop()
            music = MediaPlayer.create(miContexto,R.raw.audio)
            music!!.start()
        }

        return  view
    }


}
