package com.zhengdianfang.dazhongbao.views.im.components

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.media.MediaRecorder
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.zhengdianfang.dazhongbao.R
import java.io.File
import java.io.IOException


/**
 * Created by dfgzheng on 02/09/2017.
 */
class RecordButton : Button {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun setSavePath(path: String) {
        mFileRoot = path
    }

    fun setOnFinishedRecordListener(listener: OnFinishedRecordListener) {
        finishedListener = listener
    }

    private var mFileName: String? = null
    private var mFileRoot: String? = null

    private var finishedListener: OnFinishedRecordListener? = null
    private var startTime: Long = 0


    private var recordIndicator: Dialog? = null

    private var recorder: MediaRecorder? = null

    private var thread: ObtainDecibelThread? = null

    private var volumeHandler: Handler? = null

    private fun init() {
        volumeHandler = ShowVolumeHandler()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (mFileRoot == null)
            return false

        val action = event.action

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                setText(R.string.btn_text_up_send)
                initDialogAndStartRecord()
            }
            MotionEvent.ACTION_UP -> {
                this.setText(R.string.btn_text_speak)
                finishRecord()
            }
            MotionEvent.ACTION_CANCEL -> {
                cancelRecord()
                this.setText(R.string.btn_text_speak)
                Toast.makeText(getContext(), "cancel", Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }

    private fun initDialogAndStartRecord() {


        recordIndicator = Dialog(context, R.style.VoiceDialog)
        view = ImageView(context)
        view!!.setPadding(40, 40, 40, 40)
        view!!.setImageResource(R.mipmap.microphone1)
        recordIndicator!!.getWindow().setContentView(view)
        recordIndicator!!.setOnDismissListener(onDismiss)
        val lp = recordIndicator!!.getWindow().getAttributes()
        lp.gravity = Gravity.CENTER

        startRecording()
        recordIndicator!!.show()
    }

    private fun finishRecord() {
        stopRecording()
        recordIndicator!!.dismiss()

        val intervalTime = System.currentTimeMillis() - startTime
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(getContext(), R.string.record_lenght_too_short, Toast.LENGTH_SHORT).show()
            val file = File(mFileName)
            file.delete()
            return
        }

        if (finishedListener != null)
            finishedListener!!.onFinishedRecord(mFileName, (intervalTime / 1000).toInt())
    }

    private fun cancelRecord() {
        stopRecording()
        recordIndicator!!.dismiss()
        Toast.makeText(getContext(), R.string.cancel_record, Toast.LENGTH_SHORT).show()
        val file = File(mFileName)
        file.delete()
    }

    private fun startRecording() {
        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setAudioChannels(1)
        recorder!!.setAudioEncodingBitRate(4000)
        recorder!!.setAudioSamplingRate(16 * 1000)
        //		recorder.setMaxDuration(MAX_TIME);
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        // recorder.setVideoFrameRate(4000);
        mFileName = mFileRoot + System.currentTimeMillis() + ".amr"
        recorder!!.setOutputFile(mFileName)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        startTime = System.currentTimeMillis()
        recorder!!.start()
        thread = ObtainDecibelThread()
        thread!!.start()

    }

    private fun stopRecording() {
        if (thread != null) {
            thread!!.exit()
            thread = null
        }
        if (recorder != null) {
            try {
                recorder!!.stop()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                recorder!!.release()
                recorder = null
            }
        }
    }

    private inner class ObtainDecibelThread : Thread() {

        @Volatile private var running = true

        fun exit() {
            running = false
        }

        override fun run() {
            while (running) {
                try {
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                if (recorder == null || !running) {
                    break
                }
                val x = recorder!!.maxAmplitude
                if (x != 0) {
                    val f = (10 * Math.log(x.toDouble()) / Math.log(10.0)).toInt()
                    if (f < 26)
                        volumeHandler!!.sendEmptyMessage(0)
                    else if (f < 32)
                        volumeHandler!!.sendEmptyMessage(1)
                    else if (f < 38)
                        volumeHandler!!.sendEmptyMessage(2)
                    else
                        volumeHandler!!.sendEmptyMessage(3)

                }

            }
        }

    }

    private val onDismiss = object : DialogInterface.OnDismissListener {

        override fun onDismiss(dialog: DialogInterface) {
            stopRecording()
        }
    }

    internal class ShowVolumeHandler : Handler() {
        override fun handleMessage(msg: Message) {
            view!!.setImageResource(res[msg.what])
        }
    }

    interface OnFinishedRecordListener {
        fun onFinishedRecord(audioPath: String?, time: Int)
    }

    companion object {

        private val TAG = "RecordButton"

        private val MIN_INTERVAL_TIME = 2000// 2s

        private val res = intArrayOf(R.mipmap.microphone1, R.mipmap.microphone2, R.mipmap.microphone3, R.mipmap.microphone4, R.mipmap.microphone5)

        private var view: ImageView? = null

        val MAX_TIME = 60
    }

    // private boolean
}