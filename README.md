# Kotlin-VideoPlayer
Kotlin XML安卓视频播放器
<RelativeLayout>：根布局，它是一个相对布局，用于容纳其他视图元素。
xmlns:android="http://schemas.android.com/apk/res/android"：定义了Android命名空间。
xmlns:tools="http://schemas.android.com/tools"：定义了用于设计时预览的工具命名空间。
android:layout_width="match_parent"：设置布局的宽度与父布局相匹配。
android:layout_height="match_parent"：设置布局的高度与父布局相匹配。
tools:context=".MainActivity"：指定与该布局关联的上下文，这里是MainActivity。
<VideoView>：用于显示视频的视图元素。
android:id="@+id/videoView"：设置该视图的唯一标识符，以便在代码中引用。
android:layout_width="match_parent"：设置视图的宽度与父布局相匹配。
android:layout_height="match_parent"：设置视图的高度与父布局相匹配。
<LinearLayout>：用于容纳功能按钮的水平线性布局。
android:layout_width="match_parent"：设置布局的宽度与父布局相匹配。
android:layout_height="wrap_content"：设置布局的高度根据内容自适应。
android:layout_alignParentBottom="true"：将布局设置在父布局的底部。
android:orientation="horizontal"：设置子视图水平排列。
android:padding="16dp"：设置布局的内边距为16dp。
<Button>：功能按钮，用于选择视频、播放/暂停和速度控制。
android:id="@+id/btnChooseVideo"：设置按钮的唯一标识符。
android:layout_width="wrap_content"：设置按钮的宽度根据内容自适应。
android:layout_height="wrap_content"：设置按钮的高度根据内容自适应。
android:text="选择视频"：设置按钮显示的文本内容为"选择视频"。
<SeekBar>：用于控制音量大小的滑动条。
android:id="@+id/seekBarVolume"：设置滑动条的唯一标识符。
android:layout_width="0dp"：设置滑动条的宽度为0，以便与其他控件共享剩余空间。
android:layout_height="wrap_content"：设置滑动条的高度根据内容自适应。
android:layout_weight="1"：设置滑动条在父布局中占据剩余的空间。
  
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import android.widget.VideoView
```
- 导入所需的类和库。

class MainActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var btnChooseVideo: Button
    private lateinit var btnPlayPause: Button
    private lateinit var btnSpeed: Button
    private lateinit var seekBarVolume: SeekBar

    private var isPlaying = false
    private var playbackSpeed = 1.0f

- 创建`MainActivity`类，继承自`AppCompatActivity`，表示这是主活动类。
- 声明视频播放器和功能按钮的变量。
- 声明一些控制视频播放的状态变量。

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        btnChooseVideo = findViewById(R.id.btnChooseVideo)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnSpeed = findViewById(R.id.btnSpeed)
        seekBarVolume = findViewById(R.id.seekBarVolume)

- `onCreate`方法是活动创建时调用的函数，设置布局文件并获取界面元素的引用。


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

- 设置按钮的点击事件监听器，分别对应选择视频、切换播放/暂停、切换速度和调整音量的功能。

    private fun chooseVideo() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*"
        startActivityForResult(intent, 1)
    }

- `chooseVideo()`方法用于打开文件选择器，让用户选择视频文件。

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

- `togglePlayPause()`方法用于切换播放/暂停状态。如果正在播放，则暂停视频并将按钮文本设置为"播放"；如果是暂停状态，则开始播放视频并将按钮文本设置为"暂停"。

    private fun changeSpeed() {
        if (playbackSpeed == 1.0f) {
            playbackSpeed = 1.5f
            btnSpeed.text = "1.5x"
        } else if (playbackSpeed == 

1.5f) {
            playbackSpeed = 2.0f
            btnSpeed.text = "2x"
        } else {
            playbackSpeed = 1.0f
            btnSpeed.text = "1x"
        }
        videoView.playbackParams.speed = playbackSpeed
    }
- `changeSpeed()`方法用于切换视频播放速度。如果当前速度是1.0倍，则切换到1.5倍速度并将按钮文本设置为"1.5x"；如果当前速度是1.5倍，则切换到2.0倍速度并将按钮文本设置为"2x"；如果当前速度是2.0倍，则切换回1.0倍速度并将按钮文本设置为"1x"。

    private fun setVolume(volume: Int) {
        val maxVolume = 100
        val calculatedVolume = volume.toFloat() / maxVolume.toFloat()
        videoView.volume = calculatedVolume
    }
- `setVolume()`方法用于设置视频的音量大小。根据`SeekBar`传入的音量进度值，计算出音量的相对大小，然后设置给视频播放器。

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
- `onActivityResult()`方法用于处理从文件选择器返回的结果。如果选择了视频文件，将其设置到视频播放器中，并在准备完成后开始播放视频。设置标志位和按钮文本以反映播放状态。
