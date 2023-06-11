import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import android.widget.VideoView

class MainActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var btnChooseVideo: Button
    private lateinit var btnPlayPause: Button
    private lateinit var btnSpeed: Button
    private lateinit var seekBarVolume: SeekBar

    private var isPlaying = false
    private var playbackSpeed = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        btnChooseVideo = findViewById(R.id.btnChooseVideo)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnSpeed = findViewById(R.id.btnSpeed)
        seekBarVolume = findViewById(R.id.seekBarVolume)

        btnChooseVideo.setOnClickListener {
            chooseVideo()
        }

        btnPlayPause.setOnClickListener {
            togglePlayPause()
        }

        btnSpeed.setOnClickListener {
            changeSpeed()
        }

        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun chooseVideo() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        startActivityForResult(intent, 1)
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            videoView.pause()
            btnPlayPause.text = "播放"
        } else {
            videoView.start()
            btnPlayPause.text = "暂停"
        }
        isPlaying = !isPlaying
    }

    private fun changeSpeed() {
        if (playbackSpeed == 1.0f) {
            playbackSpeed = 1.5f
            btnSpeed.text = "1.5x"
        } else if (playbackSpeed == 1.5f) {
            playbackSpeed = 2.0f
            btnSpeed.text = "2x"
        } else {
            playbackSpeed = 1.0f
            btnSpeed.text = "1x"
        }
        videoView.playbackParams.speed = playbackSpeed
    }

    private fun setVolume(volume: Int) {
        val maxVolume = 100
        val calculatedVolume = volume.toFloat() / maxVolume.toFloat()
        videoView.volume = calculatedVolume
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val videoUri: Uri? = data?.data
            if (videoUri != null) {
                videoView.setVideoURI(videoUri)
                videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener {
                    videoView.start()
                    isPlaying = true
                    btnPlayPause.text = "暂停"
                })
            }
        }
    }
}
